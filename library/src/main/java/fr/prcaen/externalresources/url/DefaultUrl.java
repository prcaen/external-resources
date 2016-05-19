package fr.prcaen.externalresources.url;

import android.net.Uri;
import android.support.annotation.NonNull;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefaultUrl implements Url {
  protected static final String QUERY_PARAMETER_DENSITY_DPI = "density_dpi";
  protected static final String QUERY_PARAMETER_FONT_SCALE = "font_scale";
  protected static final String QUERY_PARAMETER_HARD_KEYBOARD_HIDDEN = "hard_keyboard_hidden";
  protected static final String QUERY_PARAMETER_KEYBOARD = "keyboard";
  protected static final String QUERY_PARAMETER_KEYBOARD_HIDDEN = "keyboard_hidden";
  protected static final String QUERY_PARAMETER_LOCALE = "locale";
  protected static final String QUERY_PARAMETER_MCC = "mcc";
  protected static final String QUERY_PARAMETER_MNC = "mnc";
  protected static final String QUERY_PARAMETER_NAVIGATION = "navigation";
  protected static final String QUERY_PARAMETER_NAVIGATION_HIDDEN = "navigation_hidden";
  protected static final String QUERY_PARAMETER_ORIENTATION = "orientation";
  protected static final String QUERY_PARAMETER_SCREEN_HEIGHT_DP = "screen_height_dp";
  protected static final String QUERY_PARAMETER_SCREEN_LAYOUT = "screen_layout";
  protected static final String QUERY_PARAMETER_SCREEN_WIDTH_DP = "screen_width_dp";
  protected static final String QUERY_PARAMETER_SMALLEST_SCREEN_WIDTH_DP =
      "smallest_screen_width_dp";
  protected static final String QUERY_PARAMETER_TOUCHSCREEN = "touch_screen";
  protected static final String QUERY_PARAMETER_UI_MODE = "ui_mode";

  protected final String baseURL;
  protected final HashMap<String, String> parameters = new HashMap<>();

  public DefaultUrl(@NonNull String baseUrl) {
    this.baseURL = baseUrl;
  }

  @Override public void fontScale(float fontScale) {
    parameters.put(QUERY_PARAMETER_FONT_SCALE, String.valueOf(fontScale));
  }

  @Override public void hardKeyboardHidden(int hardKeyboardHidden) {
    parameters.put(QUERY_PARAMETER_HARD_KEYBOARD_HIDDEN, String.valueOf(hardKeyboardHidden));
  }

  @Override public void keyboard(int keyboard) {
    parameters.put(QUERY_PARAMETER_KEYBOARD, String.valueOf(keyboard));
  }

  @Override public void keyboardHidden(int keyboardHidden) {
    parameters.put(QUERY_PARAMETER_KEYBOARD_HIDDEN, String.valueOf(keyboardHidden));
  }

  @Override public void locale(Locale locale) {
    parameters.put(QUERY_PARAMETER_LOCALE, locale.toString());
  }

  @Override public void mcc(int mcc) {
    parameters.put(QUERY_PARAMETER_MCC, String.valueOf(mcc));
  }

  @Override public void mnc(int mnc) {
    parameters.put(QUERY_PARAMETER_MNC, String.valueOf(mnc));
  }

  @Override public void navigation(int navigation) {
    parameters.put(QUERY_PARAMETER_NAVIGATION, String.valueOf(navigation));
  }

  @Override public void navigationHidden(int navigationHidden) {
    parameters.put(QUERY_PARAMETER_NAVIGATION_HIDDEN, String.valueOf(navigationHidden));
  }

  @Override public void orientation(int orientation) {
    parameters.put(QUERY_PARAMETER_ORIENTATION, String.valueOf(orientation));
  }

  @Override public void screenLayout(int screenLayout) {
    parameters.put(QUERY_PARAMETER_SCREEN_LAYOUT, String.valueOf(screenLayout));
  }

  @Override public void touchscreen(int touchscreen) {
    parameters.put(QUERY_PARAMETER_TOUCHSCREEN, String.valueOf(touchscreen));
  }

  @Override public void uiMode(int uiMode) {
    parameters.put(QUERY_PARAMETER_UI_MODE, String.valueOf(uiMode));
  }

  @Override public void densityDpi(int densityDpi) {
    parameters.put(QUERY_PARAMETER_DENSITY_DPI, String.valueOf(densityDpi));
  }

  @Override public void screenWidthDp(int screenWidthDp) {
    parameters.put(QUERY_PARAMETER_SCREEN_WIDTH_DP, String.valueOf(screenWidthDp));
  }

  @Override public void screenHeightDp(int screenHeightDp) {
    parameters.put(QUERY_PARAMETER_SCREEN_HEIGHT_DP, String.valueOf(screenHeightDp));
  }

  @Override public void smallestScreenWidthDp(int smallestScreenWidthDp) {
    parameters.put(QUERY_PARAMETER_SMALLEST_SCREEN_WIDTH_DP, String.valueOf(smallestScreenWidthDp));
  }

  @Override public String build() {
    Uri.Builder builder = Uri.parse(baseURL).buildUpon();

    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      builder.appendQueryParameter(entry.getKey(), entry.getValue());
    }

    return builder.build().toString();
  }
}
