package org.deking.jaxrs.client;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;

public final class InvocationBuilderImpl implements Invocation.Builder {
	private static final String PROPERTY_KEY = "jaxrs.filter.properties";

	private HttpClientWrapper httpClient;
	private SyncInvoker sync;
	private Configuration config;

	public InvocationBuilderImpl(HttpClientWrapper hc, HttpRequest.Builder builder, Configuration config) {
		this.httpClient = hc;
		this.httpClient.setBuilder(builder);
		this.sync = httpClient.sync();
		this.config = config;
	}

	public HttpClientWrapper getHttpClientWrapper() {
		return httpClient;
	}

	@Override
	public Response delete() {
		return sync.delete();
	}

	@Override
	public <T> T delete(Class<T> cls) {
		return sync.delete(cls);
	}

	@Override
	public <T> T delete(GenericType<T> type) {
		return sync.delete(type);
	}

	@Override
	public Response get() {
		return sync.get();
	}

	@Override
	public <T> T get(Class<T> cls) {
		return sync.get(cls);
	}

	@Override
	public <T> T get(GenericType<T> type) {
		return sync.get(type);
	}

	@Override
	public Response head() {
		return sync.head();
	}

	@Override
	public Response method(String method) {
		return sync.method(method);
	}

	@Override
	public <T> T method(String method, Class<T> cls) {
		return sync.method(method, cls);
	}

	@Override
	public <T> T method(String method, GenericType<T> type) {
		return sync.method(method, type);
	}

	@Override
	public Response method(String method, Entity<?> entity) {
		return sync.method(method, entity);
	}

	@Override
	public <T> T method(String method, Entity<?> entity, Class<T> cls) {
		return sync.method(method, entity, cls);
	}

	@Override
	public <T> T method(String method, Entity<?> entity, GenericType<T> type) {
		return sync.method(method, entity, type);
	}

	@Override
	public Response options() {
		return sync.options();
	}

	@Override
	public <T> T options(Class<T> cls) {
		return sync.options(cls);
	}

	@Override
	public <T> T options(GenericType<T> type) {
		return sync.options(type);
	}

	@Override
	public Response post(Entity<?> entity) {
		return sync.post(entity);
	}

	@Override
	public <T> T post(Entity<?> entity, Class<T> cls) {
		return sync.post(entity, cls);
	}

	@Override
	public <T> T post(Entity<?> entity, GenericType<T> type) {
		return sync.post(entity, type);
	}

	@Override
	public Response put(Entity<?> entity) {
		return sync.put(entity);
	}

	@Override
	public <T> T put(Entity<?> entity, Class<T> cls) {
		return sync.put(entity, cls);
	}

	@Override
	public <T> T put(Entity<?> entity, GenericType<T> type) {
		return sync.put(entity, type);
	}

	@Override
	public Response trace() {
		return sync.trace();
	}

	@Override
	public <T> T trace(Class<T> cls) {
		return sync.trace(cls);
	}

	@Override
	public <T> T trace(GenericType<T> type) {
		return sync.trace(type);
	}

	@Override
	public Builder accept(String... types) {
		for (String mt : types) {
			httpClient.getBuilder().header(HttpHeaders.ACCEPT, mt);
		}
		return this;
	}

	@Override
	public Builder accept(MediaType... types) {
		for (MediaType mt : types) {
			httpClient.getBuilder().header(HttpHeaders.ACCEPT, JAXRSUtils.mediaTypeToString(mt));
		}
		return this;
	}

	@Override
	public Builder acceptEncoding(String... enc) {

		for (String s : enc) {
			httpClient.getBuilder().header(HttpHeaders.ACCEPT_ENCODING, s);
		}
		return this;
	}

	@Override
	public Builder acceptLanguage(Locale... lang) {
		for (Locale l : lang) {
			httpClient.getBuilder().header(HttpHeaders.ACCEPT_LANGUAGE, HttpUtils.toHttpLanguage(l));
		}
		return this;
	}

	@Override
	public Builder acceptLanguage(String... lang) {
		for (String s : lang) {
			httpClient.getBuilder().header(HttpHeaders.ACCEPT_LANGUAGE, s);
		}
		return this;
	}

	@Override
	public Builder cacheControl(CacheControl control) {
		httpClient.getBuilder().header(HttpHeaders.CACHE_CONTROL, control.toString());
		return this;
	}

	@Override
	public Builder cookie(Cookie cookie) {
		httpClient.getBuilder().header(HttpHeaders.COOKIE, cookie.toString());
		return this;
	}

	@Override
	public Builder cookie(String name, String value) {
		httpClient.getBuilder().header(HttpHeaders.COOKIE, name + "=" + value);
		return this;
	}

	@Override
	public Builder header(String name, Object value) {
		httpClient.getBuilder().header(name, value.toString());
		return this;
	}

	@Override
	public Builder headers(MultivaluedMap<String, Object> headers) {

		if (headers != null) {
			for (Map.Entry<String, List<Object>> entry : headers.entrySet()) {
				for (Object value : entry.getValue()) {
					httpClient.getBuilder().header(entry.getKey(), value.toString());
				}
			}
		}
		return this;
	}

	@Override
	public Builder property(String name, Object value) {
		Map<String, Object> contextProps = null;// HttpClientWrapper.getConfig(httpClient).getRequestContext();
		Map<String, Object> filterProps = CastUtils.cast((Map<?, ?>) contextProps.get(PROPERTY_KEY));
		if (filterProps == null) {
			filterProps = new HashMap<>();
			contextProps.put(PROPERTY_KEY, filterProps);
		}
		if (value == null) {
			filterProps.remove(name);
		} else {
			filterProps.put(name, value);
		}
		return this;
	}

	@Override
	public AsyncInvoker async() {
		return httpClient.async();
	}

	@Override
	public Invocation build(String method) {
		return new InvocationImpl(method);
	}

	@Override
	public Invocation build(String method, Entity<?> entity) {
		return new InvocationImpl(method, entity);
	}

	@Override
	public Invocation buildDelete() {
		return build(HttpMethod.DELETE);
	}

	@Override
	public Invocation buildGet() {
		return build(HttpMethod.GET);
	}

	@Override
	public Invocation buildPost(Entity<?> entity) {
		return build(HttpMethod.POST, entity);
	}

	@Override
	public Invocation buildPut(Entity<?> entity) {
		return build(HttpMethod.PUT, entity);
	}

	private class InvocationImpl implements Invocation {

		private Invocation.Builder invBuilder;
		private String httpMethod;
		private Entity<?> entity;

		InvocationImpl(String httpMethod) {
			this(httpMethod, null);
		}

		InvocationImpl(String httpMethod, Entity<?> entity) {
			this.invBuilder = InvocationBuilderImpl.this;
			this.httpMethod = httpMethod;
			this.entity = entity;
		}

		@Override
		public Response invoke() {
			return invBuilder.method(httpMethod, entity);
		}

		@Override
		public <T> T invoke(Class<T> cls) {
			return invBuilder.method(httpMethod, entity, cls);
		}

		@Override
		public <T> T invoke(GenericType<T> type) {
			return invBuilder.method(httpMethod, entity, type);
		}

		@Override
		public Invocation property(String name, Object value) {
			invBuilder.property(name, value);
			return this;
		}

		@Override
		public Future<Response> submit() {
			return invBuilder.async().method(httpMethod, entity);
		}

		@Override
		public <T> Future<T> submit(Class<T> cls) {
			return invBuilder.async().method(httpMethod, entity, cls);
		}

		@Override
		public <T> Future<T> submit(GenericType<T> type) {
			return invBuilder.async().method(httpMethod, entity, type);
		}

		@Override
		public <T> Future<T> submit(InvocationCallback<T> callback) {
			return invBuilder.async().method(httpMethod, entity, callback);
		}
	}

	@Override
	public CompletionStageRxInvoker rx() {
		return httpClient.rx(Executors.newFixedThreadPool(1));
	}

	@Override
	public <T extends RxInvoker> T rx(Class<T> rxCls) {
		return httpClient.rx(rxCls, Executors.newFixedThreadPool(1));
	}

}
