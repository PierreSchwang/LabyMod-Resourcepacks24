package de.pierreschwang.labymod.resourcepacks.api.definition;

import com.google.gson.reflect.TypeToken;
import de.pierreschwang.labymod.resourcepacks.api.dao.result.PaginatedResult;
import de.pierreschwang.labymod.resourcepacks.api.dao.result.Resourcepack;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public abstract class AbstractResourcepacks24 implements IResourcepacks24 {

  private static final TypeToken<PaginatedResult<Resourcepack>> TYPE_PAGINATED_RESOURCEPACKS = new TypeToken<PaginatedResult<Resourcepack>>() {
  };
  private static final TypeToken<List<Resourcepack>> TYPE_RESOURCEPACKS = new TypeToken<List<Resourcepack>>() {
  };
  private static final TypeToken<Resourcepack> TYPE_RESOURCEPACK = TypeToken.get(
      Resourcepack.class);
  private static final TypeToken<String> TYPE_STRING = TypeToken.get(String.class);
  private static final TypeToken<List<String>> TYPE_STRINGS = new TypeToken<List<String>>() {
  };

  protected final String token;

  protected AbstractResourcepacks24(String token) {
    this.token = token;
  }

  protected abstract <T> @NotNull CompletableFuture<T> query(@NotNull String route,
      @NotNull TypeToken<T> responseType, @NotNull IResponseTransformer<T> errorTransformer);

  @Override
  public @NotNull CompletableFuture<@NotNull PaginatedResult<@NotNull Resourcepack>> byCategory(
      @NotNull String category, @Range(from = 0, to = Integer.MAX_VALUE) int page) {
    return query(
        String.format("category/%s?page=%s", category, page),
        TYPE_PAGINATED_RESOURCEPACKS,
        (statusCode, body) -> new PaginatedResult<>()
    );
  }

  @Override
  public @NotNull CompletableFuture<@NotNull List<@NotNull Resourcepack>> newest() {
    return query(
        "resourcepacks/new",
        TYPE_RESOURCEPACKS,
        (statusCode, body) -> Collections.emptyList()
    );
  }

  @Override
  public @NotNull CompletableFuture<@NotNull List<@NotNull Resourcepack>> trending(
      @Range(from = 0, to = 3) int page) {
    return query(
        "feed/trending?page=" + page,
        TYPE_RESOURCEPACKS,
        (statusCode, body) -> Collections.emptyList()
    );
  }

  @Override
  public @NotNull CompletableFuture<@NotNull List<@NotNull Resourcepack>> promoted() {
    return query(
        "feed/promotion",
        TYPE_RESOURCEPACKS,
        (statusCode, body) -> Collections.emptyList()
    );
  }

  @Override
  public @NotNull CompletableFuture<@NotNull List<@NotNull Resourcepack>> search(String keyword) {
    try {
      keyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException ignored) {
    }
    return query(
        "search/" + keyword,
        TYPE_RESOURCEPACKS,
        (statusCode, body) -> Collections.emptyList()
    );
  }

  @Override
  public @NotNull CompletableFuture<@NotNull List<@NotNull Resourcepack>> ofTheWeek(
      @Range(from = 0, to = 5) int page) {
    return query(
        "feed/resourcepack-of-the-week?page=" + page,
        TYPE_RESOURCEPACKS,
        (statusCode, body) -> Collections.emptyList()
    );
  }

  @Override
  public @NotNull CompletableFuture<@Nullable Resourcepack> byId(int resourcePackId) {
    return query(
        "resorcepack/" + resourcePackId,
        TYPE_RESOURCEPACK,
        (statusCode, body) -> null
    );
  }

  @Override
  public @NotNull CompletableFuture<@NotNull Resourcepack> random() {
    return query(
        "resourcepacks/random",
        TYPE_RESOURCEPACK,
        (statusCode, body) -> null
    );
  }

  @Override
  public @NotNull CompletableFuture<@NotNull String> download(int resourcePackId) {
    return query(
        "download/" + resourcePackId,
        TYPE_STRING,
        (statusCode, body) -> null
    );
  }

  @Override
  public @NotNull CompletableFuture<@NotNull List<@NotNull String>> categories() {
    return query(
        "category",
        TYPE_STRINGS,
        (statusCode, body) -> Collections.emptyList()
    );
  }

  protected interface IResponseTransformer<T> {

    @Nullable T transform(int statusCode, @Nullable String body);

  }

}
