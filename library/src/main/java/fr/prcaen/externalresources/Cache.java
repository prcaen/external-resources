package fr.prcaen.externalresources;

import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import android.support.annotation.IntDef;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Cache {
  private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024;  // 5MB
  private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

  private static final String EXTERNAL_RESOURCES_FILE_NAME_CACHE = "external-resources-cache";

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({
      POLICY_NONE,
      POLICY_OFFLINE,
      POLICY_ALL
  })
  public @interface Policy {}

  public static final int POLICY_NONE = 0;
  public static final int POLICY_OFFLINE = 1;
  public static final int POLICY_ALL = 2;

  private final File cacheDir;
  private final long cacheSize;

  public Cache(Context context) {
    this.cacheDir = createDefaultCacheDir(context.getApplicationContext());
    this.cacheSize = calculateDiskCacheSize(cacheDir);
  }

  public File getCacheDir() {
    return cacheDir;
  }

  public long getCacheSize() {
    return cacheSize;
  }

  private static File createDefaultCacheDir(Context context) {
    File cache = new File(context.getCacheDir(), EXTERNAL_RESOURCES_FILE_NAME_CACHE);

    if (!cache.exists()) {
      //noinspection ResultOfMethodCallIgnored
      cache.mkdirs();
    }

    return cache;
  }

  @SuppressWarnings("deprecation")
  public static long calculateDiskCacheSize(File dir) {
    long size = MIN_DISK_CACHE_SIZE;

    try {
      StatFs statFs = new StatFs(dir.getAbsolutePath());
      final long available;

      if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
        available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
      } else {
        available = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
      }

      size = available / 50;
    } catch (IllegalArgumentException ignored) {
    }

    return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
  }
}
