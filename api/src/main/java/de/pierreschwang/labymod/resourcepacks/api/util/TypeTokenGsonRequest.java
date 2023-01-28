package de.pierreschwang.labymod.resourcepacks.api.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.labymod.api.util.io.web.WebInputStream;
import net.labymod.api.util.io.web.request.AbstractRequest;

public class TypeTokenGsonRequest<T> extends AbstractRequest<T, TypeTokenGsonRequest<T>> {

    private static final Gson GSON = new Gson();
    private final TypeToken<T> typeToken;

    public TypeTokenGsonRequest(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    @Override
    protected TypeTokenGsonRequest<T> prepareCopy() {
        return new TypeTokenGsonRequest<>(typeToken);
    }

    @Override
    protected T handle(WebInputStream inputStream) throws Exception {
        return GSON.fromJson(this.readString(inputStream), this.typeToken.getType());
    }
}
