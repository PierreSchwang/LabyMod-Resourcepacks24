package de.pierreschwang.labymod.resourcepacks.api.util;

import net.labymod.api.Laby;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FutureUtil {

    /**
     * Attempts to resolve a variable amount of {@link CompletableFuture}s defined by a simple incrementing counter
     * Required for accessing the {@code Resourcepack24 API}, as some routes do not provide pagination data
     *
     * @param until          The predicate - if returns {@code true} another future function invoked with {@code counter++} will be resolved
     * @param start          The starting point of the incrementing counter
     * @param futureFunction The function which resolved the Future based on the counter
     * @param result         Will be invoked whenever a new future was resolved
     * @param <T>            The generic type of all futures and the result
     */
    public static <T> void resolveUntil(Predicate<T> until,
                                        int start,
                                        Function<Integer, CompletableFuture<T>> futureFunction,
                                        Consumer<T> result) {
        internalResolveUntil(until, start, futureFunction, result);
    }

    private static <T> void internalResolveUntil(Predicate<T> until,
                                                 int counter,
                                                 Function<Integer, CompletableFuture<T>> futureFunction,
                                                 Consumer<T> result) {
        futureFunction.apply(counter).whenCompleteAsync((t, throwable) -> {
            result.accept(t);
            if (until.test(t)) {
                internalResolveUntil(until, counter + 1, futureFunction, result);
            }
        }, Laby.labyAPI().minecraft()::executeNextTick);
    }

}
