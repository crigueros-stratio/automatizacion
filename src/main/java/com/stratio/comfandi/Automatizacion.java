package com.stratio.comfandi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.FSDataInputStream;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.LocatedFileStatus;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.fs.RemoteIterator;
//import org.apache.spark.SparkConf;
//import org.apache.spark.rdd.RDD;
//import org.apache.spark.sql.Dataset;
//import org.apache.spark.sql.Row;
//import org.apache.spark.sql.SQLContext;
//import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stratio.comfandi.model.ParametrizacionIngesta;
import com.stratio.comfandi.service.ConnectionService;
import com.stratio.comfandi.service.ParametrizacionService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class Automatizacion {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ParametrizacionService parametrizacionService;

	@Autowired
	private ConnectionService connection;

	private final static Logger log = Logger.getLogger(Automatizacion.class.getName());

	
	/**
	 * Esta funcion obtiene los registros activos de la tabla ingesta, con esta informacion se llama cada flujo de rocket parametrizado
	 * y armando su json respectivo, de esta forma se corren los flujos secuencialmente y paralelamente de las ingestas.
	 * @return
	 */
	@GetMapping("/start")
	ResponseEntity<String> getStart() {
		try {
			List<ParametrizacionIngesta> info = parametrizacionService.findWithCondicionFechas(1);

			info.forEach(x -> {
				HttpsURLConnection urlConnection = connection.confParameters();
				
				String json = toJson(x);
				try (OutputStream os = urlConnection.getOutputStream()) {
					byte[] input = json.getBytes("utf-8");
					os.write(input, 0, input.length);
				} catch (IOException e) {
					e.getMessage();
				}

				StringBuilder sb = new StringBuilder();
				int HttpResult;
				try {
					HttpResult = urlConnection.getResponseCode();

					if (HttpResult == HttpURLConnection.HTTP_OK) {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
						String line = null;
						while ((line = br.readLine()) != null) {
							sb.append(line + "\n");
						}
						br.close();
						System.out.println("" + sb.toString());
					} else {
						System.out.println(urlConnection.getResponseMessage());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().body("");
	}
	
	/**
	 * Crea json segun lo parametrizado en base de datos tabla ingesta
	 * @param par
	 * @return
	 */
	public String toJson(ParametrizacionIngesta par) {
		StringBuilder s = new StringBuilder();
		s.append("{  \"workflowId\": \"" + par.getId_flujo() + "\",");
		s.append("\"executionContext\": {\"extraParams\": [ ");
		s.append("{\"name\": \"id_ingesta\",\"value\": \""+ par.getId() + "\"}");
		
		
		if (par.getConsulta_destino() != null ) {
			s.append(",{\"name\": \"statement\",\"value\": \"" + par.getConsulta_destino() + "\"}");

			String fechaMax = par.getConsulta_destino().substring(par.getConsulta_destino().indexOf("BETWEEN")+8, par.getConsulta_destino().indexOf("AND"));
			s.append(",{\"name\": \"fechaMax\",\"value\": \""+ fechaMax.trim().replace("'", "")+ "\"}");
		}


		s.append("]},");
		s.append("\"projectId\": \"string\"}");

		return s.toString();
	}

}
