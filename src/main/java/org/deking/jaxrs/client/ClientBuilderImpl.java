package org.deking.jaxrs.client;

import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configuration;

public class ClientBuilderImpl extends ClientBuilder {

	@Override
	public Configuration getConfiguration() {
		return null;
	}

	@Override
	public ClientBuilder property(String name, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder register(Class<?> componentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder register(Class<?> componentClass, int priority) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder register(Class<?> componentClass, Class<?>... contracts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder register(Class<?> componentClass, Map<Class<?>, Integer> contracts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder register(Object component) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder register(Object component, int priority) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder register(Object component, Class<?>... contracts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder register(Object component, Map<Class<?>, Integer> contracts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder withConfig(Configuration config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder sslContext(SSLContext sslContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder keyStore(KeyStore keyStore, char[] password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder trustStore(KeyStore trustStore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder hostnameVerifier(HostnameVerifier verifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder executorService(ExecutorService executorService) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder scheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder connectTimeout(long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBuilder readTimeout(long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Client build() {
		return new ClientImpl();
	}

}
