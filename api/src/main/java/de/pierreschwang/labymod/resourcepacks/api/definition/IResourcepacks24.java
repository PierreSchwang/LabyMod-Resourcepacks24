package de.pierreschwang.labymod.resourcepacks.api.definition;

import de.pierreschwang.labymod.resourcepacks.api.dao.result.PaginatedResult;
import de.pierreschwang.labymod.resourcepacks.api.dao.result.Resourcepack;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Facade for the rest api communication with the <a
 * href="https://resourcepacks24.de/api">Resourcepacks24 API</a>.
 */
public interface IResourcepacks24 {

  /**
   * Retrieves the paginated result of resourcepacks by a specific category.
   *
   * @param category The category of resourcepacks to search for. Must be present in
   *                 {@link #categories()}.
   * @param page     The page of the results to retrieve.
   * @return A {@link PaginatedResult} containing the resourcepacks of the category, as well as
   * {@link PaginatedResult.Paginator pagination data}.
   */
  @NotNull CompletableFuture<@NotNull PaginatedResult<@NotNull Resourcepack>> byCategory(
      @NotNull String category, @Range(from = 0, to = Integer.MAX_VALUE) int page);

  /**
   * Retrieves the newest uploaded resourcepacks.
   *
   * @return A list containing the newest resourcepacks.
   */
  @NotNull CompletableFuture<@NotNull List<@NotNull Resourcepack>> newest();

  /**
   * Retrieves the currently trending resourcepacks, based on Resourcepacks24.
   * <br>
   * <b>Note:</b> This api call does not return a paginated result, but rather a plain array of
   * resourcepacks. Based on basic testing, the page range seems to not exceed {@code 3}.
   *
   * @param page The page of entries to retrieve (similar to {@link #byCategory(String, int)})
   * @return A list containing the current trending resourcepacks.
   */
  @NotNull CompletableFuture<@NotNull List<@NotNull Resourcepack>> trending(
      @Range(from = 0, to = 3) int page);

  /**
   * Retrieves the currently promoted resourcepacks, based on Resourcepacks24.
   *
   * @return A list containing the currently promoted resourcepacks.
   */
  @NotNull CompletableFuture<@NotNull List<@NotNull Resourcepack>> promoted();

  /**
   * Retrieves all resourcepacks matching a search query.
   *
   * @param keyword The provided keyword / search query by the user.
   * @return A list containing all matched resourcepacks.
   */
  @NotNull CompletableFuture<@NotNull List<@NotNull Resourcepack>> search(String keyword);

  /**
   * Retrieves the "resourcepacks of the week" nominated by Resourcepacks24.
   * <br>
   * <b>Note:</b> This api call does not return a paginated result, but rather a plain array of
   * resourcepacks. Based on basic testing, the page range seems to not exceed {@code 5}.
   *
   * @param page The page of entries to retrieve (similar to {@link #byCategory(String, int)})
   * @return A list containing the resourcepacks of the week of the selected page.
   */
  @NotNull CompletableFuture<@NotNull List<@NotNull Resourcepack>> ofTheWeek(
      @Range(from = 0, to = 5) int page);

  /**
   * Retrieves a specific resourcepack by its internal id (specified by Resourcepacks24)
   *
   * @param resourcePackId The id of the resourcepack ({@link Resourcepack#id()})
   * @return The resourcepack, if found - otherwise {@code null}
   */
  @NotNull CompletableFuture<@Nullable Resourcepack> byId(int resourcePackId);

  /**
   * Retrieves a random resourcepack.
   *
   * @return The random resourcepack provided by Resourcepacks24
   */
  @NotNull CompletableFuture<@NotNull Resourcepack> random();

  /**
   * Generates a signed download link which is valid for 5 minutes.
   *
   * @param resourcePackId The id of the resourcepack to generate the download link for
   *                       ({@link Resourcepack#id()})
   * @return The direct & signed download link for the resourcepack.
   */
  @NotNull CompletableFuture<@Nullable String> download(int resourcePackId);

  /**
   * Shorthand for {@link #download(int)} using a resourcepack object.
   *
   * @param resourcepack The resourcepack to get the download link for.
   * @return The direct & signed download link for the resourcepack
   * @see #download(int)
   */
  default @NotNull CompletableFuture<@Nullable String> download(
      @NotNull Resourcepack resourcepack) {
    return download(resourcepack.id());
  }

  /**
   * Retrieves a list of all available resourcepack categories.
   *
   * @return A list containing all categories.
   */
  @NotNull CompletableFuture<@NotNull List<@NotNull String>> categories();

}
