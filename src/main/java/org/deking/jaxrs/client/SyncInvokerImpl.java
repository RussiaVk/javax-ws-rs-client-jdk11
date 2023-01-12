package org.deking.jaxrs.client;

import java.io.IOException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class SyncInvokerImpl implements SyncInvoker {
	private HttpClientWrapper hc;

 

	public SyncInvokerImpl(HttpClientWrapper hc) {
		this.hc = hc;
	}

	@Override
	public Response delete() {
		return method(HttpMethod.DELETE);
	}

	@Override
	public <T> T delete(Class<T> cls) {
		return method(HttpMethod.DELETE, cls);
	}

	@Override
	public <T> T delete(GenericType<T> genericType) {
		return method(HttpMethod.DELETE, genericType);
	}

	@Override
	public Response get() {
		return method(HttpMethod.GET);
	}

	@Override
	public <T> T get(Class<T> cls) {
		return method(HttpMethod.GET, cls);
	}

	@Override
	public <T> T get(GenericType<T> genericType) {
		return method(HttpMethod.GET, genericType);
	}

	@Override
	public Response head() {
		return method(HttpMethod.HEAD);
	}

	@Override
	public Response options() {
		return method(HttpMethod.OPTIONS);
	}

	@Override
	public <T> T options(Class<T> cls) {
		return method(HttpMethod.OPTIONS, cls);
	}

	@Override
	public <T> T options(GenericType<T> genericType) {
		return method(HttpMethod.OPTIONS, genericType);
	}

	@Override
	public Response post(Entity<?> entity) {
		return method(HttpMethod.POST, entity);
	}

	@Override
	public <T> T post(Entity<?> entity, Class<T> cls) {
		return method(HttpMethod.POST, entity, cls);
	}

	@Override
	public <T> T post(Entity<?> entity, GenericType<T> genericType) {
		return method(HttpMethod.POST, entity, genericType);
	}

	@Override
	public Response put(Entity<?> entity) {
		return method(HttpMethod.PUT, entity);
	}

	@Override
	public <T> T put(Entity<?> entity, Class<T> cls) {
		return method(HttpMethod.PUT, entity, cls);
	}

	@Override
	public <T> T put(Entity<?> entity, GenericType<T> genericType) {
		return method(HttpMethod.PUT, entity, genericType);
	}

	@Override
	public Response trace() {
		return method("TRACE");
	}

	@Override
	public <T> T trace(Class<T> cls) {
		return method("TRACE", cls);
	}

	@Override
	public <T> T trace(GenericType<T> genericType) {
		return method("TRACE", genericType);
	}

	@Override
	public Response method(String method) {
		return method(method, Response.class);
	}

	@Override
	public <T> T method(String method, Class<T> cls) {
		try {
			return hc.send(method, null, cls);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> T method(String method, GenericType<T> genericType) {
		try {
			return hc.invoke(method, null, genericType);
		} catch (IOException | InterruptedException e) {
			return null;
		}
	}

	@Override
	public Response method(String method, Entity<?> entity) {
		return method(method, entity, Response.class);
	}

	@Override
	public <T> T method(String method, Entity<?> entity, Class<T> cls) {
		try {
			return hc.send(method, entity.getEntity(), cls);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> T method(String method, Entity<?> entity, GenericType<T> genericType) {
		try {
			return hc.invoke(method, entity, genericType);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
