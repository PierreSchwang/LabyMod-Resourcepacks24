package de.pierreschwang.labymod.resourcepacks.core.activity.browser;

import de.pierreschwang.labymod.resourcepacks.api.util.StringUtil;
import de.pierreschwang.labymod.resourcepacks.core.ResourcepacksAddon;
import de.pierreschwang.labymod.resourcepacks.core.activity.browser.content.IntermediateProvidingResourcepacksWidget;
import de.pierreschwang.labymod.resourcepacks.core.widget.SearchWidget;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.LabyScreen;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.entry.FlexibleContentEntry;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link net.labymod.api.client.gui.screen.activity.Activity} which contains a sidebar
 * navigation and a displayed activity
 */
@AutoActivity
@Link("wrapper.lss")
public class NavigationWrappedActivity extends SimpleActivity {

    private final ResourcepacksAddon addon;

    private FlexibleContentEntry flexibleContentEntry;
    private Widget activeWindow;

    public NavigationWrappedActivity(ResourcepacksAddon addon) {
        this.addon = addon;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        FlexibleContentWidget wrapper = new FlexibleContentWidget().addId("resourcepack-main-wrapper");
        wrapper.addChild(new FlexibleContentEntry(buildSidebar(), false));
        flexibleContentEntry = wrapper.addChild(new FlexibleContentEntry(
                (activeWindow = new IntermediateProvidingResourcepacksWidget(
                        this.addon.resourcepacks24()::trending
                )),
                true
        ));
        this.document.addChild(wrapper);
    }

    @Override
    public <T extends LabyScreen> @Nullable T renew() {
        return new NavigationWrappedActivity(this.addon).generic();
    }

    private Widget buildSidebar() {
        VerticalListWidget<Widget> listWidget = new VerticalListWidget<>().addId("sidebar-container");
        listWidget.addChild(new SearchWidget(s -> System.out.println("Search: " + s)));
        listWidget.addChild(
                ComponentWidget.i18n("resourcepacks24.sidebar.categories", NamedTextColor.GRAY));
        this.addon.resourcepacks24().categories().whenComplete((strings, throwable) ->
                this.labyAPI.minecraft().executeNextTick(() -> {
                    for (String category : strings) {
                        listWidget.addChildInitialized(ButtonWidget.component(
                                Component.text(StringUtil.capitalize(category)),
                                () -> navigateToCategory(category)
                        ));
                    }
                }));
        return listWidget;
    }

    private void navigateToCategory(String category) {

        flexibleContentEntry.reInitialize();
    }

}
