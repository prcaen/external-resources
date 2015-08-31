package fr.prcaen.externalresources.url;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.Locale;

public class LocalizedUrl implements Url {
  protected static final String QUERY_PARAMETER_LOCALE = "locale";

  protected final String baseURL;
  protected Locale locale = Locale.getDefault();

  public LocalizedUrl(@NonNull String baseURL) {
    this.baseURL = baseURL;
  }

  @Override
  public void fontScale(float fontScale) {

  }

  @Override
  public void hardKeyboardHidden(int hardKeyboardHidden) {

  }

  @Override
  public void keyboard(int keyboard) {

  }

  @Override
  public void keyboardHidden(int keyboardHidden) {

  }

  @Override
  public void locale(Locale locale) {
    this.locale = locale;
  }

  @Override
  public void mcc(int mcc) {

  }

  @Override
  public void mnc(int mnc) {

  }

  @Override
  public void navigation(int navigation) {

  }

  @Override
  public void navigationHidden(int navigationHidden) {

  }

  @Override
  public void orientation(int orientation) {

  }

  @Override
  public void screenLayout(int screenLayout) {

  }

  @Override
  public void touchscreen(int touchscreen) {

  }

  @Override
  public void uiMode(int uiMode) {

  }

  @Override
  public void densityDpi(int densityDpi) {

  }

  @Override
  public void screenWidthDp(int screenWidthDp) {

  }

  @Override
  public void screenHeightDp(int screenHeightDp) {

  }

  @Override
  public void smallestScreenWidthDp(int smallestScreenWidthDp) {

  }

  @Override
  public String build() {
    Uri.Builder builder = Uri.parse(baseURL).buildUpon();

    builder.appendQueryParameter(QUERY_PARAMETER_LOCALE, locale.toString());

    return builder.build().toString();
  }
}
