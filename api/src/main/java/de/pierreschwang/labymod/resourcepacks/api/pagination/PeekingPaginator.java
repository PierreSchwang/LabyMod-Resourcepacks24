package de.pierreschwang.labymod.resourcepacks.api.pagination;

import java.util.concurrent.CompletableFuture;

/**
 * Paginator implementation for dirty APIs, which don't provide pagination data
 *
 * @param <T>
 */
public abstract class PeekingPaginator<T> implements Paginator<T> {

  /**
   * Checks if a next page is available, by getting the content of the next page and checking the
   * size of the content. If the result is empty or an exception was received, no next page is
   * available (presumably)
   * <p>
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<Boolean> hasNextPage(int currentPage) {
    return getContent(currentPage + 1)
        .thenApply(ts -> ts.size() > 0)
        .exceptionally(throwable -> false);
  }

}
