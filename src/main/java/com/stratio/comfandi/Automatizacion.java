package com.stratio.comfandi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class Automatizacion {

	private final static Logger log = Logger.getLogger(Automatizacion.class.getName());

	@GetMapping("/start")
	ResponseEntity<String> getStart() {
		try {
			String user = getCookie(System.getenv().get("URL_COOKIE").toString(), System.getenv().get("USER_COOKIE").toString(), System.getenv().get("PW_COOKIE").toString());

			System.out.println(System.getenv().get("URL_COOKIE")+"-"+System.getenv().get("NAME_CERT")+"-"+System.getenv().get("CONTENT-TYPE")+"-"+System.getenv().get("ACCEPT")+"-"+user.replace("\n", "")
			+"-"+System.getenv().get("METHOD")+"-"+System.getenv().get("JSON-INPUT") );
			
			HttpsURLConnection urlConnection = getResTemplate(System.getenv().get("URL").toString(),
					System.getenv().get("NAME_CERT").toString());

			urlConnection.setRequestProperty("Content-Type",
					System.getenv().get("CONTENT-TYPE").toString() == null ? "application/json"
							: System.getenv().get("CONTENT-TYPE").toString());
			urlConnection.setRequestProperty("Accept",
					System.getenv().get("ACCEPT").toString() == null ? "application/json" : System.getenv().get("ACCEPT").toString());
			urlConnection.setRequestProperty("Cookie", user.replace("\n", ""));

			urlConnection
					.setRequestMethod(System.getenv().get("METHOD").toString() == null ? "POST" : System.getenv().get("METHOD").toString());
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);

			String json = System.getenv().get("JSON-INPUT").toString() == null
					? "{  \"workflowId\": \"7c100bc3-295a-4e58-a849-cb4977b11a4b\", \"executionContext\": {\"extraParams\": [ {\"name\": \"QUERY\",\"value\": \"(SELECT VBELN,AUART FROM VBAK limit 1) as vbak\"}]},\"projectId\": \"string\"}"
					: System.getenv().get("JSON-INPUT").toString();

			try (OutputStream os = urlConnection.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input, 0, input.length);
			} catch (IOException e) {
				e.getMessage();
			}

			StringBuilder sb = new StringBuilder();
			int HttpResult = urlConnection.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				System.out.println("" + sb.toString());
			} else {
				System.out.println(urlConnection.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().body("");
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

	public String getCookie(String urlCookies, String user, String pass) {
		URL url;
		try {
			url = new URL(urlCookies == null ? "http://10.2.32.68:10081/" : urlCookies);

			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection
					.setRequestMethod(System.getenv().get("METHOD_COOKIE").toString() == null ? "GET" : System.getenv().get("METHOD_COOKIE").toString());

			StringBuilder sb = new StringBuilder();
			int HttpResult = urlConnection.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				System.out.println("" + sb.toString());
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
}