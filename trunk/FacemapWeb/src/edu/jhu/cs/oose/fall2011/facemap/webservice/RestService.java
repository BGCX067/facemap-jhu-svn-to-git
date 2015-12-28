package edu.jhu.cs.oose.fall2011.facemap.webservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

@Path("/test")
public class RestService {

	@GET
	@Produces("text/plain")
	public String getText() {
		return "Hello world";
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("text/plain")
	public String getMessage(@PathParam("id") long id) {
		return "Hello id: " + id;
	}
	
	@GET
	@Path("/image.png")
	@Produces("image/png")
	public StreamingOutput getPicture() {
		return new StreamingOutput() {
			
			@Override
			public void write(OutputStream os) throws IOException,
					WebApplicationException {
				InputStream is =  RestService.class.getResourceAsStream("Screenshot.png");
				pipe(is, os);
				is.close();
			}
		};
	}
	
	@GET
	@Path("/image2.png")
	@Produces("image/png")
	public byte[] getPicture2() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = RestService.class.getResourceAsStream("Screenshot.png");
		pipe(is, baos);
		return baos.toByteArray();
	}
	
	private void pipe(InputStream is, OutputStream os) throws IOException {
		int b;
		while((b=is.read()) != -1) os.write(b);		
	}
	
	@POST
	@Path("/post")
	public void postBinary(byte [] input) {
		System.out.println(Arrays.toString(input));
	}
}
