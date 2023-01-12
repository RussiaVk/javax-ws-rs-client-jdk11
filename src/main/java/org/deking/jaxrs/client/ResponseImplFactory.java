package org.deking.jaxrs.client;

import java.net.http.HttpResponse;

public class ResponseImplFactory {
	public static ResponseImpl create(HttpResponse resp){
//		ResponseImpl r=new ResponseImpl(resp); 
		ResponseImpl r=new ResponseImpl(resp.statusCode(),resp.body());
		return r;
	}
}
