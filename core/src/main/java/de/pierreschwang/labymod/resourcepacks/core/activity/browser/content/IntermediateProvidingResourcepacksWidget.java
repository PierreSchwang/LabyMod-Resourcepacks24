package de.pierreschwang.labymod.resourcepacks.core.activity.browser.content;

import de.pierreschwang.labymod.resourcepacks.api.dao.result.Resourcepack;
import de.pierreschwang.labymod.resourcepacks.core.widget.IntermediateResolvingWidget;
import de.pierreschwang.labymod.resourcepacks.core.widget.ResourcepackListEntryWidget;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@AutoWidget
public class IntermediateProvidingResourcepacksWidget extends IntermediateResolvingWidget<Resourcepack> {

    public IntermediateProvidingResourcepacksWidget(
            Function<Integer, CompletableFuture<List<Resourcepack>>> futureFunction) {
        super(futureFunction, ResourcepackListEntryWidget::new);
    }

}
