package de.pierreschwang.labymod.resourcepacks.core;

import de.pierreschwang.labymod.resourcepacks.api.client.IResourcepackAdapter;
import de.pierreschwang.labymod.resourcepacks.api.definition.IResourcepacks24;
import de.pierreschwang.labymod.resourcepacks.api.impl.Resourcepack24NativeHttpImpl;
import de.pierreschwang.labymod.resourcepacks.api.util.FutureUtil;
import de.pierreschwang.labymod.resourcepacks.core.activity.overlay.ResourcepackOverlayActivity;
import de.pierreschwang.labymod.resourcepacks.core.generated.DefaultReferenceStorage;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.gui.screen.NamedScreen;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonMain;

import java.util.concurrent.CompletableFuture;

@AddonMain
public class ResourcepacksAddon extends LabyAddon<ResourcepacksConfiguration> {


  public static final ResourceLocation ICONS = ResourceLocation
          .create("resourcepacks24", "sprites/icons.png");

  private static final String LABY_RP_TOKEN = "6b514bb5-cb55-4f68-8c62-3031cf871a72";

  private IResourcepacks24 resourcepacks24;

  private IResourcepackAdapter adapter;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.resourcepacks24 = new Resourcepack24NativeHttpImpl(LABY_RP_TOKEN);
    labyAPI().activityOverlayRegistry().register(
        NamedScreen.RESOURCE_PACK_SETTINGS, ResourcepackOverlayActivity.class,
        parentScreen -> new ResourcepackOverlayActivity(parentScreen, this)
    );

    DefaultReferenceStorage storage = getReferenceStorageAccessor();
    this.adapter = storage.iResourcepackAdapter();
  }

  @Override
  protected Class<ResourcepacksConfiguration> configurationClass() {
    return ResourcepacksConfiguration.class;
  }

  public IResourcepacks24 resourcepacks24() {
    return resourcepacks24;
  }

  public IResourcepackAdapter adapter() {
    return adapter;
  }
}