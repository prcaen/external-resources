package fr.prcaen.externalresources.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainSampleActivity extends AppCompatActivity implements LocalizedStringResources.Listener {

  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    LocalizedStringResources.getInstance().register(this);

    textView = (TextView) findViewById(R.id.text);
    textView.setText(LocalizedStringResources.getInstance().getString("error_bad_request"));
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
    textView.setText(localizedStringResources.getString("error_bad_request"));
  }

}
