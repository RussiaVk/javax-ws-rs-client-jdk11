package org.deking.jaxrs.client.test;

import java.util.concurrent.ExecutionException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class ClientTest {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Client client = ClientBuilder.newClient();
		Response resp = client.target("https://baidu.com").request("text/plain").get();

//		var resp = client.target("http://baidu.com").request("text/plain").async().get();

		System.out.println(resp.getEntity());
	}
}
