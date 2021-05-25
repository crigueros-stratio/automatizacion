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

import com.sun.org.apache.xerces.internal.impl.dv.dtd.NMTOKENDatatypeValidator;

public class ReadArchivoNew {

	static String rutatemplate = "/home/stratio7/kmilo/Stratio/COMFANDI/ProyectoComfandi/ingestas/templateIngestasnew.json";
	static String rutaInput = "/home/stratio7/kmilo/Stratio/COMFANDI/ProyectoComfandi/ingestas/templateIngestasnew2.json";
	static String rutaUnions = "/home/stratio7/kmilo/Stratio/COMFANDI/ProyectoComfandi/ingestas/templateIngestasnew3.json";
	static String rutaFiltros = "/home/stratio7/kmilo/Stratio/COMFANDI/ProyectoComfandi/ingestas/archivoFiltroDfkkko.txt";

	static String rutaSalida = "Environment.PATH_HDFS_LANDING_SAPCORE_PSCD";

	static String archivo = "DFKKKO";

	public static void main(String[] args) throws IOException {
		leerArchivo();
	}

	public static void leerArchivo() throws IOException {
		FileOutputStream fileOutput = new FileOutputStream(
				new File("/home/stratio7/kmilo/Stratio/COMFANDI/ProyectoComfandi/ingestas/" + archivo+ ".json"));
		BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);

		try {

			Scanner reader = new Scanner(new File(rutatemplate));
			while (reader.hasNextLine()) {
				String data = reader.nextLine().trim();
				data = data.replace("grupo", "0001");
				data = data.replace("contador", "000000001");
				data = data.replace("versionFlujo", "1");
				data = data.replace("nameGrupo", "01_"+archivo);
				data = data.replace("tabla", archivo);
				data = data.replace("rutaSalida", rutaSalida);




				if (data.equals("introducirInputs")) {
					int cont = 1;
					data = data.replace("introducirInputs", "");
					bufferedOutput.write(data.getBytes());

					Scanner readerFiltros = new Scanner(new File(rutaFiltros));
					while (readerFiltros.hasNextLine()) {
						cont = cont + 1;
						String filtro = readerFiltros.nextLine().trim();
						String condicion = filtro.toUpperCase().contains("AND") ? "between" : "";
						String select = "(SELECT XBLNR,OPBEL,BLART,BLDAT,CPUDT FROM DFKKKO where CPUDT " + condicion
								+ " " + filtro + " ) as DFKKKO";

						Scanner readerInput = new Scanner(new File(rutaInput));
						while (readerInput.hasNextLine()) {
							
							String input = readerInput.nextLine().trim();
							input = input.replace("selectTabla", select);
							input = input.replace("nameInput", archivo + cont);
							data = data.replace("descripcionTabla", select);

							bufferedOutput.write(input.getBytes());
						}
						bufferedOutput.write(",".getBytes());

					}
				}
				
				
				
				if (data.equals("uniones")) {
					int cont = 1;
					data = data.replace("uniones", "");
					bufferedOutput.write(data.getBytes());

					Scanner readerFiltros = new Scanner(new File(rutaFiltros));
					while (readerFiltros.hasNextLine()) {
						cont = cont + 1;
						String input = readerFiltros.nextLine().trim();
						
						Scanner readerUnion = new Scanner(new File(rutaUnions));
						while (readerUnion.hasNextLine()) {
							String union = readerUnion.nextLine().trim();
							union = union.replace("nameInput", archivo + cont);
							
							bufferedOutput.write(union.getBytes());
						}
					}
				}
				
				
				
				
				bufferedOutput.write(data.getBytes());
			}

			bufferedOutput.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}