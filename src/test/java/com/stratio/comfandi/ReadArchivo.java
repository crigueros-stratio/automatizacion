package com.stratio.comfandi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReadArchivo {

	static String rutatemplate = "/home/stratio7/kmilo/Stratio/COMFANDI/ProyectoComfandi/ingestas/templateIngestas.json";
	static String identificador; // es siempre el numero del mes y se completa con 0 a la izquierda
	static String mes; // el mes con dos digitos
	static String version = ""; // de longitud de 1 version por mes
	static String findemes = ""; // el numero del mes con el que termina
	
	
	static String archivo = "VBAP";
	static String anio = "2021";
	static String orden = "22"; // tiene que ser de dos caracteres
	
	
	static String campofiltro = "ERDAT"; // el numero del mes con el que termina

	public static void main(String[] args) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "31");
		map.put("2", "28");
		map.put("3", "31");
		map.put("4", "30");
		map.put("5", "31");
		map.put("6", "30");
		map.put("7", "31");
		map.put("8", "31");
		map.put("9", "30");
		map.put("10", "31");
		map.put("11", "30");
		map.put("12", "31");

		try {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				version = entry.getKey();
				findemes = entry.getValue();
				leerArchivo();
			}
		} catch (

		FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void leerArchivo() throws IOException {
		identificador = String.format("%3s", version).replace(" ", "0");
		mes = String.format("%2s", version).replace(" ", "0");

		FileOutputStream fileOutput = new FileOutputStream(
				new File("/home/stratio7/kmilo/Stratio/COMFANDI/ProyectoComfandi/ingestas/" + archivo + "_" + anio + "_"
						+ mes + ".json"));
		BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);

		try {
			Scanner reader = new Scanner(new File(rutatemplate));
			while (reader.hasNextLine()) {
				String data = reader.nextLine().trim();
				data = data.replace("anio", anio);
				data = data.replace("grupo", anio);
				data = data.replace("contador", orden + anio + identificador);
				data = data.replace("nameInput", archivo + "_" + anio + "_" + mes + "_");
				data = data.replace("versionFlujo", version);
				data = data.replace("mesActual", "-" + mes + "-");
				data = data.replace("findemes", findemes);
				data = data.replace("nameGrupo", orden + "_" + archivo + "_EN_DIC_" + anio);
				data = data.replace("tabla", archivo);
				data = data.replace("filtro", campofiltro);

				bufferedOutput.write(data.getBytes());
			}
			bufferedOutput.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}