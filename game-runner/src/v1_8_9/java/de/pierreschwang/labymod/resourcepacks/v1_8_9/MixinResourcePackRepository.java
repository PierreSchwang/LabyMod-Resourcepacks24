package de.pierreschwang.labymod.resourcepacks.v1_8_9;

import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

@Mixin(ResourcePackRepository.class)
public class MixinResourcePackRepository implements IMixinResourcePackRepository {

  @Shadow
  @Final
  private File dirResourcepacks;

  @Shadow
  @Final
  private ReentrantLock lock;

  public File dirResourcepacks() {
    return dirResourcepacks;
  }

  public ReentrantLock lock() {
    return lock;
  }
}
