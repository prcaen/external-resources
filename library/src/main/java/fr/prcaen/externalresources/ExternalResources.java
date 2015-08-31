package fr.prcaen.externalresources;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.prcaen.externalresources.listener.OnExternalResourcesChangeListener;
import fr.prcaen.externalresources.listener.OnExternalResourcesLoadListener;
import fr.prcaen.externalresources.model.DimensionResource;
import fr.prcaen.externalresources.model.Resources;
import fr.prcaen.externalresources.url.DefaultUrl;
import fr.prcaen.externalresources.url.Url;

@SuppressWarnings("UnusedDeclaration")
public class ExternalResources {
  private static final String TAG = "ExternalResources";
  private static final String THREAD_NAME = "ExternalResourcesThread";
  private static final String EXCEPTION_NOT_LOADED = "Resources are null.";

  private static volatile ExternalResources singleton = null;

  @NonNull
  private final DisplayMetrics metrics;
  @NonNull
  private final Downloader downloader;
  @NonNull
  private final Resources defaultResources;
  @NonNull
  private final Options options;
  @Nullable
  private final OnExternalResourcesLoadListener listener;
  @NonNull
  private final ArrayList<OnExternalResourcesChangeListener> listeners = new ArrayList<>();
  @NonNull
  private Resources resources;
  @NonNull
  private Configuration configuration;

  private ExternalResources(@NonNull Context context, @NonNull Url url, @Cache.Policy int policy, @NonNull Resources defaultResources, @NonNull Options options, @Nullable OnExternalResourcesLoadListener listener) {
    this.configuration = new Configuration(context.getResources().getConfiguration());
    this.metrics = context.getResources().getDisplayMetrics();
    this.downloader = new Downloader(context, url, policy, options);
    this.defaultResources = defaultResources;
    this.resources = defaultResources;
    this.options = options;
    this.listener = listener;

    launch();
  }

  public void onConfigurationChanged(Configuration newConfig) {
    if (shouldRelaunch(newConfig)) {
      launch();
    }

    this.configuration = new Configuration(newConfig);
  }

  public boolean getBoolean(@NonNull String key) throws Resources.NotFoundException {
    return resources.getBoolean(key);
  }

  @ColorInt
  public int getColor(@NonNull String key) throws Resources.NotFoundException {
    return resources.getInteger(key);
  }

  public float getDimension(@NonNull String key) throws Resources.NotFoundException {
    String raw = getString(key);

    try {
      return DimensionResource.fromString(raw).toFloat(metrics);
    } catch (IllegalArgumentException e) {
      throw new Resources.NotFoundException("Dimension resource with key: " + key);
    }
  }

  public String getString(@NonNull String key) throws Resources.NotFoundException {
    return resources.getString(key);
  }

  public String getString(@NonNull String key, Object... formatArgs) throws Resources.NotFoundException {
    String raw = getString(key);
    return String.format(configuration.locale, raw, formatArgs);
  }

  public String[] getStringArray(@NonNull String key) throws Resources.NotFoundException {
    return resources.getStringArray(key);
  }

  public int getInteger(@NonNull String key) throws Resources.NotFoundException {
    return resources.getInteger(key);
  }

  public int[] getIntArray(@NonNull String key) throws Resources.NotFoundException {
    return resources.getIntArray(key);
  }

  public void register(OnExternalResourcesChangeListener listener) {
    listeners.add(listener);
  }

  public void unregister(OnExternalResourcesChangeListener listener) {
    listeners.remove(listener);
  }

  private void launch() {
    ExecutorService pool = Executors.newSingleThreadExecutor();
    pool.submit(new ResourcesRunnable(downloader, new ResourcesRunnable.Listener() {
      @Override
      public void onResourcesLoadSuccess(Resources resources) {
        ExternalResources.this.resources = defaultResources.merge(resources);

        triggerChange();
      }

      @Override
      public void onResourcesLoadFailed(Exception exception) {
        if (listener != null) {
          listener.onExternalResourcesLoadFailed(exception);
        }
      }
    }));
    pool.shutdown();
  }

  @SuppressWarnings("ConstantConditions")
  private boolean shouldRelaunch(Configuration newConfig) {
    return configuration.fontScale != newConfig.fontScale && options.isUseFontScale()
        || configuration.hardKeyboardHidden != newConfig.hardKeyboardHidden && options.isUseHardKeyboardHidden()
        || configuration.keyboard != newConfig.keyboard && options.isUseKeyboard()
        || configuration.keyboardHidden != newConfig.keyboardHidden && options.isUseKeyboardHidden()
        || configuration.mcc != newConfig.mcc && options.isUseMcc()
        || configuration.mnc != newConfig.mnc && options.isUseMnc()
        || configuration.navigation != newConfig.navigation && options.isUseNavigation()
        || configuration.navigationHidden != newConfig.navigationHidden && options.isUseNavigationHidden()
        || configuration.orientation != newConfig.orientation && options.isUseOrientation()
        || configuration.screenLayout != newConfig.screenLayout && options.isUseScreenLayout()
        || configuration.touchscreen != newConfig.touchscreen && options.isUseTouchscreen()
        || configuration.uiMode != newConfig.uiMode && options.isUseUiMode()
        || !configuration.locale.equals(newConfig.locale) && options.isUseLocale()
        || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && configuration.densityDpi != newConfig.densityDpi && options.isUseDensityDpi()
        || Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2 && configuration.screenWidthDp != newConfig.screenWidthDp && options.isUseScreenWidthDp()
        || Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2 && configuration.screenHeightDp != newConfig.screenHeightDp && options.isUseScreenHeightDp()
        || Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2 && configuration.smallestScreenWidthDp != newConfig.smallestScreenWidthDp && options.isUseSmallestScreenWidthDp();
  }

  private void triggerChange() {
    if(listener != null) {
      listener.onExternalResourcesChange(this);
    }

    for (OnExternalResourcesChangeListener listener : listeners) {
      if (listener != null) {
        listener.onExternalResourcesChange(this);
      }
    }
  }

  public static ExternalResources initialize(@NonNull Context context, Url url) {
    if (singleton == null) {
      synchronized (ExternalResources.class) {
        if (singleton == null) {
          singleton = new Builder(context, url).build();
        }
      }
    }

    return singleton;
  }

  public static ExternalResources initialize(@NonNull Context context, String path) {
    if (singleton == null) {
      synchronized (ExternalResources.class) {
        if (singleton == null) {
          singleton = new Builder(context, path).build();
        }
      }
    }

    return singleton;
  }

  @SuppressWarnings("ConstantConditions")
  public static ExternalResources initialize(@NonNull ExternalResources externalResources) {
    if (externalResources == null) {
      throw new IllegalArgumentException("ExternalResources must not be null.");
    }
    synchronized (ExternalResources.class) {
      if (singleton != null) {
        throw new IllegalStateException("Singleton instance already exists.");
      }
      singleton = externalResources;
    }

    return singleton;
  }

  public static ExternalResources getInstance() {
    if (singleton == null) {
      throw new IllegalArgumentException("You should call initialize() before getInstance().");
    }

    return singleton;
  }

  @SuppressWarnings("ConstantConditions")
  public static class Builder {
    private final Context context;
    private final Url url;

    @Cache.Policy
    private int cachePolicy = Cache.POLICY_ALL;
    @Nullable
    private Resources defaultResources;
    @Nullable
    private OnExternalResourcesLoadListener listener;
    @Nullable
    private Options options;

    public Builder(@NonNull Context context, @NonNull Url url) {
      if (context == null) {
        throw new IllegalArgumentException("Context must not be null.");
      }

      if (url == null) {
        throw new IllegalArgumentException("Url must not be null.");
      }

      this.context = context.getApplicationContext();
      this.url = url;
    }

    public Builder(@NonNull Context context, @NonNull String path) {
      this(context, new DefaultUrl(path));
    }

    public Builder cachePolicy(@Cache.Policy int cachePolicy) {
      this.cachePolicy = cachePolicy;

      return this;
    }

    public Builder defaultResources(@NonNull Resources defaultResources) {
      if (defaultResources == null) {
        throw new IllegalArgumentException("Default resources must not be null.");
      }

      if (this.defaultResources != null) {
        throw new IllegalStateException("Default resources already set.");
      }

      this.defaultResources = defaultResources;

      return this;
    }

    public Builder listener(@NonNull OnExternalResourcesLoadListener listener) {
      if (listener == null) {
        throw new IllegalArgumentException("Listener must not be null.");
      }

      if (this.listener != null) {
        throw new IllegalStateException("Listener already set.");
      }

      this.listener = listener;

      return this;
    }

    public Builder options(@NonNull Options options) {
      if (options == null) {
        throw new IllegalArgumentException("Options must not be null.");
      }

      if (this.options != null) {
        throw new IllegalStateException("Options already set.");
      }

      this.options = options;

      return this;
    }

    public ExternalResources build() {
      if (defaultResources == null) {
        this.defaultResources = new Resources();
      }

      if (options == null) {
        this.options = Options.createDefault();
      }

      return new ExternalResources(context, url, cachePolicy, defaultResources, options, listener);
    }
  }
}
