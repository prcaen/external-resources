package fr.prcaen.externalresources.url;

import java.util.Locale;

public interface Url {

  void fontScale(float fontScale);

  void hardKeyboardHidden(int hardKeyboardHidden);

  void keyboard(int keyboard);

  void keyboardHidden(int keyboardHidden);

  void locale(Locale locale);

  void mcc(int mcc);

  void mnc(int mnc);

  void navigation(int navigation);

  void navigationHidden(int navigationHidden);

  void orientation(int orientation);

  void screenLayout(int screenLayout);

  void touchscreen(int touchscreen);

  void uiMode(int uiMode);

  void densityDpi(int densityDpi);

  void screenWidthDp(int screenWidthDp);

  void screenHeightDp(int screenHeightDp);

  void smallestScreenWidthDp(int smallestScreenWidthDp);

  String build();
}
