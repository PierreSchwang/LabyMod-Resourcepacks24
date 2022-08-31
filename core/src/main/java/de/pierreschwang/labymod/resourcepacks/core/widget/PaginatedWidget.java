package de.pierreschwang.labymod.resourcepacks.core.widget;

import de.pierreschwang.labymod.resourcepacks.api.dao.result.PaginatedResult;
import java.util.function.Consumer;
import java.util.function.Function;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.action.Pressable;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.I18n;

@AutoWidget
@Link("paginated-widget.lss")
public class PaginatedWidget<T, W extends Widget> extends DivWidget {

  private static final ResourceLocation SPRITE = ResourceLocation
      .create("resourcepacks24", "sprites/pagination.png");

  private final Function<T, W> entryWidgetTransformer;
  private final Consumer<Integer> changePageConsumer;

  private final PaginatedResult<T> paginatedResult;

  public PaginatedWidget(PaginatedResult<T> paginatedResult,
      Function<T, W> entryWidgetTransformer, Consumer<Integer> changePageConsumer) {
    this.paginatedResult = paginatedResult;
    this.entryWidgetTransformer = entryWidgetTransformer;
    this.changePageConsumer = changePageConsumer;
    addChild(scrollWidgetForCurrentPage());
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
  }

  private Widget controlWidget() {
    HorizontalListWidget container = new HorizontalListWidget().addId("control-container");
    container.addEntry(button(
        0, 0, () -> changePageConsumer.accept(paginatedResult.paginator().currentPage() - 1),
        paginatedResult.paginator().currentPage() == 0
    ));
    String indicator = I18n.translate(
        "resourcepacks24.pagination.page-indicator",
        paginatedResult.paginator().currentPage(),
        paginatedResult.paginator().totalPages()
    );
    container.addEntry(ComponentWidget.text(indicator));
    container.addEntry(button(
        1, 0, () -> changePageConsumer.accept(paginatedResult.paginator().currentPage() + 1),
        paginatedResult.paginator().currentPage() == paginatedResult.paginator().totalPages()
    ));
    return container;
  }

  private ButtonWidget button(int slotX, int slotY, Pressable pressable, boolean disabled) {
    ButtonWidget widget = ButtonWidget.icon(
        Icon.sprite32(SPRITE, slotX, slotY), disabled ? null : pressable
    );
    if (disabled) {
      widget.setEnabled(false);
    }
    widget.addId("pagination-button");
    return widget;
  }

  private ScrollWidget scrollWidgetForCurrentPage() {
    VerticalListWidget<W> list = new VerticalListWidget<>();
    for (T data : paginatedResult.data()) {
      list.addChildInitialized(entryWidgetTransformer.apply(data), false);
    }
    list.spaceBetweenEntries().set(3f);
    list.addId("entry-container");
    ScrollWidget widget = new ScrollWidget(list);
    widget.addChildInitialized(controlWidget(), false);
    return widget;
  }

}
