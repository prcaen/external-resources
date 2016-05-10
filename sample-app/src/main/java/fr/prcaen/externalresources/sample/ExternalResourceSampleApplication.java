package fr.prcaen.externalresources.sample;

import android.app.Application;
import android.content.res.Configuration;

import fr.prcaen.externalresources.ExternalResources;

public class ExternalResourceSampleApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    ExternalResources.initialize(this, "https://external-res.s3-eu-west-1.amazonaws.com/strings-en.json");
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    ExternalResources.getInstance().onConfigurationChanged(newConfig);
  }
}
