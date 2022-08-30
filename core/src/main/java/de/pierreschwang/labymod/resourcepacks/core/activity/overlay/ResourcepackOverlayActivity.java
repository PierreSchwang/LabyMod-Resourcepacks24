package de.pierreschwang.labymod.resourcepacks.core.activity.overlay;

import de.pierreschwang.labymod.resourcepacks.core.ResourcepacksAddon;
import de.pierreschwang.labymod.resourcepacks.core.activity.browser.RemoteResourcepacksOverviewActivity;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.LabyScreen;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenWrapper;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.OverlayActivity;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.inject.LabyGuice;
import org.jetbrains.annotations.Nullable;

@AutoActivity
@Link("open-button.lss")
public class ResourcepackOverlayActivity extends OverlayActivity {

  private final ResourcepacksAddon addon;

  public ResourcepackOverlayActivity(ScreenWrapper parentScreen, ResourcepacksAddon addon) {
    super(parentScreen);
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    HorizontalListWidget wrapper = new HorizontalListWidget();
    DivWidget inner = new DivWidget();
    inner.addChild(ButtonWidget.icon(Icon.sprite32(
        ResourceLocation.create("resourcepacks24", "sprites/img.png"),
        0, 1
    ), () -> this.displayScreen(LabyGuice.getInstance(RemoteResourcepacksOverviewActivity.class))));

    wrapper.spaceBetweenEntries().set(8);
    wrapper.addEntry(inner);
    wrapper.addEntry(new DivWidget());
    this.document().addChild(wrapper);
  }

  @Override
  public void renderOverlay(Stack stack, MutableMouse mouse, float partialTicks) {
    super.renderOverlay(stack, mouse, partialTicks);
  }

  @Override
  public <T extends LabyScreen> @Nullable T renew() {
    return new ResourcepackOverlayActivity(getParentScreen(), addon).generic();
  }

}
