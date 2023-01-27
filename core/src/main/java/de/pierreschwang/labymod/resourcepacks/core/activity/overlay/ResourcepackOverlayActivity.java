package de.pierreschwang.labymod.resourcepacks.core.activity.overlay;

import de.pierreschwang.labymod.resourcepacks.core.ResourcepacksAddon;
import de.pierreschwang.labymod.resourcepacks.core.activity.browser.RemoteResourcepacksOverviewActivity;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.LabyScreen;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.AbstractLayerActivity;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@AutoActivity
@Link("open-button.lss")
public class ResourcepackOverlayActivity extends AbstractLayerActivity {

  private static final ResourceLocation SPRITE = ResourceLocation
      .create("resourcepacks24", "sprites/pagination.png");
  private final ResourcepacksAddon addon;

  public ResourcepackOverlayActivity(ScreenInstance screenInstance, ResourcepacksAddon addon) {
    super(screenInstance);
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    HorizontalListWidget wrapper = new HorizontalListWidget();
    DivWidget inner = new DivWidget();
    inner.addChild(ButtonWidget.icon(Icon.sprite32(SPRITE, 2, 0),
        () -> Laby.labyAPI().minecraft().minecraftWindow()
            .displayScreen(new RemoteResourcepacksOverviewActivity(this.addon.resourcepacks24()))));

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
    return new ResourcepackOverlayActivity(parent.currentLabyScreen(), addon).generic();
  }

}
