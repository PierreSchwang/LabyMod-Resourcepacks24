package de.pierreschwang.labymod.resourcepacks.api.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.pierreschwang.labymod.resourcepacks.api.definition.AbstractResourcepacks24;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

public class Resourcepacks24OkHttpImpl extends AbstractResourcepacks24 {

  private static final String BASE_URL = "https://resourcepacks24.de/api/";

  private final ExecutorService executorPool;
  private final Gson gson;
  private final OkHttpClient client;

  public Resourcepacks24OkHttpImpl(String token) {
    super(token);
    this.executorPool = Executors.newCachedThreadPool();
    this.gson = new Gson();
    this.client = new OkHttpClient();
  }

  @Override
  public @NotNull CompletableFuture<InputStream> download(int resourcePackId) {
    return downloadUrl(resourcePackId).thenApply(downloadLink -> {
      Request request = new Builder()
          .url(downloadLink)
          .header("RP24-Token", token)
          .method("GET", null)
          .build();
      try(Response response = this.client.newCall(request).execute()) {
        ResponseBody body = response.body();
        if (response.code() != 200 || body == null) {
          throw new RuntimeException("Expected Status Code 200 and body");
        }
        return body.byteStream();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  protected @NotNull <T> CompletableFuture<T> query(@NotNull String route,
      @NotNull TypeToken<T> responseType, @NotNull IResponseTransformer<T> errorTransformer) {
    Request request = new Builder()
        .url(BASE_URL + route)
        .header("RP24-Token", token)
        .method("GET", null)
        .build();
    return CompletableFuture.supplyAsync(() -> {
      try (Response response = this.client.newCall(request).execute()) {
        ResponseBody body = response.body();
        if (response.code() != 200 || body == null) {
          return errorTransformer.transform(response.code(), body != null ? body.string() : null);
        }
        return this.gson.fromJson(body.string(), responseType.getType());
      } catch (IOException e) {
        e.printStackTrace();
      }
      return errorTransformer.transform(-1, null);
    }, executorPool);
  }


}
