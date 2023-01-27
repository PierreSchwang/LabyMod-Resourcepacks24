package de.pierreschwang.labymod.resourcepacks.api.client;

import com.google.common.util.concurrent.ListenableFuture;
import net.labymod.api.reference.annotation.Referenceable;
import java.io.File;
import java.io.InputStream;

@Referenceable()
public interface IResourcepackAdapter {

  /**
   * Set's the passed file as the currently used resourcepack and schedules a resources reload.
   *
   * @param resourcePackFile The resourcepack to set as currently used.
   */
  ListenableFuture<Object> setResourcePackInstance(File resourcePackFile);

  /**
   * Moves the contents of the passed stream into the resourcepacks folder into an archive called
   * {@code fileName}. Does not automatically enable the resourcepack to be used.
   *
   * @param stream   The stream containing the bytes of the resourcepack file
   * @param fileName The final file destination
   */
  ListenableFuture<Object> addResourcepack(InputStream stream, String fileName);

}
