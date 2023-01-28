package de.pierreschwang.labymod.resourcepacks.core.widget;

import de.pierreschwang.labymod.resourcepacks.core.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;

@AutoWidget
@Link("listing-widget.lss")
public class ListingWidget<T> extends DivWidget {

    private final Function<T, Widget> widgetFunction;
    private final UpdateStrategy updateStrategy;
    private List<T> resolved;

    public ListingWidget(Function<T, Widget> widgetFunction, UpdateStrategy updateStrategy) {
        this.widgetFunction = widgetFunction;
        this.updateStrategy = updateStrategy;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        if (resolved == null) {
            addChild(ComponentWidget.component(Components.LOADING).addId("infotext"));
            resolved = new ArrayList<>();
            return;
        }

        if (resolved.isEmpty()) {
            addChild(ComponentWidget.component(Components.NO_DATA).addId("infotext"));
            return;
        }

        VerticalListWidget<Widget> list = new VerticalListWidget<>();
        for (T t : resolved) {
            list.addChild(widgetFunction.apply(t));
        }
        ScrollWidget widget = new ScrollWidget(list);
        addChildInitialized(widget);
    }

    public void supplyData(List<T> data) {
        if (updateStrategy == UpdateStrategy.APPEND) {
            this.resolved.addAll(data);
        } else {
            this.resolved = data;
        }
        this.reInitialize();
    }

    public static <F> ListingWidget<F> ofFuture(Function<F, Widget> widgetFunction, UpdateStrategy updateStrategy, CompletableFuture<List<F>> future) {
        ListingWidget<F> widget = new ListingWidget<>(widgetFunction, updateStrategy);
        future.whenCompleteAsync((fs, throwable) -> widget.supplyData(fs), Laby.labyAPI().minecraft()::executeNextTick);
        return widget;
    }

    public enum UpdateStrategy {
        APPEND,
        REPLACE
    }

}
