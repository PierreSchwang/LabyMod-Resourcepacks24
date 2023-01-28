package de.pierreschwang.labymod.resourcepacks.core.widget.resourcepack;

import de.pierreschwang.labymod.resourcepacks.api.util.FutureUtil;
import de.pierreschwang.labymod.resourcepacks.core.widget.ListingWidget;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.Widget;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@AutoWidget
@Link("paginated-widget.lss")
public class IntermediateResolvingWidget<T> extends ListingWidget<T> {

    public IntermediateResolvingWidget(
            Function<Integer, CompletableFuture<List<T>>> futureFunction,
            Function<T, Widget> widgetFunction,
            UpdateStrategy updateStrategy) {
        super(widgetFunction, updateStrategy);
        FutureUtil.resolveUntil(ts -> !ts.isEmpty(), 1, futureFunction, this::supplyData);
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
    }
}
