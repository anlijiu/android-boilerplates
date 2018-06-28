package com.anlijiu.example.data.cache;

import android.content.Context;
import com.anlijiu.example.data.cache.serializer.Serializer;
import com.anlijiu.example.data.entity.CardEntity;
import com.anlijiu.example.data.exception.CardNotFoundException;
import com.anlijiu.example.domain.executor.ThreadExecutor;
import io.reactivex.Observable;
import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link CardCache} implementation.
 */
@Singleton
public class CardCacheImpl implements CardCache {

  private static final String SETTINGS_FILE_NAME = "com.anlijiu.example.data.SETTINGS";
  private static final String SETTINGS_KEY_LAST_CACHE_UPDATE = "last_cache_update";

  private static final String DEFAULT_FILE_NAME = "card_";
  private static final long EXPIRATION_TIME = 60 * 10 * 1000;

  private final Context context;
  private final File cacheDir;
  private final Serializer serializer;
  private final FileManager fileManager;
  private final ThreadExecutor threadExecutor;

  /**
   * Constructor of the class {@link CardCacheImpl}.
   *
   * @param context A
   * @param serializer {@link Serializer} for object serialization.
   * @param fileManager {@link FileManager} for saving serialized objects to the file system.
   */
  @Inject CardCacheImpl(Context context, Serializer serializer,
      FileManager fileManager, ThreadExecutor executor) {
    if (context == null || serializer == null || fileManager == null || executor == null) {
      throw new IllegalArgumentException("Invalid null parameter");
    }
    this.context = context.getApplicationContext();
    this.cacheDir = this.context.getCacheDir();
    this.serializer = serializer;
    this.fileManager = fileManager;
    this.threadExecutor = executor;
  }

  @Override public Observable<CardEntity> get(final int cardId) {
    return Observable.create(emitter -> {
      final File cardEntityFile = CardCacheImpl.this.buildFile(cardId);
      final String fileContent = CardCacheImpl.this.fileManager.readFileContent(cardEntityFile);
      final CardEntity cardEntity =
          CardCacheImpl.this.serializer.deserialize(fileContent, CardEntity.class);

      if (cardEntity != null) {
        emitter.onNext(cardEntity);
        emitter.onComplete();
      } else {
        emitter.onError(new CardNotFoundException());
      }
    });
  }

  @Override public void put(CardEntity cardEntity) {
    if (cardEntity != null) {
      final File cardEntityFile = this.buildFile(cardEntity.getId());
      if (!isCached(cardEntity.getId())) {
        final String jsonString = this.serializer.serialize(cardEntity, CardEntity.class);
        this.executeAsynchronously(new CacheWriter(this.fileManager, cardEntityFile, jsonString));
        setLastCacheUpdateTimeMillis();
      }
    }
  }

  @Override public boolean isCached(long cardId) {
    final File cardEntityFile = this.buildFile(cardId);
    return this.fileManager.exists(cardEntityFile);
  }

  @Override public boolean isExpired() {
    long currentTime = System.currentTimeMillis();
    long lastUpdateTime = this.getLastCacheUpdateTimeMillis();

    boolean expired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME);

    if (expired) {
      this.evictAll();
    }

    return expired;
  }

  @Override public void evictAll() {
    this.executeAsynchronously(new CacheEvictor(this.fileManager, this.cacheDir));
  }

  /**
   * Build a file, used to be inserted in the disk cache.
   *
   * @param cardId The id card to build the file.
   * @return A valid file.
   */
  private File buildFile(long cardId) {
    final StringBuilder fileNameBuilder = new StringBuilder();
    fileNameBuilder.append(this.cacheDir.getPath());
    fileNameBuilder.append(File.separator);
    fileNameBuilder.append(DEFAULT_FILE_NAME);
    fileNameBuilder.append(cardId);

    return new File(fileNameBuilder.toString());
  }

  /**
   * Set in millis, the last time the cache was accessed.
   */
  private void setLastCacheUpdateTimeMillis() {
    final long currentMillis = System.currentTimeMillis();
    this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME,
        SETTINGS_KEY_LAST_CACHE_UPDATE, currentMillis);
  }

  /**
   * Get in millis, the last time the cache was accessed.
   */
  private long getLastCacheUpdateTimeMillis() {
    return this.fileManager.getFromPreferences(this.context, SETTINGS_FILE_NAME,
        SETTINGS_KEY_LAST_CACHE_UPDATE);
  }

  /**
   * Executes a {@link Runnable} in another Thread.
   *
   * @param runnable {@link Runnable} to execute
   */
  private void executeAsynchronously(Runnable runnable) {
    this.threadExecutor.execute(runnable);
  }

  /**
   * {@link Runnable} class for writing to disk.
   */
  private static class CacheWriter implements Runnable {
    private final FileManager fileManager;
    private final File fileToWrite;
    private final String fileContent;

    CacheWriter(FileManager fileManager, File fileToWrite, String fileContent) {
      this.fileManager = fileManager;
      this.fileToWrite = fileToWrite;
      this.fileContent = fileContent;
    }

    @Override public void run() {
      this.fileManager.writeToFile(fileToWrite, fileContent);
    }
  }

  /**
   * {@link Runnable} class for evicting all the cached files
   */
  private static class CacheEvictor implements Runnable {
    private final FileManager fileManager;
    private final File cacheDir;

    CacheEvictor(FileManager fileManager, File cacheDir) {
      this.fileManager = fileManager;
      this.cacheDir = cacheDir;
    }

    @Override public void run() {
      this.fileManager.clearDirectory(this.cacheDir);
    }
  }
}
