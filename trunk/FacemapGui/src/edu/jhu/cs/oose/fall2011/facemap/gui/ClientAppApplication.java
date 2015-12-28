package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;
import android.util.Log;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.v2.ClientAppImpl;
import edu.jhu.cs.oose.fall2011.facemap.client.v3.RestLoginService;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * Enables global access to clientApp, a person list, and a group list
 * 
 * @author Chuan Huang, Ying Dou
 */
public class ClientAppApplication extends Application
{

	/** The TAG. */
	private final String TAG = "ClientApplication";

	/** The client app. */
	ClientApp clientApp;

	// ClientApp testClient1, testClient2, testClient3;

	/** The account. */
	// private AccountImpl[] account = new AccountImpl[10];

	/** The private person. */
	// private PrivatePersonImpl[] privatePerson = new PrivatePersonImpl[10];

	/** The person. */
	// private PersonImpl[] person = new PersonImpl[10];

	private Set<Person> personList;
	private Set<FriendGroup> groupList;

	/**
	 * Instantiates a new client app application.
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public ClientAppApplication() throws IOException, ClientProtocolException
	{
		this.clientApp = new ClientAppImpl(new RestLoginService("10.0.2.2", "8080"));
		Log.d(TAG, "Finally Works.............. T_T");

		// random test
//		String query = "http://10.0.2.2:8080/FacemapServer3Web/rest/LoginService";
//		HttpClient httpclient = new DefaultHttpClient();
//		HttpPost httpPost = new HttpPost(query);
//
//		byte bytesToSend[] = "Random stuff to send to server".getBytes();
//
//		Log.d(TAG, "Random Bytes: " + new String(bytesToSend));
//		HttpEntity httpEntity = new ByteArrayEntity(bytesToSend);
//		httpPost.setEntity(httpEntity);
//
//		Log.d(TAG, "Posted to Server ");
//
//		HttpResponse httpResponse = httpclient.execute(httpPost);
//		Log.d(TAG, "Stuff Returned ");
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		httpResponse.getEntity().writeTo(baos);
//		byte[] output = baos.toByteArray();
//
//		Log.d(TAG, "ArrayOutput: " + Arrays.toString(output));
//		Log.d(TAG, "ArrayOutput: " + new String(output));

		/*
		 * String url = "http://10.0.2.2:8080/FacemapWeb/rest/LoginService"; Log.d(TAG, "url: " + url);
		 * DefaultHttpClient httpClient = new DefaultHttpClient();
		 * 
		 * HttpPost httpPost = new HttpPost(url);
		 * 
		 * Log.d(TAG, "Created httpClients "); byte bytesToSend[] =
		 * "Random stuff to send to server!!!!!!!!!!!!!!!!!!!!!!!!!!!!!".getBytes();
		 * 
		 * Log.d(TAG, "Random Bytes " + bytesToSend); HttpEntity httpEntity = new ByteArrayEntity(bytesToSend);
		 * httpPost.setEntity(httpEntity);
		 * 
		 * Log.d(TAG, "Posted to Server ");
		 * 
		 * HttpResponse httpResponse = httpClient.execute(httpPost); Log.d(TAG, "Stuff Returned ");
		 * 
		 * ByteArrayOutputStream baos = new ByteArrayOutputStream(); httpResponse.getEntity().writeTo(baos); byte[]
		 * output = baos.toByteArray();
		 * 
		 * Log.d(TAG, "ArrayOutput: " + Arrays.toString(output)); Log.d(TAG, "ArrayOutput: " + new String(output));
		 */
	}

	/**
	 * Gets the client app.
	 * 
	 * @return the client app
	 */
	public ClientApp getClientApp()
	{
		return clientApp;
	}

	public void setPersonList(Set<Person> newPersonList)
	{
		this.personList = newPersonList;
		for (Person p : personList)
		{
			Log.d(TAG, "Person: " + p.getName());
		}
	}

	public Set<Person> getPersonList()
	{
		return this.personList;
	}

	public void setGroupList(Set<FriendGroup> newGroupList)
	{
		this.groupList = newGroupList;

		for (FriendGroup g : groupList)
		{
			Log.d(TAG, "Person: " + g.getGroupName());
		}
	}

	public Set<FriendGroup> getGroupList()
	{
		return this.groupList;
	}

	// insert proxy codes
}