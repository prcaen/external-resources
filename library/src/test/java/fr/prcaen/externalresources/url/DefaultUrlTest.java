package fr.prcaen.externalresources.url;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public final class DefaultUrlTest {
  private static final String BASE_URL = "http://test.com";
  private DefaultUrl url;

  @Test
  public void testFontScale() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.fontScale(2.0f);

    assertTrue("font_scale key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_FONT_SCALE));
    assertTrue("font_scale value exists", url.parameters.containsValue("2.0"));
  }

  @Test
  public void testHardKeyboardHidden() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.hardKeyboardHidden(1);

    assertTrue("hard_keyboard_hidden key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_HARD_KEYBOARD_HIDDEN));
    assertTrue("hard_keyboard_hidden value exists", url.parameters.containsValue("1"));
  }

  @Test
  public void testKeyboard() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.keyboard(2);

    assertTrue("keyboard key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_KEYBOARD));
    assertTrue("keyboard value exists", url.parameters.containsValue("2"));
  }

  @Test
  public void testKeyboardHidden() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.keyboardHidden(3);

    assertTrue("keyboard hidden key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_KEYBOARD_HIDDEN));
    assertTrue("keyboard hidden value exists", url.parameters.containsValue("3"));
  }

  @Test
  public void testLocale() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.locale(Locale.FRANCE);

    assertTrue("locale key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_LOCALE));
    assertTrue("locale value exists", url.parameters.containsValue("fr_FR"));
  }

  @Test
  public void testMcc() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.mcc(1);

    assertTrue("mcc key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_MCC));
    assertTrue("mcc value exists", url.parameters.containsValue("1"));
  }

  @Test
  public void testMnc() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.mnc(2);

    assertTrue("mnc key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_MNC));
    assertTrue("mnc value exists", url.parameters.containsValue("2"));
  }

  @Test
  public void testNavigation() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.navigation(3);

    assertTrue("navigation key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_NAVIGATION));
    assertTrue("navigation value exists", url.parameters.containsValue("3"));
  }

  @Test
  public void testNavigationHidden() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.navigationHidden(0);

    assertTrue("navigation_hidden key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_NAVIGATION_HIDDEN));
    assertTrue("navigation_hidden value exists", url.parameters.containsValue("0"));
  }

  @Test
  public void testOrientation() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.orientation(1);

    assertTrue("orientation key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_ORIENTATION));
    assertTrue("orientation value exists", url.parameters.containsValue("1"));
  }

  @Test
  public void testScreenLayout() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.screenLayout(2);

    assertTrue("screen_layout key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_SCREEN_LAYOUT));
    assertTrue("screen_layout value exists", url.parameters.containsValue("2"));
  }

  @Test
  public void testTouchscreen() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.touchscreen(3);

    assertTrue("touchscreen key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_TOUCHSCREEN));
    assertTrue("touchscreen value exists", url.parameters.containsValue("3"));
  }

  @Test
  public void testUiMode() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.uiMode(4);

    assertTrue("ui_mode key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_UI_MODE));
    assertTrue("ui_mode value exists", url.parameters.containsValue("4"));
  }

  @Test
  public void testDensityDpi() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.densityDpi(5);

    assertTrue("density_dpi key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_DENSITY_DPI));
    assertTrue("density_dpi value exists", url.parameters.containsValue("5"));
  }

  @Test
  public void testScreenWidthDp() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.screenWidthDp(6);

    assertTrue("screen_width_dp key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_SCREEN_WIDTH_DP));
    assertTrue("screen_width_dp value exists", url.parameters.containsValue("6"));
  }

  @Test
  public void testScreenHeightDp() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.screenHeightDp(7);

    assertTrue("screen_height_dp key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_SCREEN_HEIGHT_DP));
    assertTrue("screen_height_dp value exists", url.parameters.containsValue("7"));
  }

  @Test
  public void testSmallestScreenWidthDp() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.smallestScreenWidthDp(8);

    assertTrue("smallest_screen_width_dp key exists", url.parameters.containsKey(DefaultUrl.QUERY_PARAMETER_SMALLEST_SCREEN_WIDTH_DP));
    assertTrue("smallest_screen_width_dp value exists", url.parameters.containsValue("8"));
  }

  @Test
  public void testBuild() throws Exception {
    url = new DefaultUrl(BASE_URL);

    url.screenHeightDp(7);
    url.densityDpi(5);

    String urlString = url.build();
    assertThat("Contains density_dpi", urlString, containsString("density_dpi=5"));
    assertThat("Contains screen_height_dp", urlString, containsString("screen_height_dp=7"));
  }
}