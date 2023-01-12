package org.deking.jaxrs.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configurable;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class ClientImpl implements Client {

	private Configurable<Client> configImpl;
	private boolean closed;
	private Set<HttpClientWrapper> baseClients = Collections
			.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<HttpClientWrapper, Boolean>()));

	public ClientImpl() {
	}

	public ClientImpl(Configuration config) {
//		configImpl = new ClientConfigurableImpl<>(this, config);
	}

	@Override
	public void close() {
		if (!closed) {
			synchronized (baseClients) {
				baseClients.clear();
			}
			baseClients = null;
			closed = true;
		}

	}

	@Override
	public Builder invocation(Link link) {
		checkNull(link);
		checkClosed();
		Builder builder = target(link.getUriBuilder()).request();
		String type = link.getType();
		if (type != null) {
			builder.accept(type);
		}
		return builder;
	}

	@Override
	public WebTarget target(UriBuilder builder) {
		checkNull(builder);
		checkClosed(); 
		return new WebTargetImpl(builder, null);
	}

	@Override
	public WebTarget target(String address) {
		checkNull(address);
		if (address.isEmpty()) {
			address = "/";
		}
		return target(UriBuilder.fromUri(address));
	}

	@Override
	public WebTarget target(Link link) {
		checkNull(link);
		return target(link.getUriBuilder());
	}

	@Override
	public WebTarget target(URI uri) {
		checkNull(uri);
		return target(UriBuilder.fromUri(uri));
	}

	private void checkNull(Object... target) {
		for (Object o : target) {
			if (o == null) {
				throw new NullPointerException("Value is null");
			}
		}
	}

	@Override
	public HostnameVerifier getHostnameVerifier() {
		return null;
	}

	@Override
	public SSLContext getSslContext() {
		checkClosed();
		try {
			return SSLContext.getDefault();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private void checkClosed() {
		if (closed) {
			throw new IllegalStateException("client is closed");
		}
	}

	@Override
	public Configuration getConfiguration() {
		checkClosed();
		return configImpl.getConfiguration();
	}

	@Override
	public Client property(String name, Object value) {
		checkClosed();
		return configImpl.property(name, value);
	}

	@Override
	public Client register(Class<?> cls) {
		checkClosed();
		return configImpl.register(cls);
	}

	@Override
	public Client register(Object object) {
		checkClosed();
		return configImpl.register(object);
	}

	@Override
	public Client register(Class<?> cls, int index) {
		checkClosed();
		return configImpl.register(cls, index);
	}

	@Override
	public Client register(Class<?> cls, Class<?>... contracts) {
		checkClosed();
		return configImpl.register(cls, contracts);
	}

	@Override
	public Client register(Class<?> cls, Map<Class<?>, Integer> map) {
		checkClosed();
		return configImpl.register(cls, map);
	}

	@Override
	public Client register(Object object, int index) {
		checkClosed();
		return configImpl.register(object, index);
	}

	@Override
	public Client register(Object object, Class<?>... contracts) {
		checkClosed();
		return configImpl.register(object, contracts);
	}

	@Override
	public Client register(Object object, Map<Class<?>, Integer> map) {
		checkClosed();
		return configImpl.register(object, map);
	}

	public class WebTargetImpl implements WebTarget {
		private Configurable<WebTarget> configImpl;
		private UriBuilder uriBuilder;
		private HttpClientWrapper targetClient;

		public WebTargetImpl(UriBuilder uriBuilder, Configuration config) {
			this(uriBuilder, config, null);
		}

		public WebTargetImpl(UriBuilder uriBuilder, Configuration config, HttpClientWrapper targetClient) {
			this.uriBuilder = uriBuilder;
			// this.configImpl = new ClientConfigurableImpl<>(this, config);
			this.targetClient = targetClient;
		}

		@Override
		public Builder request() {
			targetClient = new HttpClientWrapper();
			return new InvocationBuilderImpl(targetClient, HttpRequest.newBuilder().uri(getUri()), null);
		}

		public HttpClientWrapper getWebClient() {
			return this.targetClient;
		}

		@Override
		public Builder request(String... accept) {
			return request().accept(accept);
		}

		@Override
		public Builder request(MediaType... accept) {
			return request().accept(accept);
		}

		@Override
		public URI getUri() {
			checkClosed();
			return uriBuilder.build();
		}

		@Override
		public UriBuilder getUriBuilder() {
			checkClosed();
			return uriBuilder.clone();
		}

		@Override
		public WebTarget path(String path) {
			checkClosed();
			checkNull(path);
			return newWebTarget(getUriBuilder().path(path));
		}

		@Override
		public WebTarget queryParam(String name, Object... values) {
			checkClosed();
			checkNullValues(name, values);
			UriBuilder thebuilder = getUriBuilder();
			if (values == null || values.length == 1 && values[0] == null) {
				thebuilder.replaceQueryParam(name, (Object[]) null);
			} else {
				thebuilder.queryParam(name, values);
			}
			return newWebTarget(thebuilder);
		}

		@Override
		public WebTarget matrixParam(String name, Object... values) {
			checkClosed();
			checkNullValues(name, values);

			UriBuilder thebuilder = getUriBuilder();
			if (values == null || values.length == 1 && values[0] == null) {
				thebuilder.replaceMatrixParam(name, (Object[]) null);
			} else {
				thebuilder.matrixParam(name, values);
			}
			return newWebTarget(thebuilder);
		}

		@Override
		public WebTarget resolveTemplate(String name, Object value) {
			return resolveTemplate(name, value, true);
		}

		@Override
		public WebTarget resolveTemplate(String name, Object value, boolean encodeSlash) {
			checkClosed();
			checkNull(name, value);
			return newWebTarget(getUriBuilder().resolveTemplate(name, value, encodeSlash));
		}

		@Override
		public WebTarget resolveTemplateFromEncoded(String name, Object value) {
			checkNull(name, value);
			return newWebTarget(getUriBuilder().resolveTemplateFromEncoded(name, value));
		}

		@Override
		public WebTarget resolveTemplates(Map<String, Object> templatesMap) {
			return resolveTemplates(templatesMap, true);
		}

		@Override
		public WebTarget resolveTemplates(Map<String, Object> templatesMap, boolean encodeSlash) {
			checkClosed();
			checkNullMap(templatesMap);
			if (templatesMap.isEmpty()) {
				return this;
			}
			return newWebTarget(getUriBuilder().resolveTemplates(templatesMap, encodeSlash));
		}

		@Override
		public WebTarget resolveTemplatesFromEncoded(Map<String, Object> templatesMap) {
			checkClosed();
			checkNullMap(templatesMap);
			if (templatesMap.isEmpty()) {
				return this;
			}
			return newWebTarget(getUriBuilder().resolveTemplatesFromEncoded(templatesMap));
		}

		private WebTarget newWebTarget(UriBuilder newBuilder) {
			if (targetClient == null) {
				targetClient = new HttpClientWrapper();
			}
			return new WebTargetImpl(newBuilder, getConfiguration(), targetClient);
		}

		@Override
		public Configuration getConfiguration() {
			checkClosed();
			return configImpl.getConfiguration();
		}

		@Override
		public WebTarget property(String name, Object value) {
			checkClosed();
			return configImpl.property(name, value);
		}

		@Override
		public WebTarget register(Class<?> cls) {
			checkClosed();
			return configImpl.register(cls);
		}

		@Override
		public WebTarget register(Object object) {
			checkClosed();
			return configImpl.register(object);
		}

		@Override
		public WebTarget register(Class<?> cls, int index) {
			checkClosed();
			return configImpl.register(cls, index);
		}

		@Override
		public WebTarget register(Class<?> cls, Class<?>... contracts) {
			checkClosed();
			return configImpl.register(cls, contracts);
		}

		@Override
		public WebTarget register(Class<?> cls, Map<Class<?>, Integer> map) {
			checkClosed();
			return configImpl.register(cls, map);
		}

		@Override
		public WebTarget register(Object object, int index) {
			checkClosed();
			return configImpl.register(object, index);
		}

		@Override
		public WebTarget register(Object object, Class<?>... contracts) {
			checkClosed();
			return configImpl.register(object, contracts);
		}

		@Override
		public WebTarget register(Object object, Map<Class<?>, Integer> map) {
			checkClosed();
			return configImpl.register(object, map);
		}

		private void checkNullValues(Object name, Object... values) {
			checkNull(name);
			if (values != null && values.length > 1) {
				checkNull(values);
			}
		}

		private void checkNullMap(Map<String, Object> templatesMap) {
			checkNull(templatesMap);
			checkNull(templatesMap.keySet().toArray());
			checkNull(templatesMap.values().toArray());
		}

	}
}
