package de.pierreschwang.labymod.resourcepacks.core.activity.browser;

import de.pierreschwang.labymod.resourcepacks.api.pagination.Paginator;
import de.pierreschwang.labymod.resourcepacks.api.util.StringUtil;
import de.pierreschwang.labymod.resourcepacks.core.ResourcepacksAddon;
import de.pierreschwang.labymod.resourcepacks.core.widget.ListingWidget;
import de.pierreschwang.labymod.resourcepacks.core.widget.SearchWidget;
import de.pierreschwang.labymod.resourcepacks.core.widget.resourcepack.ResourcepackListEntryWidget;
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
import net.labymod.api.client.gui.screen.widget.widgets.renderer.HrWidget;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * An {@link net.labymod.api.client.gui.screen.activity.Activity} which contains a sidebar
 * navigation and a displayed activity
 */
@AutoActivity
@Link("wrapper.lss")
public class NavigationWrappedActivity extends SimpleActivity {

    private final ResourcepacksAddon addon;

    private List<String> categories;

    private Widget activeWindow;

    public NavigationWrappedActivity(ResourcepacksAddon addon) {
        this.addon = addon;
        this.activeWindow = new ListingWidget<>(
                Paginator.peeking(this.addon.resourcepacks24()::trending),
                ResourcepackListEntryWidget::new
        );
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        FlexibleContentWidget wrapper = new FlexibleContentWidget().addId("resourcepack-main-wrapper");
        wrapper.addChild(new FlexibleContentEntry(buildSidebar(), false));
        wrapper.addChild(new FlexibleContentEntry(activeWindow, true));
        this.document.addChild(wrapper);
    }

    @Override
    public <T extends LabyScreen> @Nullable T renew() {
        return new NavigationWrappedActivity(this.addon).generic();
    }

    private Widget buildSidebar() {
        VerticalListWidget<Widget> listWidget = new VerticalListWidget<>().addId("sidebar-container");
        listWidget.addChild(new SearchWidget(s -> System.out.println("Search: " + s)));
        listWidget.addChild(new HrWidget());
        listWidget.addChild(ButtonWidget.i18n("resourcepacks24.sidebar.newest", () -> {
            this.activeWindow = new ListingWidget<>(
                    Paginator.single(this.addon.resourcepacks24().newest()),
                    ResourcepackListEntryWidget::new
            );
            this.labyAPI.minecraft().executeNextTick(this::reload);
        }));

        listWidget.addChild(ButtonWidget.i18n("resourcepacks24.sidebar.of-the-week", () -> {
            this.activeWindow = new ListingWidget<>(
                    Paginator.peeking(this.addon.resourcepacks24()::ofTheWeek),
                    ResourcepackListEntryWidget::new
            );
            this.labyAPI.minecraft().executeNextTick(this::reload);
        }));

        listWidget.addChild(
                ComponentWidget.i18n("resourcepacks24.sidebar.categories", NamedTextColor.GRAY));

        if (this.categories != null) {
            for (String category : categories) {
                listWidget.addChild(ButtonWidget.component(
                        Component.text(StringUtil.capitalize(category)),
                        () -> navigateToCategory(category)
                ));
            }
        } else {
            this.addon.resourcepacks24().categories().whenCompleteAsync((strings, throwable) -> {
                this.categories = strings;
                NavigationWrappedActivity.this.reload();
            }, this.labyAPI.minecraft()::executeNextTick);
        }
        return listWidget;
    }

    private void navigateToCategory(String category) {
        // TODO
    }

}
