package de.pierreschwang.labymod.resourcepacks.core.widget;

import de.pierreschwang.labymod.resourcepacks.api.util.FutureUtil;
import de.pierreschwang.labymod.resourcepacks.core.Components;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@AutoWidget
public class IntermediateResolvingWidget<T> extends DivWidget {

    private final Function<Integer, CompletableFuture<List<T>>> futureFunction;
    private final Function<T, Widget> widgetFunction;

    private List<T> resolved = null;

    public IntermediateResolvingWidget(Function<Integer, CompletableFuture<List<T>>> futureFunction,
                                       Function<T, Widget> widgetFunction) {
        this.futureFunction = futureFunction;
        this.widgetFunction = widgetFunction;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        if (resolved == null) {
            addChild(ComponentWidget.component(Components.LOADING).addId("infotext"));
            resolved = new ArrayList<>();
            FutureUtil.resolveUntil(l -> !l.isEmpty(), 1, futureFunction, t -> {
                resolved.addAll(t);
                Laby.labyAPI().minecraft().executeNextTick(this::reInitialize);
            });
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
        addChild(widget);
        widget.initialize(this);
    }

}
