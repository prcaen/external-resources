package fr.prcaen.externalresources;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.prcaen.externalresources.converter.Converter;
import fr.prcaen.externalresources.converter.JsonConverter;
import fr.prcaen.externalresources.listener.OnExternalResourcesChangeListener;
import fr.prcaen.externalresources.listener.OnExternalResourcesLoadListener;
import fr.prcaen.externalresources.model.DimensionResource;
import fr.prcaen.externalresources.model.Resource;
import fr.prcaen.externalresources.model.Resources;
import fr.prcaen.externalresources.url.DefaultUrl;
import fr.prcaen.externalresources.url.Url;

@SuppressWarnings("UnusedDeclaration")
public class ExternalResources {
  public static final String TAG = "ExternalResources";

  private static final String THREAD_NAME = "ExternalResourcesThread";
  private static final String EXCEPTION_NOT_LOADED = "Resources are null.";

  protected static volatile ExternalResources singleton = null;

  @NonNull
  private final DisplayMetrics metrics;
  @NonNull
  private final Downloader downloader;
  @NonNull
  private final Resources defaultResources;
  @NonNull
  private final Options options;
  @Cache.Policy
  private final int policy;
  @Nullable
  private final OnExternalResourcesLoadListener listener;
  @NonNull
  protected final ArrayList<OnExternalResourcesChangeListener> listeners = new ArrayList<>();
  @NonNull
  private Resources resources;
  @NonNull
  private Configuration configuration;

  private ExternalResources(@NonNull Context context, @NonNull Converter converter, @NonNull Url url, @NonNull Options options, @Cache.Policy int policy, @Logger.Level int logLevel, @NonNull Resources defaultResources, @Nullable OnExternalResourcesLoadListener listener) {
    Logger.setLevel(logLevel);

    this.configuration = new Configuration(context.getResources().getConfiguration());
    this.metrics = context.getResources().getDisplayMetrics();
    this.downloader = new Downloader(context, converter, url, options);
    this.policy = policy;
    this.defaultResources = defaultResources;
    this.resources = defaultResources;
    this.options = options;
    this.listener = listener;

    launch();
  }

  public void onConfigurationChanged(Configuration newConfig) {
    Logger.d(TAG, "onConfigurationChanged");

    if (shouldRelaunch(newConfig)) {
      Logger.v(TAG, "Relaunch");

      launch();
    }

    this.configuration = new Configuration(newConfig);
  }

  public void setLogLevel(@Logger.Level int logLevel) {
    Logger.setLevel(logLevel);
  }

  public boolean getBoolean(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (resource != null) {
      return resource.getAsBoolean();
    }

    throw new NotFoundException("Boolean resource with key: " + key);
  }

  @ColorInt
  public int getColor(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (resource != null && resource.getAsString() != null) {
      return Color.parseColor(resource.getAsString());
    }

    throw new NotFoundException("Color resource with key: " + key);
  }

  public float getDimension(@NonNull String key) throws NotFoundException {
    String raw = getString(key);

    try {
      return DimensionResource.fromString(raw).toFloat(metrics);
    } catch (IllegalArgumentException e) {
      throw new NotFoundException("Dimension resource with key: " + key);
    }
  }

  public String getString(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (resource != null) {
      return resource.getAsString();
    }

    throw new NotFoundException("String resource with key: " + key);
  }

  public String getString(@NonNull String key, Object... formatArgs) throws NotFoundException {
    String raw = getString(key);
    return String.format(configuration.locale, raw, formatArgs);
  }

  public String[] getStringArray(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (resource != null) {
      return resource.getAsStringArray();
    }

    throw new NotFoundException("String array resource with key: " + key);
  }

  public int getInteger(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (resource != null && resource.getAsInt() != null) {
      return resource.getAsInt();
    }

    throw new NotFoundException("Int resource with key: " + key);
  }

  public int[] getIntArray(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (resource != null) {
      return resource.getAsIntegerArray();
    }

    throw new NotFoundException("Int array resource with key: " + key);
  }

  public void register(OnExternalResourcesChangeListener listener) {
    Logger.v(TAG, "Register listener:" + listener.getClass().getSimpleName());
    listeners.add(listener);
  }

  public void unregister(OnExternalResourcesChangeListener listener) {
    Logger.v(TAG, "Unregister listener:" + listener.getClass().getSimpleName());
    listeners.remove(listener);
  }

  private void launch() {
    Logger.v(TAG, "Launch");
    ExecutorService pool = Executors.newSingleThreadExecutor();
    pool.submit(new ResourcesRunnable(downloader, policy, new ResourcesRunnable.Listener() {
      @Override
      public void onResourcesLoadSuccess(Resources resources) {
        Logger.i(TAG, "onResourcesLoadSuccess");
        ExternalResources.this.resources = defaultResources.merge(resources);

        triggerChange();
      }

      @Override
      public void onResourcesLoadFailed(Exception exception) {
        Logger.e(TAG, "onResourcesLoadFailed", exception);

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
    if (listener != null) {
      listener.onExternalResourcesChange(this);
    }

    for (OnExternalResourcesChangeListener listener : listeners) {
      if (listener != null) {
        Logger.v(TAG, "Trigger change for listener: " + listener.getClass().getSimpleName());
        listener.onExternalResourcesChange(this);
      }
    }
  }

  @SuppressWarnings("ConstantConditions")
  public static ExternalResources initialize(@NonNull Context context, Url url) {
    if (context == null) {
      throw new IllegalArgumentException("Context must not be null.");
    }

    if (url == null) {
      throw new IllegalArgumentException("Path must not be null.");
    }

    synchronized (ExternalResources.class) {
      if (singleton != null) {
        throw new IllegalStateException("Singleton instance already exists.");
      }

      singleton = new Builder(context, url).build();
    }

    return singleton;
  }

  @SuppressWarnings("ConstantConditions")
  public static ExternalResources initialize(@NonNull Context context, @NonNull String path) {
    if (context == null) {
      throw new IllegalArgumentException("Context must not be null.");
    }

    if (path == null) {
      throw new IllegalArgumentException("Path must not be null.");
    }

    synchronized (ExternalResources.class) {
      if (singleton != null) {
        throw new IllegalStateException("Singleton instance already exists.");
      }

      singleton = new Builder(context, path).build();
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
    @Logger.Level
    private int logLevel = Logger.LEVEL_ERROR;
    @Nullable
    private Resources defaultResources;
    @Nullable
    private OnExternalResourcesLoadListener listener;
    @Nullable
    private Options options;
    @Nullable
    private Converter converter;

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

    public Builder logLevel(@Logger.Level int logLevel) {
      this.logLevel = logLevel;

      return this;
    }

    public Builder converter(@NonNull Converter converter) {
      if (converter == null) {
        throw new IllegalArgumentException("Converter must not be null.");
      }

      if (this.converter != null) {
        throw new IllegalStateException("Converter already set.");
      }

      this.converter = converter;

      return this;
    }

    public ExternalResources build() {
      if (defaultResources == null) {
        this.defaultResources = new Resources();
      }

      if (options == null) {
        this.options = Options.createDefault();
      }

      if (converter == null) {
        this.converter = new JsonConverter();
      }

      return new ExternalResources(context, converter, url, options, cachePolicy, logLevel, defaultResources, listener);
    }
  }

  public static class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(String name) {
      super(name);
    }
  }
}
