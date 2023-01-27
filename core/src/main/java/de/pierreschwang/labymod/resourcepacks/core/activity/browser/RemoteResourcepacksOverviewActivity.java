package de.pierreschwang.labymod.resourcepacks.core.activity.browser;

import de.pierreschwang.labymod.resourcepacks.api.definition.IResourcepacks24;
import de.pierreschwang.labymod.resourcepacks.core.activity.browser.content.TrendingResourcepacksWidget;
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
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.entry.FlexibleContentEntry;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.HrWidget;
import net.labymod.api.util.I18n;
import org.jetbrains.annotations.Nullable;

@AutoActivity
@Link("resourcepack-list-widget.lss")
@Link("wrapper.lss")
@Link("paginated-widget.lss")
public class RemoteResourcepacksOverviewActivity extends SimpleActivity {

  private final IResourcepacks24 resourcepacks24;

  public RemoteResourcepacksOverviewActivity(IResourcepacks24 resourcepacks24) {
    this.resourcepacks24 = resourcepacks24;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    FlexibleContentWidget wrapper = new FlexibleContentWidget().addId("resourcepack-main-wrapper");
    wrapper.addChild(new FlexibleContentEntry(buildSidebar(), false));
    wrapper.addChild(new FlexibleContentEntry(
        new TrendingResourcepacksWidget(this.resourcepacks24, 0),
        true
    ));
    this.document.addChild(wrapper);
  }

  private Widget buildSidebar() {
    VerticalListWidget<Widget> listWidget = new VerticalListWidget<>().addId("sidebar-container");
    listWidget.addChild(new TextFieldWidget()
        .placeholder(Component.text(I18n.translate("resourcepacks24.sidebar.search")))
        .updateListener(this::updateSearchContent)
        .addId("search-field")
    );
    listWidget.addChild(new HrWidget());
    listWidget.addChild(new ButtonWidget().updateComponent(
        Component.text(I18n.translate("resourcepacks24.sidebar.browse"))));
    listWidget.addChild(new HrWidget());
    listWidget.addChild(
        ComponentWidget.i18n("resourcepacks24.sidebar.categories", NamedTextColor.GRAY));
    resourcepacks24.categories().whenComplete((strings, throwable) -> {
      this.labyAPI.minecraft().executeNextTick(() -> {
        for (String category : strings) {
          listWidget.addChildInitialized(
              new ButtonWidget().updateComponent(Component.text(category)));
        }
      });
    });
    return listWidget;
  }

  private void updateSearchContent(String query) {

  }

  @Override
  public <T extends LabyScreen> @Nullable T renew() {
    return new RemoteResourcepacksOverviewActivity(resourcepacks24).generic();
  }

}