package fr.prcaen.externalresources.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import fr.prcaen.externalresources.ExternalResources;
import fr.prcaen.externalresources.listener.OnExternalResourcesChangeListener;

public class MainSampleActivity extends AppCompatActivity implements OnExternalResourcesChangeListener {

  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    textView = (TextView) findViewById(R.id.text);
    textView.setText(ExternalResources.getInstance().getString(R.string.app_name));
  }

  @Override
  protected void onResume() {
    super.onResume();

    ExternalResources.getInstance().register(this);
  }

  @Override
  protected void onPause() {
    super.onPause();

    ExternalResources.getInstance().unregister(this);
  }

  @Override
  public void onExternalResourcesChange(ExternalResources externalResources) {
    textView.setText(externalResources.getString(R.string.app_name));
  }
}
