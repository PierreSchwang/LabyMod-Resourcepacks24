package de.pierreschwang.labymod.resourcepacks.api.impl;

import com.google.gson.reflect.TypeToken;
import de.pierreschwang.labymod.resourcepacks.api.definition.AbstractResourcepacks24;
import de.pierreschwang.labymod.resourcepacks.api.util.TypeTokenGsonRequest;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Response;
import net.labymod.api.util.io.web.request.WebResolver;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class Resourcepack24NativeHttpImpl extends AbstractResourcepacks24 {

    private static final String BASE_URL = "https://resourcepacks24.de/api/";

    public Resourcepack24NativeHttpImpl(String token) {
        super(token);
    }

    @Override
    protected @NotNull <T> CompletableFuture<T> query(@NotNull String route, @NotNull TypeToken<T> responseType, @NotNull IResponseTransformer<T> errorTransformer) {
        return CompletableFuture.supplyAsync(() -> {
            Response<T> response = WebResolver.resolveSync(new TypeTokenGsonRequest<>(responseType)
                    .url(BASE_URL + route).addHeader("RP24-Token", this.token));
            return response.getOrDefault(errorTransformer.transform(response.getResponseCode(), null));
        });
    }

    @Override
    public @NotNull CompletableFuture<InputStream> download(int resourcePackId) {
        return downloadUrl(resourcePackId).thenApplyAsync(url -> WebResolver.resolveSync(
                Request.ofInputStream()
                        .url(url)
                        .addHeader("RP24-Token", this.token)
        ).get().getInputStream());
    }


}
