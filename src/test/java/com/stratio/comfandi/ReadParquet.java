//package com.stratio.comfandi;
//
//import java.io.IOException;
//
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
//
//public class ReadParquet {
//
//
//	public static void main(String[] args) throws IOException {
//	    String tabla = "prueba";
//	    String path = System.getenv().get("PAH_PARQUET") == null ? "/home/stratio7/kmilo/Vanti/GOLIVE/ZDOCURTR/Parquet" : System.getenv().get("PAH_PARQUET") ;
//	    
//	    
//	    SparkConf configuracion = new SparkConf()
//	    	      .setAppName("prueba")
//	    	      .setMaster("local")
//	    	      .set("spark.driver.memory", "4g"); 
//
//		SparkSession sparkSession = SparkSession.builder().appName("prueba").master("local[*]").config(configuracion).getOrCreate();
//		SQLContext sqlContext = sparkSession.sqlContext();
//		
//		
//	    Configuration conf = new Configuration();
//		FileSystem fs = FileSystem.get(conf);
//		RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(new Path(path), true);
//		
//		FSDataInputStream f = fs.open(new Path(path));
//		
//		
//		//para que funcione hay que compilar con java 8
//		Dataset<Row> df = sparkSession.read().parquet(path);
//        String query = "SELECT count(*) FROM "+ tabla ;
//        sparkSession.read().parquet(path).createOrReplaceTempView(tabla);
//
//		Dataset<Row> d = sparkSession.sql(query);
//		d.show();
//
//        Dataset<Row> parquet = sqlContext.read().parquet(path);
//        parquet.show(20);
//	}
//
//}