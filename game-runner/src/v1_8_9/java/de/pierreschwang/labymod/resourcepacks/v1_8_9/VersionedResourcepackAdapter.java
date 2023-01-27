package de.pierreschwang.labymod.resourcepacks.v1_8_9;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.pierreschwang.labymod.resourcepacks.api.client.IResourcepackAdapter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

@Singleton
@Implements(IResourcepackAdapter.class)
public class VersionedResourcepackAdapter implements IResourcepackAdapter {

  private final ListeningExecutorService executor;

  @Inject
  public VersionedResourcepackAdapter() {
    executor = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
  }

  @Override
  public ListenableFuture<Object> setResourcePackInstance(File resourcePackFile) {
    return Minecraft.getMinecraft().getResourcePackRepository()
        .setResourcePackInstance(resourcePackFile);
  }

  @Override
  public ListenableFuture<Object> addResourcepack(InputStream stream, String fileName) {
    IMixinResourcePackRepository mixin = (IMixinResourcePackRepository) Minecraft.getMinecraft()
        .getResourcePackRepository();

    return executor.submit(() -> {
      String finalFileName = fileName;
      if (finalFileName.endsWith(".zip")) {
        finalFileName = finalFileName.substring(0, ".zip".length());
      }
      mixin.lock().lock();
      try {
        File destination = new File(mixin.dirResourcepacks(), finalFileName + ".zip");
        if (destination.exists()) { // uh-oh
          destination = new File(
              mixin.dirResourcepacks() + "-" + System.currentTimeMillis() + ".zip");
        }
        FileUtils.copyInputStreamToFile(stream, destination); // copies to file and closes IS
        Minecraft.getMinecraft().getResourcePackRepository().updateRepositoryEntriesAll();
      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        mixin.lock().unlock();
      }
      return null;
    });
  }

}
