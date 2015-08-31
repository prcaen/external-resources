package fr.prcaen.externalresources.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainSampleActivity extends AppCompatActivity implements LocalizedStringResources.Listener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }


  @Override
  protected void onResume() {
    super.onResume();

    LocalizedStringResources.getInstance().register(this);
  }

  @Override
  protected void onPause() {
    super.onPause();

    LocalizedStringResources.getInstance().unregister(this);
  }

  @Override
  public void onLocalizedStringResourcesChange(LocalizedStringResources localizedStringResources) {
  }
}
