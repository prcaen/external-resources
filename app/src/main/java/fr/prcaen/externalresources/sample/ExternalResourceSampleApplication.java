package fr.prcaen.externalresources.sample;

import android.app.Application;
import android.content.res.Configuration;

public class ExternalResourceSampleApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    LocalizedStringResources.initialize(this);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    LocalizedStringResources.getInstance().onConfigurationChanged(newConfig);
  }
}
