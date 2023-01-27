package de.pierreschwang.labymod.resourcepacks.core.activity.browser.content;

import de.pierreschwang.labymod.resourcepacks.api.dao.result.PaginatedResult;
import de.pierreschwang.labymod.resourcepacks.api.dao.result.PaginatedResult.Paginator;
import de.pierreschwang.labymod.resourcepacks.api.dao.result.Resourcepack;
import de.pierreschwang.labymod.resourcepacks.api.definition.IResourcepacks24;
import de.pierreschwang.labymod.resourcepacks.core.widget.PaginatedWidget;
import de.pierreschwang.labymod.resourcepacks.core.widget.ResourcepackListEntryWidget;
import java.util.concurrent.Executor;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;

@AutoWidget
public class TrendingResourcepacksWidget extends DivWidget {

  private static final Component LOADING_TEXT = Component.translatable("resourcepacks24.loading")
      .color(NamedTextColor.GRAY);

  private final IResourcepacks24 resourcepacks24;

  private final Executor syncExecutor;
  private int page;
  private PaginatedResult<Resourcepack> trending;

  public TrendingResourcepacksWidget(IResourcepacks24 resourcepacks24, int page) {
    this.resourcepacks24 = resourcepacks24;
    this.syncExecutor = command -> Laby.labyAPI().minecraft().executeNextTick(command);
    this.page = page;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    if (trending == null) {
      addChild(ComponentWidget.component(LOADING_TEXT).addId("infotext"));
      resourcepacks24.trending(page).whenCompleteAsync((resourcepacks, throwable) -> {
        this.trending = new PaginatedResult<>(resourcepacks,
            new Paginator(page, 5, 0, resourcepacks.size(), resourcepacks.size() * 5, 20));
        reInitialize();
      }, syncExecutor);
      return;
    }
    addChildInitialized(
        new PaginatedWidget<>(trending, ResourcepackListEntryWidget::new, this::switchPage));
  }

  private void switchPage(int page) {
    if (page < 0 || page > 5) {
      return;
    }
    this.trending = null;
    this.page = page;
    reInitialize();
  }

}
