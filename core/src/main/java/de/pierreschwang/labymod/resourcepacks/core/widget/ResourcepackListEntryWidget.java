package de.pierreschwang.labymod.resourcepacks.core.widget;

import de.pierreschwang.labymod.resourcepacks.api.dao.result.Resourcepack;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Resourcepack} as an entry in a list of multiple resourcepacks. Based on
 * {@code StoreItemWidget} for a convenient style.
 */
@AutoWidget
@Link("resourcepack-list-widget.lss")
public class ResourcepackListEntryWidget extends SimpleWidget {

  private final Resourcepack resourcepack;

  public ResourcepackListEntryWidget(@NotNull Resourcepack resourcepack) {
    this.resourcepack = resourcepack;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    this.addChild(buildResoucepackItem());
  }

  private Widget buildResoucepackItem() {
    FlexibleContentWidget wrapper = new FlexibleContentWidget().addId("resourcepack-wrapper");
    // TODO: preloader? -> non-blocking
    IconWidget thumbnail = new IconWidget(Icon.url(resourcepack.thumbnail()))
        .addId("resourcepack-thumbnail");
    wrapper.addContent(thumbnail);
    DivWidget textWrapper = new DivWidget().addId("resourcepack-text-wrapper");
    // TODO: add rating
    HorizontalListWidget nameWrapper = new HorizontalListWidget().addId(
        "resourcepack-name-wrapper");
    nameWrapper.addEntry(
        ComponentWidget.text(resourcepack.name()).addId("resourcepack-name"));
    nameWrapper.addEntry(ComponentWidget.text(resourcepack.username(), NamedTextColor.GREEN)
        .addId("resourcepack-author"));
    textWrapper.addChild(nameWrapper);

    ComponentWidget descriptionWrapper = ComponentWidget.text(resourcepack.description())
        .addId("resourcepack-description-wrapper");
    textWrapper.addChild(descriptionWrapper);

    wrapper.addFlexibleContent(textWrapper);
    // TODO: button
    return wrapper;
  }

}