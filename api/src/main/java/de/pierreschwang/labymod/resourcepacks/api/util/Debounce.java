package de.pierreschwang.labymod.resourcepacks.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Stolen from {@link net.labymod.api.util.Debounce} - I need the possibility to cancel tasks
 */
public class Debounce {

    private static final Map<String, ScheduledFuture<?>> TASKS = new HashMap<>();
    private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(1);

    public static void of(String id, long milliseconds, Runnable runnable) {
        ScheduledFuture<?> task = TASKS.get(id);
        if (task != null) {
            task.cancel(true);
        }

        task = SERVICE.schedule(() -> {
            runnable.run();
            TASKS.remove(id);
        }, milliseconds, TimeUnit.MILLISECONDS);

        TASKS.put(id, task);
    }

    public static void cancel(String id) {
        ScheduledFuture<?> task = TASKS.remove(id);
        if (task != null) {
            task.cancel(true);
        }
    }

}
