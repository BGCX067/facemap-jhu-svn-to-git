package edu.jhu.cs.oose.fall2011.facemap.client.webservice;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;


/**
 * Hack to allow things to move quickly without learning a xml or json api.
 * Encapsulates a method invocation on a server EJB - sent as a stream of 
 * bytes from the server. Serialized to bytes using standard java 
 * serialization.
 * 
 * @author Daniel Cranford
 *
 */
public class Command implements Serializable {
	private String method;
	private Object[] args;

	public Command(String method, Object... args) {
		this.method = method;
		this.args = args;
	}
	
	public String getMethod() {return method;}
	public Object[] getArgs() {return args;}
	
	public Class[] getArgTypes() {
		Class[] argTypes = new Class[args.length];
		for(int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		return argTypes;
	}
	
	public byte[] toBytes() throws IOException {
		return Serializer.toBytes(this);
	}
	
	public static Command fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		return (Command)Serializer.fromBytes(bytes);
	}
	
	public String toString()
	{
		return method + "(" + Arrays.toString(args) + ")";
	}
}
