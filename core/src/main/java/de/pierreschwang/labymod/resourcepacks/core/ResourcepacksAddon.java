package de.pierreschwang.labymod.resourcepacks.core;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import de.pierreschwang.labymod.resourcepacks.api.definition.IResourcepacks24;
import de.pierreschwang.labymod.resourcepacks.api.impl.Resourcepacks24OkHttpImpl;
import de.pierreschwang.labymod.resourcepacks.core.activity.overlay.ResourcepackOverlayActivity;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.inject.LabyGuice;
import net.labymod.api.inject.LabyInjector;
import net.labymod.api.models.addon.annotation.AddonListener;

@Singleton
@AddonListener
public class ResourcepacksAddon extends LabyAddon<ResourcepacksConfiguration> {

  private static final String LABY_RP_TOKEN = "6b514bb5-cb55-4f68-8c62-3031cf871a72";

  @Override
  protected void enable() {
    this.registerSettingCategory();

    LabyGuice.addModules(binder -> binder.bind(IResourcepacks24.class)
        .toInstance(new Resourcepacks24OkHttpImpl(LABY_RP_TOKEN)));

    labyAPI().activityOverlayService().registerOverlay(
        "resource_pack_settings", ResourcepackOverlayActivity.class,
        parentScreen -> new ResourcepackOverlayActivity(parentScreen, this)
    );
  }

  @Override
  protected Class<ResourcepacksConfiguration> configurationClass() {
    return ResourcepacksConfiguration.class;
  }

}