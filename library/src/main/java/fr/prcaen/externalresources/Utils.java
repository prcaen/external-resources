package fr.prcaen.externalresources;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.AnyRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.M;

public final class Utils {

  @Nullable
  public static NetworkInfo getActiveNetworkInfo(Context context) {
    if(!hasNetworkStatePermission(context)) {
      Logger.w(ExternalResources.TAG, "To work perfectly, ACCESS_NETWORK_STATE permission is required.");
      return null;
    }

    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
    return connectivityManager.getActiveNetworkInfo();
  }

  @TargetApi(JELLY_BEAN_MR1)
  @SuppressWarnings("deprecation")
  public static boolean isAirplaneModeOn(Context context) {
    ContentResolver contentResolver = context.getContentResolver();

    try {
      if(SDK_INT < JELLY_BEAN_MR1) {
        return Settings.System.getInt(contentResolver, Settings.System.AIRPLANE_MODE_ON, 0) != 0;
      } else {
        return Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
      }
    } catch (NullPointerException | SecurityException e) {
      return false;
    }
  }

  public static boolean hasInternetPermission(Context context) {
    return hasPermission(context, INTERNET);
  }

  public static boolean hasNetworkStatePermission(Context context) {
    return hasPermission(context, ACCESS_NETWORK_STATE);
  }

  @SuppressWarnings("deprecation")
  @ColorInt
  public static int getColor(Context context, @ColorRes int resId) {
    if(SDK_INT >= M) {
      return context.getColor(resId);
    } else {
      return context.getResources().getColor(resId);
    }
  }

  @Nullable
  public static String getAndroidResourceEntryName(Context context, @AnyRes int resId) {
    try {
      return context.getResources().getResourceEntryName(resId);
    } catch (android.content.res.Resources.NotFoundException e) {
      return null;
    }
  }

  @IdRes
  public static int getAndroidResourceIdentifier(Context context, String key, String defType) {
    return context.getResources().getIdentifier(key, defType, context.getPackageName());
  }

  private static boolean hasPermission(Context context, String permission) {
    return context.checkCallingOrSelfPermission(permission) == PERMISSION_GRANTED;
  }
}
