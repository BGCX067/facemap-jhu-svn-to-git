package edu.jhu.cs.oose.fall2011.facemap.client.v3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ServerInvoker
{
	public byte[] postToServer(String url, byte[] bytesToSend) throws ClientProtocolException, IOException 
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
	    
		HttpEntity httpEntity = new ByteArrayEntity(bytesToSend);
		httpPost.setEntity(httpEntity);
		
	    HttpResponse httpResponse = httpClient.execute(httpPost);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    httpResponse.getEntity().writeTo(baos);

	    
	    if (httpResponse.getStatusLine().getStatusCode() / 100 != 2) {
	    	throw new RuntimeException("httpResponse not ok:" + httpResponse);	
	    }
	    
	    return baos.toByteArray();
	}
}
