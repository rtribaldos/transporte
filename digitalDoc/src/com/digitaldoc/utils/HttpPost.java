package com.digitaldoc.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.itextpdf.text.pdf.codec.Base64;

public class HttpPost {

	static final int BUFFER_SIZE = 4096;
	static final String CHARSET = "UTF-8";
	
	public static void send(String uploadUrl, byte[] datos) throws IOException {
		MultipartUtility multipart = new MultipartUtility(uploadUrl, CHARSET);
		
		multipart.addHeaderField("User-Agent", "CodeJava");
		multipart.addHeaderField("Test-Header", "Header-Value");
		
		multipart.addFormField("empresa", "1234");
		multipart.addFilePart("fichero2", datos);
		
		List<String> response = multipart.finish();
		
		System.out.println("SERVER REPLIED:");
		
		for (String line : response) {
			System.out.println(line);
		}
	}
	
	
	/*  public static void send(String uploadUrl, byte[] datos) throws IOException {
		  String attachmentName = "file";
		    String attachmentFileName = "firmado.pdf";
		    String crlf = "\r\n";
		    String twoHyphens = "--";
		    String boundary =  "*****";

		    try{

		    URL url = new URL(uploadUrl);
		  
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    connection.setDoOutput(true);
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		    DataOutputStream request = new DataOutputStream(connection.getOutputStream()); 
		    request.writeBytes(twoHyphens + boundary + crlf);
		    request.writeBytes("Content-Disposition: form-data; name=\"" +
		        attachmentName + "\";filename=\"" + 
		        attachmentFileName + "\"" + crlf);
		    request.writeBytes(crlf);
		    request.write(datos);
		    request.writeBytes(crlf);
		    request.writeBytes(twoHyphens + boundary + 
		        twoHyphens + crlf);
		   
		    request.flush();
		    request.close();
		    
		    int status = connection.getResponseCode();
		    
		   
		    
		    }catch(Exception e){
		        e.printStackTrace();
		    }

	  }*/
	
	  
	  
	  private static String getPostParamString(Hashtable<String, String> params) {
		    if(params.size() == 0)
		        return "";

		    StringBuffer buf = new StringBuffer();
		    Enumeration<String> keys = params.keys();
		    while(keys.hasMoreElements()) {
		        buf.append(buf.length() == 0 ? "" : "&");
		        String key = keys.nextElement();
		        buf.append(key).append("=").append(params.get(key));
		    }
		    return buf.toString();
		}
	  
	}