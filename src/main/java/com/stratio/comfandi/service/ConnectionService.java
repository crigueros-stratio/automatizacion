package com.stratio.comfandi.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.stereotype.Service;

@Service
public class ConnectionService {

	public HttpsURLConnection confParameters() {
		try {
			String user = getCookie(System.getenv().get("URL_COOKIE"));

			System.out.println(System.getenv().get("URL_COOKIE") + "-" + System.getenv().get("NAME_CERT") + "-"
					+ System.getenv().get("CONTENT-TYPE") + "-" + System.getenv().get("ACCEPT") + "-"
					+ user.replace("\n", "") + "-" + System.getenv().get("METHOD") + "-"
					+ System.getenv().get("JSON-INPUT"));

			HttpsURLConnection urlConnection = getResTemplate(System.getenv().get("URL"),
					System.getenv().get("NAME_CERT"));

			urlConnection.setRequestProperty("Content-Type",
					System.getenv().get("CONTENT-TYPE") == null ? "application/json"
							: System.getenv().get("CONTENT-TYPE"));
			urlConnection.setRequestProperty("Accept",
					System.getenv().get("ACCEPT") == null ? "application/json" : System.getenv().get("ACCEPT"));
			urlConnection.setRequestProperty("Cookie", user.replace("\n", ""));

			urlConnection
					.setRequestMethod(System.getenv().get("METHOD") == null ? "POST" : System.getenv().get("METHOD"));
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);

			return urlConnection;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public HttpsURLConnection getResTemplate(String urlRocket, String nameCert) {

		try {
			URL url = new URL(urlRocket == null
					? "https://rocket-comfandi.saaslatampd.stratio.com/comfandi-rocket/workflows/runWithExecutionContext"
					: urlRocket);
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream caInput = new BufferedInputStream(
					new FileInputStream(nameCert == null ? "ca-saas.crt" : nameCert));
			Certificate ca;

			try {
				ca = cf.generateCertificate(caInput);
				System.out.println(((X509Certificate) ca).getSubjectDN());
			} finally {
				caInput.close();
			}

			String keyStoreType = KeyStore.getDefaultType();
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);

			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keyStore);

			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, tmf.getTrustManagers(), null);

			urlConnection.setSSLSocketFactory(context.getSocketFactory());

			return urlConnection;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getCookie(String urlCookies) {
		URL url;
		try {
			url = new URL(urlCookies == null ? "http://10.2.32.68:10081/" : urlCookies);

			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod(
					System.getenv().get("METHOD_COOKIE") == null ? "GET" : System.getenv().get("METHOD_COOKIE"));

			StringBuilder sb = new StringBuilder();
			int HttpResult = urlConnection.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				System.out.println("" + sb.toString().replace("\n", ""));
			} else {
				System.out.println(urlConnection.getResponseMessage());
			}

			return sb.toString();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

//	public String consulta(String query) throws SQLException {
//
//		Connection con = DriverManager.getConnection(System.getenv().get("SPRING_DATASOURCE_URL") == null
//				? "jdbc:postgresql://comfandi-poolpostgresmd5.comfandi.marathon.mesos:20120/comfandidc?prepareThreshold=0"
//				: System.getenv().get("SPRING_DATASOURCE_URL"),
//				System.getenv().get("SPRING_DATASOURCE_USERNAME") == null ? "extpostgresuser" : System.getenv().get("SPRING_DATASOURCE_USERNAME"),
//				System.getenv().get("SPRING_DATASOURCE_PASSWORD") == null ? "QXpe64SdbEYeMQ5c"
//						: System.getenv().get("SPRING_DATASOURCE_PASSWORD"));
//		Statement st = con.createStatement();
//		ResultSet rs = st.executeQuery(query);
//
//		if (rs.next()) {
//			con.close();
//			return rs.getString(1);
//		} else
//			con.close();
//			return "";
//	}

}