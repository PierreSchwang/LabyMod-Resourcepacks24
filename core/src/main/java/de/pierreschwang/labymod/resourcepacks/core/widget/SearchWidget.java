package de.pierreschwang.labymod.resourcepacks.core.widget;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.util.Debounce;
import net.labymod.api.util.I18n;

import java.util.function.Consumer;

@AutoWidget
@Link("search.lss")
public class SearchWidget extends DivWidget {

    private static final String DEBOUNCE_ID = "rp24_addon_search_widget";

    public SearchWidget(Consumer<String> query) {
        addChild(new TextFieldWidget()
                .placeholder(Component.text(I18n.translate("resourcepacks24.sidebar.search")))
                .updateListener(s -> Debounce.of(DEBOUNCE_ID, 200, () -> {
                    if (isDestroyed()) {
                        return;
                    }
                    query.accept(s);
                }))).addId("search-widget-input");
    }
}