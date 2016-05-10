package fr.prcaen.externalresources;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.AnyRes;
import android.support.annotation.ArrayRes;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;

import java.util.ArrayList;

import fr.prcaen.externalresources.converter.Converter;
import fr.prcaen.externalresources.converter.JsonConverter;
import fr.prcaen.externalresources.exception.ExternalResourceException;
import fr.prcaen.externalresources.exception.NotFoundException;
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

  protected static volatile ExternalResources singleton = null;

  @NonNull
  private final Context context;
  @NonNull
  private final DisplayMetrics metrics;
  @NonNull
  private final Dispatcher dispatcher;
  @NonNull
  private final Options options;
  @Nullable
  private final OnExternalResourcesLoadListener listener;
  @NonNull
  protected final ArrayList<OnExternalResourcesChangeListener> listeners = new ArrayList<>();
  private final boolean useApplicationResources;
  @NonNull
  private Resources resources;
  @NonNull
  private Configuration configuration;

  private ExternalResources(@NonNull Context context, @NonNull Converter converter, @NonNull Url url, @NonNull Options options, @Cache.Policy int cachePolicy, @Logger.Level int logLevel, @NonNull Resources defaultResources, @Nullable OnExternalResourcesLoadListener listener, boolean useApplicationResources) {
    Logger.setLevel(logLevel);

    this.context = context;
    this.dispatcher = new Dispatcher(context, new Downloader(context, converter, url, options), new ExternalResourcesHandler(this), cachePolicy);
    this.configuration = new Configuration(context.getResources().getConfiguration());
    this.metrics = context.getResources().getDisplayMetrics();
    this.resources = defaultResources;
    this.options = options;
    this.listener = listener;
    this.useApplicationResources = useApplicationResources;

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

  public boolean getBoolean(@BoolRes int resId) throws NotFoundException {
    String key = getApplicationResourceEntryName(resId);

    if (null == key) {
      throw new NotFoundException("Boolean resource with resId: " + resId);
    }

    return getBoolean(key);
  }

  public boolean getBoolean(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (null != resource) {
      return resource.getAsBoolean();
    }

    @BoolRes int resId = getApplicationResourceIdentifier(key, "bool");

    if (0 != resId) {
      boolean value = context.getResources().getBoolean(resId);
      resources.add(key, new Resource(value));

      return value;
    }

    throw new NotFoundException("Boolean resource with key: " + key);
  }

  @ColorInt
  public int getColor(@ColorRes int resId) throws NotFoundException {
    String key = getApplicationResourceEntryName(resId);

    if (null == key) {
      throw new NotFoundException("Color resource with resId: " + resId);
    }

    Resource resource = resources.get(key);
    if (null != resource && null != resource.getAsString()) {
      return Color.parseColor(resource.getAsString());
    }

    int value = Utils.getColor(context, resId);
    resources.add(key, new Resource(value));

    return value;
  }

  @ColorInt
  public int getColor(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (null != resource) {
      try {
        return Color.parseColor(resource.getAsString());
      } catch (IllegalArgumentException ignored) {
      }
    }

    @ColorRes int resId = getApplicationResourceIdentifier(key, "color");

    if (0 != resId) {
      return Utils.getColor(context, resId);
    }

    throw new NotFoundException("Color resource with key: " + key);
  }

  public float getDimension(@DimenRes int resId) throws NotFoundException {
    String key = getApplicationResourceEntryName(resId);

    if (null == key) {
      throw new NotFoundException("Dimension resource with resId: " + resId);
    }

    Resource resource = resources.get(key);
    if (null != resource && null != resource.getAsString()) {
      return DimensionResource.fromString(resource.getAsString()).toFloat(metrics);
    }

    return context.getResources().getDimension(resId);
  }

  public float getDimension(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (null != resource && null != resource.getAsString()) {
      try {
        return DimensionResource.fromString(resource.getAsString()).toFloat(metrics);
      } catch (IllegalArgumentException ignored) {
      }
    }

    @DimenRes int resId = getApplicationResourceIdentifier(key, "dimen");

    if (0 != resId) {
      float value = context.getResources().getDimension(resId);
      resources.add(key, new Resource(value));

      return value;
    }

    throw new NotFoundException("String resource with key: " + key);
  }

  public String getString(@StringRes int resId) throws NotFoundException {
    String key = getApplicationResourceEntryName(resId);

    if (null == key) {
      throw new NotFoundException("String resource with resId: " + resId);
    }

    return getString(key);
  }

  public String getString(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (null != resource) {
      return resource.getAsString();
    }

    @StringRes int resId = getApplicationResourceIdentifier(key, "string");

    if (0 != resId) {
      String value = context.getResources().getString(resId);
      resources.add(key, new Resource(value));

      return value;
    }

    throw new NotFoundException("String resource with key: " + key);
  }

  public String getString(@StringRes int resId, Object... formatArgs) throws NotFoundException {
    String key = getApplicationResourceEntryName(resId);
    if (null == key) {
      throw new NotFoundException("String resource with resId: " + resId);
    }
    return getString(key, formatArgs);
  }

  public String getString(@NonNull String key, Object... formatArgs) throws NotFoundException {
    String raw = getString(key);
    return String.format(configuration.locale, raw, formatArgs);
  }

  public String[] getStringArray(@ArrayRes int resId) throws NotFoundException {
    String key = getApplicationResourceEntryName(resId);

    if (null == key) {
      throw new NotFoundException("String array resource with resId: " + resId);
    }

    return getStringArray(key);
  }

  public String[] getStringArray(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (null != resource) {
      return resource.getAsStringArray();
    }

    @ArrayRes int resId = getApplicationResourceIdentifier(key, "array");

    if (0 != resId) {
      String[] values = context.getResources().getStringArray(resId);
      Resource[] stringResources = new Resource[values.length];

      for (int i = 0; i < values.length; i++) {
        stringResources[i] = new Resource(values[i]);
      }

      resources.add(key, new Resource(stringResources));

      return values;
    }

    throw new NotFoundException("String array resource with key: " + key);
  }

  public int getInteger(@IntegerRes int resId) throws NotFoundException {
    String key = getApplicationResourceEntryName(resId);

    if (null != key) {
      Resource resource = resources.get(key);
      if (null != resource && null != resource.getAsInt()) {
        return resource.getAsInt();
      }
    }

    throw new NotFoundException("Integer resource with resId: " + resId);
  }

  public int getInteger(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (null != resource && null != resource.getAsInt()) {
      return resource.getAsInt();
    }

    @IntegerRes int resId = getApplicationResourceIdentifier(key, "integer");

    if (0 != resId) {
      int value = context.getResources().getInteger(resId);
      resources.add(key, new Resource(value));

      return value;
    }

    throw new NotFoundException("Integer resource with key: " + key);
  }

  public int[] getIntArray(@ArrayRes int resId) throws NotFoundException {
    String key = getApplicationResourceEntryName(resId);

    if (null == key) {
      throw new NotFoundException("Int array resource with resId: " + resId);
    }

    return getIntArray(key);
  }

  public int[] getIntArray(@NonNull String key) throws NotFoundException {
    Resource resource = resources.get(key);
    if (null != resource && null != resource.getAsIntegerArray()) {
      return resource.getAsIntegerArray();
    }

    @ArrayRes int resId = getApplicationResourceIdentifier(key, "array");

    if (0 != resId) {
      int[] values = context.getResources().getIntArray(resId);

      Resource[] intResources = new Resource[values.length];

      for (int i = 0; i < values.length; i++) {
        intResources[i] = new Resource(values[i]);
      }

      resources.add(key, new Resource(intResources));

      return values;
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

  public void shutdown() {
    dispatcher.stop();
  }

  public void onResourcesLoadSuccess(Resources resources) {
    Logger.i(TAG, "onResourcesLoadSuccess");
    this.resources.merge(resources);

    triggerChange();
  }

  public void onResourcesLoadFailed(ExternalResourceException exception) {
    Logger.e(TAG, "onResourcesLoadFailed", exception);

    if (null != listener) {
      listener.onExternalResourcesLoadFailed(exception);
    }
  }

  private void launch() {
    Logger.v(TAG, "Launch");

    dispatcher.dispatchLaunch();
  }

  @Nullable
  private String getApplicationResourceEntryName(@AnyRes int resId) throws IllegalStateException {
    if(!useApplicationResources) {
      throw new IllegalStateException("You have set the useApplicationResources to false, using application resource is an error.");
    }

    return Utils.getAndroidResourceEntryName(context, resId);
  }

  @IdRes
  private int getApplicationResourceIdentifier(String key, String defType) {
    return useApplicationResources ? Utils.getAndroidResourceIdentifier(context, key, defType) : 0;
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
    if (null != listener) {
      listener.onExternalResourcesChange(this);
    }

    for (OnExternalResourcesChangeListener listener : listeners) {
      if (null != listener) {
        Logger.v(TAG, "Trigger change for listener: " + listener.getClass().getSimpleName());
        listener.onExternalResourcesChange(this);
      }
    }
  }

  @SuppressWarnings("ConstantConditions")
  public static ExternalResources initialize(@NonNull Context context, Url url) {
    if (null == context) {
      throw new IllegalArgumentException("Context must not be null.");
    }

    if (null == url) {
      throw new IllegalArgumentException("Path must not be null.");
    }

    synchronized (ExternalResources.class) {
      if (null != singleton) {
        throw new IllegalStateException("Singleton instance already exists.");
      }

      singleton = new Builder(context, url).build();
    }

    return singleton;
  }

  @SuppressWarnings("ConstantConditions")
  public static ExternalResources initialize(@NonNull Context context, @NonNull String path) {
    if (null == context) {
      throw new IllegalArgumentException("Context must not be null.");
    }

    if (null == path) {
      throw new IllegalArgumentException("Path must not be null.");
    }

    synchronized (ExternalResources.class) {
      if (null != singleton) {
        throw new IllegalStateException("Singleton instance already exists.");
      }

      singleton = new Builder(context, path).build();
    }

    return singleton;
  }

  @SuppressWarnings("ConstantConditions")
  public static ExternalResources initialize(@NonNull Context context, Url url, Resources resources) {
    if (null == context) {
      throw new IllegalArgumentException("Context must not be null.");
    }

    if (null == url) {
      throw new IllegalArgumentException("Path must not be null.");
    }

    synchronized (ExternalResources.class) {
      if (null != singleton) {
        throw new IllegalStateException("Singleton instance already exists.");
      }

      singleton = new Builder(context, url).build();
    }

    return singleton;
  }

  @SuppressWarnings("ConstantConditions")
  public static ExternalResources initialize(@NonNull Context context, @NonNull String path, Resources defaultResources) {
    if (null == context) {
      throw new IllegalArgumentException("Context must not be null.");
    }

    if (null == path) {
      throw new IllegalArgumentException("Path must not be null.");
    }

    synchronized (ExternalResources.class) {
      if (null != singleton) {
        throw new IllegalStateException("Singleton instance already exists.");
      }

      singleton = new Builder(context, path).defaultResources(defaultResources).build();
    }

    return singleton;
  }

  @SuppressWarnings("ConstantConditions")
  public static ExternalResources initialize(@NonNull ExternalResources externalResources) {
    if (null == externalResources) {
      throw new IllegalArgumentException("ExternalResources must not be null.");
    }
    synchronized (ExternalResources.class) {
      if (null != singleton) {
        throw new IllegalStateException("Singleton instance already exists.");
      }

      singleton = externalResources;
    }

    return singleton;
  }

  public static ExternalResources getInstance() {
    if (null == singleton) {
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
    private boolean useApplicationResources = true;

    public Builder(@NonNull Context context, @NonNull Url url) {
      if (null == context) {
        throw new IllegalArgumentException("Context must not be null.");
      }

      if (null == url) {
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
      if (null == defaultResources) {
        throw new IllegalArgumentException("Default resources must not be null.");
      }

      if (null != this.defaultResources) {
        throw new IllegalStateException("Default resources already set.");
      }

      this.defaultResources = defaultResources;

      return this;
    }

    public Builder listener(@NonNull OnExternalResourcesLoadListener listener) {
      if (null == listener) {
        throw new IllegalArgumentException("Listener must not be null.");
      }

      if (null != this.listener) {
        throw new IllegalStateException("Listener already set.");
      }

      this.listener = listener;

      return this;
    }

    public Builder options(@NonNull Options options) {
      if (null == options) {
        throw new IllegalArgumentException("Options must not be null.");
      }

      if (null != this.options) {
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
      if (null == converter) {
        throw new IllegalArgumentException("Converter must not be null.");
      }

      if (null != this.converter) {
        throw new IllegalStateException("Converter already set.");
      }

      this.converter = converter;

      return this;
    }

    public Builder useApplicationResources(boolean useApplicationResources) {
      this.useApplicationResources = useApplicationResources;

      return this;
    }

    public ExternalResources build() {
      if (null == defaultResources) {
        this.defaultResources = new Resources();
      }

      if (null == options) {
        this.options = Options.createDefault();
      }

      if (null == converter) {
        this.converter = new JsonConverter();
      }

      return new ExternalResources(context, converter, url, options, cachePolicy, logLevel, defaultResources, listener, useApplicationResources);
    }
  }

  private static class ExternalResourcesHandler extends Handler {

    private final ExternalResources externalResources;

    public ExternalResourcesHandler(ExternalResources externalResources) {
      super(Looper.getMainLooper());

      this.externalResources = externalResources;
    }

    @Override
    public void handleMessage(Message message) {
      super.handleMessage(message);

      switch (message.what) {
        case Dispatcher.REQUEST_DONE:
          externalResources.onResourcesLoadSuccess((Resources) message.obj);
          break;
        case Dispatcher.REQUEST_FAILED:
          externalResources.onResourcesLoadFailed((ExternalResourceException) message.obj);
          break;
        default:
          Logger.e(ExternalResources.TAG, "Unknown message: " + message.what);
          break;
      }

    }

  }

}
