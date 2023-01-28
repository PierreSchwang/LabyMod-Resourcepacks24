package de.pierreschwang.labymod.resourcepacks.core.widget;

import de.pierreschwang.labymod.resourcepacks.api.pagination.Paginator;
import de.pierreschwang.labymod.resourcepacks.core.Components;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@AutoWidget
@Link("listing-widget.lss")
public class ListingWidget<T> extends DivWidget {

    private final Paginator<T> paginator;
    private final Function<T, Widget> widgetFunction;
    private List<T> resolved;

    private int page;

    public ListingWidget(Paginator<T> paginator, Function<T, Widget> widgetFunction) {
        this.paginator = paginator;
        this.widgetFunction = widgetFunction;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        if (resolved == null) {
            addChild(ComponentWidget.component(Components.LOADING).addId("infotext"));
            resolved = new ArrayList<>();
            this.paginator.getContent(page).whenCompleteAsync((ts, throwable) -> {
                resolved = ts;
                reInitialize();
            }, Laby.labyAPI().minecraft()::executeOnRenderThread);
            return;
        }

        if (resolved.isEmpty()) {
            addChild(ComponentWidget.component(Components.NO_DATA).addId("infotext"));
        }

        VerticalListWidget<Widget> list = new VerticalListWidget<>();
        for (T t : resolved) {
            list.addChild(widgetFunction.apply(t));
        }
        ScrollWidget widget = new ScrollWidget(list);
        addChildInitialized(widget);
    }

}
