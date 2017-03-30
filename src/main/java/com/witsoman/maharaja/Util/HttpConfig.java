package com.witsoman.maharaja.Util;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class HttpConfig {

	String TAG = "AFB";

	public static String username = null;
	public static String password = null;
	
	public HttpConfig(String Username, String Password){
		username = Username;
		password = Password;
	}
	public HttpConfig(){
		//username = Username;
	}

	@SuppressLint("NewApi")
	public String httpget(String URL) {

		String responseString = null;

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		try {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 60 * 1000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 60 * 1000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpGet hg = new HttpGet(URL);
			HttpResponse response;

			if(username != null){
				//AuthScope as = new AuthScope(Webconfig.HOST);
				/*UsernamePasswordCredentials upc = new UsernamePasswordCredentials(
						username, password);

				((AbstractHttpClient) httpclient).getCredentialsProvider()
				.setCredentials(as, upc);
*/
				BasicHttpContext localContext = new BasicHttpContext();

				BasicScheme basicAuth = new BasicScheme();
				localContext.setAttribute("preemptive-auth", basicAuth);

				//HttpHost targetHost = new HttpHost(Webconfig.HOST, Webconfig.PORT, Webconfig.PROTOCOL);
				HttpHost targetHost = new HttpHost(Webconfig.HOST); // Webconfig.PROTOCOL);


				response = httpclient.execute(targetHost, hg,
						localContext);
			} else {
				response = httpclient.execute(hg);
			}

			
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				responseString = null;
				response.getEntity().getContent().close();
				// throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ConnectTimeoutException e) {
			responseString = null;
		} catch (Exception e) {
			// TODO: handle exception
			responseString = null;
			// e.printStackTrace();
		}
		return responseString;
	}
	
	
	@SuppressLint("NewApi")
	public String httppost(String URL) {

		String responseString = null;

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		HttpURLConnection connection = null;

		try {
			URL url = new URL(URL);
			try {
				connection = (HttpURLConnection) url.openConnection();
			} catch (Exception e) {
				//responseString = false;
			}

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(60 * 1000);

			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 60 * 1000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 60 * 1000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost hp = new HttpPost(URL);
			HttpResponse response;

			if(username != null){
				/*AuthScope as = new AuthScope(Webconfig.HOST, Webconfig.PORT);
				UsernamePasswordCredentials upc = new UsernamePasswordCredentials(
						username, password);

				((AbstractHttpClient) httpclient).getCredentialsProvider()
				.setCredentials(as, upc);
*/
				BasicHttpContext localContext = new BasicHttpContext();

				BasicScheme basicAuth = new BasicScheme();
				localContext.setAttribute("preemptive-auth", basicAuth);

				//HttpHost targetHost = new HttpHost(Webconfig.HOST, Webconfig.PORT, Webconfig.PROTOCOL);
				HttpHost targetHost = new HttpHost(Webconfig.HOST); // Webconfig.PROTOCOL);


				response = httpclient.execute(targetHost, hp,
						localContext);
			} else {
				response = httpclient.execute(hp);
			}

			
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				responseString = null;
				response.getEntity().getContent().close();
				// throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ConnectTimeoutException e) {
			responseString = null;
		} catch (Exception e) {
			// TODO: handle exception
			responseString = null;
			// e.printStackTrace();
		}
		return responseString; 
	}
	
	

	@SuppressLint("NewApi")
	@SuppressWarnings({ "unused" })
	public String doPost(String content, String urlValue) {

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		StringBuffer response = new StringBuffer();
		int status = 0;
		boolean responsecode = false;
		Log.v(TAG, "Content :: " + content);
		String postData = null;
		String urlString = urlValue;
		content = content.replace("&", "%26");
		try {

			postData = new String(content.getBytes(), "utf-8");
			Log.v(TAG, "After encrypted" + postData);
		} catch (Exception e) {

		}

		StringBuffer sb = new StringBuffer();
		HttpParams httpParameters = new BasicHttpParams();
		// int timeoutConnection = 10 * 1000;
		// HttpConnectionParams.setConnectionTimeout(httpParameters,
		// timeoutConnection);
		// int timeoutSocket = 10 * 1000;
		// HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlString);
			try {
				connection = (HttpURLConnection) url.openConnection();
			} catch (Exception e) {
				responsecode = false;
			}
			byte[] buff;

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(postData.getBytes().length));
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(70 * 1000);
			if(username != null){
				String userpassword = username + ":" + password;
				final String basicAuth = "Basic " + Base64.encodeToString(userpassword.getBytes(), Base64.NO_WRAP);
				connection.setRequestProperty ("Authorization", basicAuth);				
			}
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
					connection.getOutputStream(), "UTF-8"));
			wr.write(postData);
			wr.flush();
			wr.close();
			Log.v(TAG, "After posting************ ");
			Log.v(TAG, "Response Code" + connection.getResponseCode());
			// Get Response
			InputStream is;

			if (connection.getResponseCode() <= 400) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();

			}
			status = connection.getResponseCode();

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = rd.readLine()) != null) {
				responsecode = true;
				response.append(line);
				response.append('\r');
			}
			rd.close();
		} catch (SocketTimeoutException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			connection.disconnect();
		}
		return response.toString();

	}
	// public void makeHTTPPOSTRequest(String jsonDataStr) {
	// try {
	// HttpClient c = HttpClientBuilder.create().build();
	// HttpPost p = new HttpPost(this.apiURL);
	//
	// p.setEntity(new StringEntity(jsonDataStr, ContentType
	// .create("application/json")));
	//
	// HttpResponse r = c.execute(p);
	//
	// BufferedReader rd = new BufferedReader(new InputStreamReader(r
	// .getEntity().getContent()));
	// String line = "";
	// while ((line = rd.readLine()) != null) {
	// // Parse our JSON response
	// JSONParser j = new JSONParser();
	// JSONObject o;
	//
	// /*
	// * try { o = (JSONObject) j.parse(line);
	// * Constant.prtMsg(o.get("Success")); } catch
	// * (org.json.simple.parser.ParseException e) {
	// * e.printStackTrace(); }
	// */
	//
	// }
	// } catch (IOException e) {
	// Constant.prtMsg(e);
	// }
	// }

}
