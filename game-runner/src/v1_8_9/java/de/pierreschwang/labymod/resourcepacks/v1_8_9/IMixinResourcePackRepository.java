package de.pierreschwang.labymod.resourcepacks.v1_8_9;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

public interface IMixinResourcePackRepository {

  File dirResourcepacks();

  ReentrantLock lock();

}
