package org.deking.jaxrs.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class CompletionStageRxInvokerImpl implements CompletionStageRxInvoker {
    private HttpClientWrapper wc;
    private ExecutorService ex;
    CompletionStageRxInvokerImpl(HttpClientWrapper wc, ExecutorService ex) {
        this.ex = ex;
        this.wc = wc;
    }

    @Override
    public CompletionStage<Response> get() {
        return get(Response.class);
    }

    @Override
    public <T> CompletionStage<T> get(Class<T> responseType) {
        return method(HttpMethod.GET, responseType);
    }

    @Override
    public <T> CompletionStage<T> get(GenericType<T> responseType) {
        return method(HttpMethod.GET, responseType);
    }

    @Override
    public CompletionStage<Response> put(Entity<?> entity) {
        return put(entity, Response.class);
    }

    @Override
    public <T> CompletionStage<T> put(Entity<?> entity, Class<T> responseType) {
        return method(HttpMethod.PUT, entity, responseType);
    }

    @Override
    public <T> CompletionStage<T> put(Entity<?> entity, GenericType<T> responseType) {
        return method(HttpMethod.PUT, entity, responseType);
    }

    @Override
    public CompletionStage<Response> post(Entity<?> entity) {
        return post(entity, Response.class);
    }

    @Override
    public <T> CompletionStage<T> post(Entity<?> entity, Class<T> responseType) {
        return method(HttpMethod.POST, entity, responseType);
    }

    @Override
    public <T> CompletionStage<T> post(Entity<?> entity, GenericType<T> responseType) {
        return method(HttpMethod.POST, entity, responseType);
    }

    @Override
    public CompletionStage<Response> delete() {
        return delete(Response.class);
    }

    @Override
    public <T> CompletionStage<T> delete(Class<T> responseType) {
        return method(HttpMethod.DELETE, responseType);
    }

    @Override
    public <T> CompletionStage<T> delete(GenericType<T> responseType) {
        return method(HttpMethod.DELETE, responseType);
    }

    @Override
    public CompletionStage<Response> head() {
        return method(HttpMethod.HEAD);
    }

    @Override
    public CompletionStage<Response> options() {
        return options(Response.class);
    }

    @Override
    public <T> CompletionStage<T> options(Class<T> responseType) {
        return method(HttpMethod.OPTIONS, responseType);
    }

    @Override
    public <T> CompletionStage<T> options(GenericType<T> responseType) {
        return method(HttpMethod.OPTIONS, responseType);
    }

    @Override
    public CompletionStage<Response> trace() {
        return trace(Response.class);
    }

    @Override
    public <T> CompletionStage<T> trace(Class<T> responseType) {
        return method("TRACE", responseType);
    }

    @Override
    public <T> CompletionStage<T> trace(GenericType<T> responseType) {
        return method("TRACE", responseType);
    }

    @Override
    public CompletionStage<Response> method(String name) {
        return method(name, Response.class);
    }

    @Override
    public CompletionStage<Response> method(String name, Entity<?> entity) {
        return method(name, entity, Response.class);
    }

    @Override
    public <T> CompletionStage<T> method(String name, Entity<?> entity, Class<T> responseType) {
        if (ex == null) {
            return CompletableFuture.supplyAsync(() -> wc.sync().method(name, entity, responseType));
        }
        return CompletableFuture.supplyAsync(() -> wc.sync().method(name, entity, responseType), ex);
    }

    @Override
    public <T> CompletionStage<T> method(String name, Entity<?> entity, GenericType<T> responseType) {
        if (ex == null) {
            return CompletableFuture.supplyAsync(() -> wc.sync().method(name, entity, responseType));
        }
        return CompletableFuture.supplyAsync(() -> wc.sync().method(name, entity, responseType), ex);
    }

    @Override
    public <T> CompletionStage<T> method(String name, Class<T> responseType) {
        if (ex == null) {
            return CompletableFuture.supplyAsync(() -> wc.sync().method(name, responseType));
        }
        return CompletableFuture.supplyAsync(() -> wc.sync().method(name, responseType), ex);
    }

    @Override
    public <T> CompletionStage<T> method(String name, GenericType<T> responseType) {
        if (ex == null) {
            return CompletableFuture.supplyAsync(() -> wc.sync().method(name, responseType));
        }
        return CompletableFuture.supplyAsync(() -> wc.sync().method(name, responseType), ex);
    }

}
