package com.stratio.comfandi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.List;

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
import java.util.logging.Logger;

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

	@GetMapping("/start")
	ResponseEntity<String> getStart() {
		try {
//			prueba();
			
			List<ParametrizacionIngesta> info = parametrizacionService.findWithCondicionFechas(1);

			info.forEach(x -> {
				HttpsURLConnection urlConnection = connection.confParameters();

				if (x.getConsulta_origen()!=null && !x.getConsulta_origen().isEmpty()) {
					/**
					 * validQuery: Retorna resultado de la query origen y la concatena en el destino
					 */
					x.setConsulta_destino(x.getConsulta_destino().concat("'" + validQuery(x) + "'"));
				}
				
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

	private String validQuery(ParametrizacionIngesta info) {
		try {
			return connection.consulta(info.getConsulta_origen());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

//	public void prueba() throws IOException {
//		String tabla = "prueba";
//		String path = System.getenv().get("PAH_PARQUET") == null ? "/home/stratio7/kmilo/Vanti/GOLIVE/ZDOCURTR/Parquet"
//				: System.getenv().get("PAH_PARQUET");
//
//		SparkConf configuracion = new SparkConf().setAppName("prueba").setMaster("local").set("spark.driver.memory",
//				"4g");
//
//		SparkSession sparkSession = SparkSession.builder().appName("prueba").master("local[*]").config(configuracion)
//				.getOrCreate();
//		SQLContext sqlContext = sparkSession.sqlContext();
//
//		Configuration conf = new Configuration();
//
//		FileSystem fs = FileSystem.get(conf);
//		System.out.println(fs.getHomeDirectory());
//
//		RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(new Path(path), true);
//		while (fileStatusListIterator.hasNext()) {
//			LocatedFileStatus fileStatus = fileStatusListIterator.next();
//			String pathFile = fileStatus.getPath().toString();
//			System.out.println(pathFile);
//			if (pathFile.endsWith(".parquet")) {
//				sparkSession.read().parquet(pathFile).createOrReplaceTempView("tabla");
//				String query = "SELECT count(*) FROM " + tabla;
//
//				RDD<Row> d = sparkSession.sql(query).rdd();
//				System.out.println(d.count());
//			}
//		}
//
//		Dataset<Row> df1 = sparkSession
//				  .read()
//				  .format("parquet")
//				  .option("header", false)
//				  .option("inferSchema", true)
//				  .load(path);
//		df1.show();
//				  
//		Dataset<Row> df2 = sparkSession.read().parquet(path);
//		df2.show();
//		
//		String query = "SELECT count(*) FROM " + tabla;
//		sparkSession.read().parquet(path).createOrReplaceTempView(tabla);
//
//		RDD<Row> d = sparkSession.sql(query).rdd();
//		System.out.println(d.count());
//
//		Dataset<Row> parquet = sqlContext.read().parquet(path);
//		parquet.show(20);

//	}
	
	
	public String toJson(ParametrizacionIngesta par) {
		StringBuilder s = new StringBuilder();
		s.append("{  \"workflowId\": \"" + par.getId_flujo() + "\",");
		s.append("\"executionContext\": {\"extraParams\": [ ");
		s.append("{\"name\": \"id_ingesta\",\"value\": \""+ par.getId() + "\"}");
		
		
		if (par.getConsulta_destino() != null && !par.getConsulta_destino().endsWith(">")) {
			s.append(",{\"name\": \"statement\",\"value\": \"( "+ par.getConsulta_destino() + ") as " + par.getNombre_tabla() + "\"}");
		}


		s.append("]},");
		s.append("\"projectId\": \"string\"}");

		return s.toString();
	}

}
