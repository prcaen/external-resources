package fr.prcaen.externalresources;

import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Logger {
  @Retention(RetentionPolicy.SOURCE)
  @IntDef({
      LEVEL_OFF,
      LEVEL_ERROR,
      LEVEL_WARN,
      LEVEL_INFO,
      LEVEL_DEBUG,
      LEVEL_VERBOSE
  })
  public @interface Level {}
  public static final int LEVEL_OFF = 0;
  public static final int LEVEL_ERROR = 1;
  public static final int LEVEL_WARN = 2;
  public static final int LEVEL_INFO = 3;
  public static final int LEVEL_DEBUG = 4;
  public static final int LEVEL_VERBOSE = 5;

  @Level
  private static int level = LEVEL_ERROR;

  public static void v(String tag, String msg) {
    if(level == LEVEL_VERBOSE) {
      Log.v(tag, msg);
    }
  }

  public static void d(String tag, String msg) {
    if(level >= LEVEL_DEBUG) {
      Log.d(tag, msg);
    }
  }

  public static void i(String tag, String msg) {
    if(level >= LEVEL_INFO) {
      Log.i(tag, msg);
    }
  }

  public static void w(String tag, String msg) {
    if(level >= LEVEL_WARN) {
      Log.w(tag, msg);
    }
  }

  public static void e(String tag, String msg) {
    if(level >= LEVEL_ERROR) {
      Log.e(tag, msg);
    }
  }

  public static void e(String tag, String msg, Throwable tr) {
    if(level >= LEVEL_ERROR) {
      Log.e(tag, msg, tr);
    }
  }

  public static void setLevel(@Level int level) {
    Logger.level = level;
  }
}
