package org.deking.jaxrs.client;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public final class AsyncInvokerImpl implements AsyncInvoker {

	private HttpClientWrapper hc;

	public AsyncInvokerImpl(HttpClientWrapper wc) {
		this.hc = wc;
	}

	@Override
	public Future<Response> get() {
		return get(Response.class);
	}

	@Override
	public <T> Future<T> get(Class<T> responseType) {
		return method(HttpMethod.GET, responseType);
	}

	@Override
	public <T> Future<T> get(GenericType<T> responseType) {
		return method(HttpMethod.GET, responseType);
	}

	@Override
	public <T> Future<T> get(InvocationCallback<T> callback) {
		return method(HttpMethod.GET, callback);
	}

	@Override
	public Future<Response> put(Entity<?> entity) {
		return put(entity, Response.class);
	}

	@Override
	public <T> Future<T> put(Entity<?> entity, Class<T> responseType) {
		return method(HttpMethod.PUT, entity, responseType);
	}

	@Override
	public <T> Future<T> put(Entity<?> entity, GenericType<T> responseType) {
		return method(HttpMethod.PUT, entity, responseType);
	}

	@Override
	public <T> Future<T> put(Entity<?> entity, InvocationCallback<T> callback) {
		return method(HttpMethod.PUT, entity, callback);
	}

	@Override
	public Future<Response> post(Entity<?> entity) {
		return post(entity, Response.class);
	}

	@Override
	public <T> Future<T> post(Entity<?> entity, Class<T> responseType) {
		return method(HttpMethod.POST, entity, responseType);
	}

	@Override
	public <T> Future<T> post(Entity<?> entity, GenericType<T> responseType) {
		return method(HttpMethod.POST, entity, responseType);
	}

	@Override
	public <T> Future<T> post(Entity<?> entity, InvocationCallback<T> callback) {
		return method(HttpMethod.POST, entity, callback);
	}

	@Override
	public Future<Response> delete() {
		return delete(Response.class);
	}

	@Override
	public <T> Future<T> delete(Class<T> responseType) {
		return method(HttpMethod.DELETE, responseType);
	}

	@Override
	public <T> Future<T> delete(GenericType<T> responseType) {
		return method(HttpMethod.DELETE, responseType);
	}

	@Override
	public <T> Future<T> delete(InvocationCallback<T> callback) {
		return method(HttpMethod.DELETE, callback);
	}

	@Override
	public Future<Response> head() {
		return method(HttpMethod.HEAD);
	}

	@Override
	public Future<Response> head(InvocationCallback<Response> callback) {
		return method(HttpMethod.HEAD, callback);
	}

	@Override
	public Future<Response> options() {
		return options(Response.class);
	}

	@Override
	public <T> Future<T> options(Class<T> responseType) {
		return method(HttpMethod.OPTIONS, responseType);
	}

	@Override
	public <T> Future<T> options(GenericType<T> responseType) {
		return method(HttpMethod.OPTIONS, responseType);
	}

	@Override
	public <T> Future<T> options(InvocationCallback<T> callback) {
		return method(HttpMethod.OPTIONS, callback);
	}

	@Override
	public Future<Response> trace() {
		return trace(Response.class);
	}

	@Override
	public <T> Future<T> trace(Class<T> responseType) {
		return method("TRACE", responseType);
	}

	@Override
	public <T> Future<T> trace(GenericType<T> responseType) {
		return method("TRACE", responseType);
	}

	@Override
	public <T> Future<T> trace(InvocationCallback<T> callback) {
		return method("TRACE", callback);
	}

	@Override
	public Future<Response> method(String name) {
		return method(name, Response.class);
	}

	@Override
	public <T> Future<T> method(String name, Class<T> responseType) {
		try {
			return hc.sendAsync(name, null, responseType);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> Future<T> method(String name, GenericType<T> responseType) {
		try {
			return hc.invokeAsync(name, null, responseType);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> Future<T> method(String name, InvocationCallback<T> callback) {
		return this.method(name, null, callback);
	}

	@Override
	public Future<Response> method(String name, Entity<?> entity) {
		try {
			return hc.sendAsync(name, entity.getEntity(), Response.class);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> Future<T> method(String name, Entity<?> entity, Class<T> responseType) {
		try {
			return (Future<T>) hc.sendAsync(name, entity.getEntity(), responseType);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> Future<T> method(String name, Entity<?> entity, GenericType<T> responseType) {
		try {
			return hc.invokeAsync(name, entity.getEntity(), responseType);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Future<T> method(String name, Entity<?> entity, InvocationCallback<T> callback) {
		Class<T> clazz = (Class<T>) ((ParameterizedType) callback.getClass().getGenericInterfaces()[0])
				.getActualTypeArguments()[0];
		try {
			return hc.sendAsync(name, entity.getEntity(), clazz)
					.whenComplete((BiConsumer<? super T, ? super Throwable>) callback);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

}