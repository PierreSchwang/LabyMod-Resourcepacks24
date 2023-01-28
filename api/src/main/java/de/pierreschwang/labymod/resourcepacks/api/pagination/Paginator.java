package de.pierreschwang.labymod.resourcepacks.api.pagination;

import de.pierreschwang.labymod.resourcepacks.api.dao.result.PaginatedResult;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public interface Paginator<T> {

    /**
     * Get the content of a specific page
     *
     * @param page The page to get the content for
     * @return A future which resolves with the requested content
     */
    CompletableFuture<@NonNull List<T>> getContent(int page);

    /**
     * Check if there is a next page available ({@code currentPage + 1})
     *
     * @param currentPage The current page to base of
     * @return A future which resolves {@code true} if there is a next page - {@code false} otherwise.
     */
    CompletableFuture<Boolean> hasNextPage(int currentPage);

    /**
     * Check if there is a previous page available ({@code currentPage - 1})
     * <p>
     * Defaults to {@code currentPage > 1}
     *
     * @param currentPage The current page to base of
     * @return A future which resolves {@code true} if there is a previous page - {@code false}
     * otherwise.
     */
    default CompletableFuture<Boolean> hasPreviousPage(int currentPage) {
        return CompletableFuture.completedFuture(currentPage > 1);
    }

    static <F> Paginator<F> peeking(
            Function<Integer, CompletableFuture<List<F>>> futureByPageFunction) {
        return new PeekingPaginator<F>() {
            @Override
            public CompletableFuture<List<F>> getContent(int page) {
                return futureByPageFunction.apply(page);
            }
        };
    }

    static <F> Paginator<F> single(CompletableFuture<List<F>> future) {
        return new Paginator<F>() {
            @Override
            public CompletableFuture<@NonNull List<F>> getContent(int page) {
                return future;
            }

            @Override
            public CompletableFuture<Boolean> hasNextPage(int currentPage) {
                return CompletableFuture.completedFuture(false);
            }

            @Override
            public CompletableFuture<Boolean> hasPreviousPage(int currentPage) {
                return CompletableFuture.completedFuture(false);
            }
        };
    }

    static <F> Paginator<F> paginated(
            Function<Integer, CompletableFuture<PaginatedResult<F>>> futureByPageFunction) {
        return new Paginator<F>() {

            final AtomicInteger totalPages = new AtomicInteger(0);

            @Override
            public CompletableFuture<List<F>> getContent(int page) {
                return futureByPageFunction.apply(page).thenApply(result -> {
                    totalPages.set(result.paginator().totalPages());
                    return result.data();
                });
            }

            @Override
            public CompletableFuture<Boolean> hasNextPage(int currentPage) {
                return CompletableFuture.completedFuture(currentPage < totalPages.get());
            }
        };
    }

}
