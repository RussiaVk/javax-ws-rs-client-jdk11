package org.deking.jaxrs.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow.Publisher;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.client.RxInvokerProvider;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public final class HttpClientWrapper {

	private HttpClient httpClient;
	private Builder builder;

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public Builder getBuilder() {
		return builder;
	}

	public void setBuilder(Builder builder) {
		this.builder = builder;
	}

	{
		httpClient = HttpClient.newHttpClient();
	}

	@SuppressWarnings({ "preview", "unchecked" })
	private static BodyPublisher bodyPublisher(Object body) throws FileNotFoundException {
		BodyPublisher bodyPublisher = switch (body) {
		case null -> BodyPublishers.ofString("");
		case String s -> BodyPublishers.ofString(s);
		case byte[] b -> BodyPublishers.ofByteArray(b);
		case Path p -> BodyPublishers.ofFile(p);
		case Supplier<?> su -> BodyPublishers.ofInputStream((Supplier<? extends InputStream>) su);
		default -> throw new IllegalArgumentException("Unexpected value: " + body);
		};
		return bodyPublisher;
	}

	@SuppressWarnings({ "unchecked", "preview" })
	private static <T> BodyHandler<T> bodyHandler(Class<T> responseClass) throws FileNotFoundException {

		BodyHandler<T> responseBodyHandler = (BodyHandler<T>) switch (responseClass) {
		case Class<?> cla && cla == String.class -> HttpResponse.BodyHandlers.ofString(Charset.defaultCharset());
		case Class<?> cla && cla == byte[].class -> HttpResponse.BodyHandlers.ofByteArray();
		case Class<?> cla && cla == InputStream.class -> HttpResponse.BodyHandlers.ofInputStream();
		case Class<?> cla && cla == Stream.class -> HttpResponse.BodyHandlers.ofLines();
		case Class<?> cla && cla == Publisher.class -> HttpResponse.BodyHandlers.ofPublisher();
//		case Class<?> cla&&cla==Optional.class&& clazz2==byte[].class->HttpResponse.BodyHandlers.ofByteArrayConsumer(null);
//		case Class<?> cla&&cla==Path.class->HttpResponse.BodyHandlers.ofFile(Path.of(""));
		default -> throw new IllegalArgumentException("Unexpected value: " + responseClass);
		};
		return responseBodyHandler;
	}

	@SuppressWarnings("unchecked")

	protected <R> R send(String httpMethod, Object body, Class<R> responseClass)
			throws IOException, InterruptedException {

		BodyPublisher bodyPublisher = bodyPublisher(body);
		if (responseClass == Response.class) {
			HttpResponse resp = httpClient.send(builder.method(httpMethod, bodyPublisher).build(),
					HttpResponse.BodyHandlers.ofString());
			return (R) ResponseImplFactory.create(resp);
		} else {
			BodyHandler<R> responseBodyHandler = (BodyHandler<R>) bodyHandler(responseClass);
			return (R) httpClient.send(builder.method(httpMethod, bodyPublisher).build(), responseBodyHandler).body();
		}

	}

	@SuppressWarnings("unchecked")
	protected <T, R> CompletableFuture<R> sendAsync(String httpMethod, T body, Class<R> responseClass)
			throws IOException, InterruptedException {
		BodyPublisher bodyPublisher = bodyPublisher(body);
		if (responseClass == Response.class) {
			CompletableFuture<HttpResponse<String>> resp = httpClient
					.sendAsync(builder.method(httpMethod, bodyPublisher).build(), HttpResponse.BodyHandlers.ofString());
			return (CompletableFuture<R>) resp.thenAccept(ResponseImplFactory::create);

		} else {
			BodyHandler<R> responseBodyHandler = (BodyHandler<R>) bodyHandler(responseClass);
			return (CompletableFuture<R>) httpClient
					.sendAsync(builder.method(httpMethod, bodyPublisher).build(), responseBodyHandler)
					.thenAccept(HttpResponse::body);
		}

	}

	@SuppressWarnings("unchecked")
	public <R> R invoke(String httpMethod, Object body, GenericType<R> responseType)
			throws IOException, InterruptedException {
		return (R) send(httpMethod, body, responseType.getRawType());

	}

	@SuppressWarnings("unchecked")
	public <R> CompletableFuture<R> invokeAsync(String httpMethod, Object body, GenericType<R> responseType)
			throws IOException, InterruptedException {
		return (CompletableFuture<R>) sendAsync(httpMethod, body, responseType.getRawType());

	}

	public <T> T method(String httpMethod, GenericType<T> genericType) throws IOException, InterruptedException {
		Class<?> clazz = genericType.getRawType();
		Class<?> clazz2 = genericType.getType().getClass();

		return invoke(httpMethod, null, genericType);

	}

	// Link to JAX-RS 2.0 AsyncInvoker
	public AsyncInvoker async() {
		return new AsyncInvokerImpl(this);
	}

	// Link to JAX-RS 2.0 SyncInvoker
	public SyncInvoker sync() {
		return new SyncInvokerImpl(this);
	}

	// Link to JAX-RS 2.1 CompletionStageRxInvoker
	public CompletionStageRxInvoker rx() {
		return rx(lookUpExecutorService());
	}

	public CompletionStageRxInvoker rx(ExecutorService ex) {
		return new CompletionStageRxInvokerImpl(this, ex);
	}

	// Link to JAX-RS 2.1 RxInvoker extensions
	public <T extends RxInvoker> T rx(Class<T> rxCls) {
		return rx(rxCls, (ExecutorService) null);
	}

	@SuppressWarnings("unchecked")
	public <T extends RxInvoker> T rx(Class<T> rxCls, ExecutorService executorService) {
		if (CompletionStageRxInvoker.class.isAssignableFrom(rxCls)) {
			return (T) rx(executorService);
		}
//		ClientProviderFactory pf = ClientProviderFactory.getInstance(WebClient.getConfig(this).getEndpoint());
//		RxInvokerProvider rxProvider = pf.getRxInvokerProvider();
		RxInvokerProvider rxProvider = null;
		if (rxProvider != null && rxProvider.isProviderFor(rxCls)) {
			return (T) rxProvider.getRxInvoker(sync(), executorService);
		}
		throw new IllegalStateException("Provider for " + rxCls.getName() + " is not available");
	}

	private ExecutorService lookUpExecutorService() {
		try {
			javax.naming.InitialContext ic = new javax.naming.InitialContext();
			Object execService = ic.lookup("java:comp/DefaultManagedExecutorService");
			if (execService != null) {
				return (ExecutorService) execService;
			}
		} catch (Throwable ex) {
			// ignore
		}
		return null;
	}

}
