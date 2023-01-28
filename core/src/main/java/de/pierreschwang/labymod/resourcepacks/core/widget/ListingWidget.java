package de.pierreschwang.labymod.resourcepacks.core.widget;

import de.pierreschwang.labymod.resourcepacks.api.pagination.Paginator;
import de.pierreschwang.labymod.resourcepacks.core.Components;
import de.pierreschwang.labymod.resourcepacks.core.ResourcepacksAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.entry.FlexibleContentEntry;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@AutoWidget
@Link("listing-widget.lss")
public class ListingWidget<T> extends DivWidget {

    private final Paginator<T> paginator;
    private final Function<T, Widget> widgetFunction;
    private List<T> resolved;

    private int page = 1;

    private boolean previousPage = false, nextPage = false;

    public ListingWidget(Paginator<T> paginator, Function<T, Widget> widgetFunction) {
        this.paginator = paginator;
        this.widgetFunction = widgetFunction;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        if (resolved == null) {
            refresh();
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

        // control bar

        FlexibleContentWidget flexibleContentWidget = new FlexibleContentWidget();
        flexibleContentWidget.addId("controls");
        flexibleContentWidget.addChild(new FlexibleContentEntry(
                ButtonWidget.icon(
                        Icon.sprite32(ResourcepacksAddon.ICONS, 2, 0).setHoverOffset(0, 1)
                ), false
        ));
        flexibleContentWidget.addChild(new FlexibleContentEntry(
                ComponentWidget.i18n("resourcepacks24.pagination.page-indicator", page)
                        .addId("page-indicator"),
                false
        ));
        flexibleContentWidget.addChild(new FlexibleContentEntry(
                ButtonWidget.icon(
                        Icon.sprite32(ResourcepacksAddon.ICONS, 1, 0).setHoverOffset(0, 1)
                ), false
        ));
        addChild(flexibleContentWidget);
    }

    private void refresh() {
        CompletableFuture<Boolean> hasPreviousPage = this.paginator.hasPreviousPage(page);
        CompletableFuture<Boolean> hasNextPage = this.paginator.hasNextPage(page);
        CompletableFuture<List<T>> content = this.paginator.getContent(page);

        resolved = new ArrayList<>();
        Laby.labyAPI().minecraft().executeOnRenderThread(this::reInitialize);

        CompletableFuture.allOf(hasPreviousPage, hasNextPage, content).whenCompleteAsync((unused, throwable) -> {
            this.resolved = content.getNow(Collections.emptyList());
            this.previousPage = hasPreviousPage.getNow(false);
            this.nextPage = hasNextPage.getNow(false);
            this.reInitialize();
        }, Laby.labyAPI().minecraft()::executeOnRenderThread);
    }

}
