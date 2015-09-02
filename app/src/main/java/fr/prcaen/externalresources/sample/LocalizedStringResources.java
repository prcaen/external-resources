package fr.prcaen.externalresources.sample;

import android.content.Context;
import android.content.res.Configuration;

import java.io.IOException;
import java.util.ArrayList;

import fr.prcaen.externalresources.ExternalResources;
import fr.prcaen.externalresources.ExternalResources.NotFoundException;
import fr.prcaen.externalresources.Logger;
import fr.prcaen.externalresources.Options;
import fr.prcaen.externalresources.listener.OnExternalResourcesLoadListener;
import fr.prcaen.externalresources.model.Resources;
import fr.prcaen.externalresources.url.LocalizedUrl;

public final class LocalizedStringResources implements OnExternalResourcesLoadListener {
  private static volatile LocalizedStringResources singleton = null;

  private final ExternalResources resources;
  private final ArrayList<Listener> listeners = new ArrayList<>();

  private LocalizedStringResources(Context context) {
    Options.Builder optionsBuilder = new Options.Builder();

    optionsBuilder.setUseFontScale(false);
    optionsBuilder.setUseHardKeyboardHidden(false);
    optionsBuilder.setUseKeyboard(false);
    optionsBuilder.setUseLocale(true);
    optionsBuilder.setUseKeyboardHidden(false);
    optionsBuilder.setUseKeyboardHidden(false);
    optionsBuilder.setUseMcc(false);
    optionsBuilder.setUseMnc(false);
    optionsBuilder.setUseNavigation(false);
    optionsBuilder.setUseNavigationHidden(false);
    optionsBuilder.setUseOrientation(false);
    optionsBuilder.setUseScreenLayout(false);
    optionsBuilder.setUseTouchscreen(false);
    optionsBuilder.setUseUiMode(false);
    optionsBuilder.setUseDensityDpi(false);
    optionsBuilder.setUseScreenWidthDp(false);
    optionsBuilder.setUseScreenHeightDp(false);
    optionsBuilder.setUseSmallestScreenWidthDp(false);
    optionsBuilder.setUseSmallestScreenWidthDp(false);

    Resources defaultResources = null;
    try {
      defaultResources = Resources.fromJson(context.getApplicationContext().getAssets().open("defaults.json"));
    } catch (IOException ignored) {
    }

    ExternalResources.Builder builder = new ExternalResources.Builder(context, new LocalizedStringResourcesUrl("http://d1dcnpnaiymuxd.cloudfront.net/translation/android/"));

    builder.options(optionsBuilder.build());
    builder.listener(this);
    builder.logLevel(Logger.LEVEL_VERBOSE);

    if (defaultResources != null) {
      builder.defaultResources(defaultResources);
    }

    this.resources = builder.build();
  }

  public void onConfigurationChanged(Configuration newConfig) {
    resources.onConfigurationChanged(newConfig);
  }

  public void register(Listener listener) {
    listeners.add(listener);
  }

  public void unregister(Listener listener) {
    listeners.remove(listener);
  }

  public String getString(String key) throws NotFoundException {
    return resources.getString(key);
  }

  public String getString(String key, Object... formatArgs) throws NotFoundException {
    return resources.getString(key, formatArgs);
  }

  @Override
  public void onExternalResourcesLoadFailed(Exception exception) {

  }

  @Override
  public void onExternalResourcesChange(ExternalResources externalResources) {
    for (Listener listener : listeners) {
      if(listener != null) {
        listener.onLocalizedStringResourcesChange(this);
      }
    }
  }

  public static LocalizedStringResources initialize(Context context) {
    if (singleton == null) {
      synchronized (LocalizedStringResources.class) {
        if (singleton == null) {
          singleton = new LocalizedStringResources(context);
        }
      }
    }

    return singleton;
  }

  public static LocalizedStringResources getInstance() {
    if (singleton == null) {
      throw new IllegalArgumentException("You should call initialize() before getInstance().");
    }

    return singleton;
  }

  private class LocalizedStringResourcesUrl extends LocalizedUrl {
    public LocalizedStringResourcesUrl(String baseUrl) {
      super(baseUrl);
    }

    @Override
    public String build() {
      final String localeStr = locale.getLanguage();

      return baseURL + "strings-" + localeStr + ".json";
    }
  }

  public interface Listener {
    void onLocalizedStringResourcesChange(LocalizedStringResources localizedStringResources);
  }
}
