package fr.prcaen.externalresources;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import fr.prcaen.externalresources.converter.Converter;
import fr.prcaen.externalresources.converter.JsonConverter;
import fr.prcaen.externalresources.exception.ExternalResourceException;
import fr.prcaen.externalresources.exception.ResponseException;
import fr.prcaen.externalresources.url.DefaultUrl;
import java.util.Locale;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB_MR2;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static java.util.Locale.US;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class) @Config(manifest = "/src/main/AndroidManifest.xml", sdk = KITKAT)
public final class DownloaderTest {
  private static final String BASE_URL = "http://test.com";

  private static final int ORIENTATION = 0;
  private static final int KEYBOARD = 0;
  private static final int TOUCH_SCREEN = 0;
  private static final int MCC = 0;
  private static final int NAVIGATION_HIDDEN = 0;
  private static final int SMALLEST_SCREEN_WIDTH_DP = 0;
  private static final int SCREEN_WIDTH_DP = 0;
  private static final int KEYBOARD_HIDDEN = 0;
  private static final int NAVIGATION = 0;
  private static final int SCREEN_LAYOUT = 82;
  private static final int SCREEN_HEIGHT_DP = 0;
  private static final int MNC = 0;
  private static final int UI_MODE = 0;
  private static final int HARD_KEYBOARD_HIDDEN = 0;
  private static final int DENSITY_DPI = 160;

  private static final float FONT_SCALE = 1.0f;

  private static final Locale LOCALE = US;

  private final Converter converter = new JsonConverter();
  private final Options options = Options.createDefault();
  private final MockWebServer server = new MockWebServer();

  @Mock private Context context;

  @Mock private Resources resources;

  @Mock private Configuration configuration;

  @Before public void setUp() throws Exception {
    initMocks(this);

    setDefaultConfiguration();
    when(context.getApplicationContext()).thenReturn(RuntimeEnvironment.application);

    final String successJson =
        IOUtils.toString(getClass().getResourceAsStream("/test.json"), "UTF-8");

    server.setDispatcher(new Dispatcher() {
      @Override public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        if (request.getPath().startsWith("/success")) {
          return new MockResponse().setResponseCode(200).setBody(successJson);
        } else {
          return new MockResponse().setResponseCode(400);
        }
      }
    });
  }

  @Test public void testLoad() throws Exception {
    server.start();

    DefaultUrl url = new DefaultUrl(server.url("/success").toString());
    Downloader downloader = new Downloader(context, converter, url, options);

    assertNotNull("From network", downloader.load(Cache.POLICY_ALL));

    server.shutdown();

    assertNotNull("From cache", downloader.load(Cache.POLICY_OFFLINE));
  }

  @Test(expected = ExternalResourceException.class) public void testLoadWithException()
      throws Exception {
    server.start();

    DefaultUrl url = new DefaultUrl(server.url("/error").toString());
    Downloader downloader = new Downloader(context, converter, url, options);

    downloader.load(Cache.POLICY_NONE);

    server.shutdown();
  }

  @Config(sdk = JELLY_BEAN) @Test public void testBuildUrlOnJellyBean() throws Exception {
    setDefaultConfiguration();
    DefaultUrl url = new DefaultUrl(BASE_URL);
    Downloader downloader = new Downloader(context, converter, url, options);

    downloader.buildUrl();
    String urlString = url.build();
    assertThat("Contains orientation", urlString, containsString("orientation=" + ORIENTATION));
    assertThat("Contains keyboard", urlString, containsString("keyboard=" + KEYBOARD));
    assertThat("Contains touch_screen", urlString, containsString("touch_screen=" + TOUCH_SCREEN));
    assertThat("Contains font_scale", urlString, containsString("font_scale=" + FONT_SCALE));
    assertThat("Contains mcc", urlString, containsString("mcc=" + MCC));
    assertThat("Contains navigation_hidden", urlString,
        containsString("navigation_hidden=" + NAVIGATION_HIDDEN));
    assertThat("Contains locale", urlString, containsString("locale=" + LOCALE));
    assertThat("Contains smallest_screen_width_dp", urlString,
        containsString("smallest_screen_width_dp=" + SMALLEST_SCREEN_WIDTH_DP));
    assertThat("Contains keyboard_hidden", urlString,
        containsString("keyboard_hidden=" + KEYBOARD_HIDDEN));
    assertThat("Contains navigation", urlString, containsString("navigation=" + NAVIGATION));
    assertThat("Contains screen_layout", urlString,
        containsString("screen_layout=" + SCREEN_LAYOUT));
    assertThat("Contains screen_height_dp", urlString,
        containsString("screen_height_dp=" + SCREEN_HEIGHT_DP));
    assertThat("Contains mnc", urlString, containsString("mnc=" + MNC));
    assertThat("Contains ui_mode", urlString, containsString("ui_mode=" + UI_MODE));
    assertThat("Contains hard_keyboard_hidden", urlString,
        containsString("hard_keyboard_hidden=" + HARD_KEYBOARD_HIDDEN));
  }

  @Config(sdk = LOLLIPOP) @Test public void testBuildUrlOnLollipop() throws Exception {
    setDefaultConfiguration();
    DefaultUrl url = new DefaultUrl(BASE_URL);
    Downloader downloader = new Downloader(context, converter, url, options);

    downloader.buildUrl();
    String urlString = url.build();

    assertThat("Contains orientation", urlString, containsString("orientation=" + ORIENTATION));
    assertThat("Contains keyboard", urlString, containsString("keyboard=" + KEYBOARD));
    assertThat("Contains touch_screen", urlString, containsString("touch_screen=" + TOUCH_SCREEN));
    assertThat("Contains font_scale", urlString, containsString("font_scale=" + FONT_SCALE));
    assertThat("Contains mcc", urlString, containsString("mcc=" + MCC));
    assertThat("Contains navigation_hidden", urlString,
        containsString("navigation_hidden=" + NAVIGATION_HIDDEN));
    assertThat("Contains locale", urlString, containsString("locale=" + LOCALE));
    assertThat("Contains smallest_screen_width_dp", urlString,
        containsString("smallest_screen_width_dp=" + SMALLEST_SCREEN_WIDTH_DP));
    assertThat("Contains keyboard_hidden", urlString,
        containsString("keyboard_hidden=" + KEYBOARD_HIDDEN));
    assertThat("Contains navigation", urlString, containsString("navigation=" + NAVIGATION));
    assertThat("Contains screen_layout", urlString,
        containsString("screen_layout=" + SCREEN_LAYOUT));
    assertThat("Contains screen_height_dp", urlString,
        containsString("screen_height_dp=" + SCREEN_HEIGHT_DP));
    assertThat("Contains mnc", urlString, containsString("mnc=" + MNC));
    assertThat("Contains ui_mode", urlString, containsString("ui_mode=" + UI_MODE));
    assertThat("Contains hard_keyboard_hidden", urlString,
        containsString("hard_keyboard_hidden=" + HARD_KEYBOARD_HIDDEN));
    assertThat("Contains density_dpi", urlString, containsString("density_dpi=" + DENSITY_DPI));
  }

  @SuppressWarnings("ThrowableInstanceNeverThrown") @Test public void testResponseException() {
    ResponseException exception = new ResponseException("test", Cache.POLICY_NONE, 404);

    assertEquals("Exception message", exception.getMessage(), "test");
    assertFalse("LocalCacheOnly", exception.isLocalCacheOnly());
    assertEquals("Exception response code", exception.getResponseCode(), 404);
  }

  private void setDefaultConfiguration() {
    Configuration configuration = RuntimeEnvironment.application.getResources().getConfiguration();

    configuration.orientation = ORIENTATION;
    configuration.keyboard = KEYBOARD;
    configuration.touchscreen = TOUCH_SCREEN;
    configuration.mcc = MCC;
    configuration.navigationHidden = NAVIGATION_HIDDEN;
    configuration.keyboardHidden = KEYBOARD_HIDDEN;
    configuration.navigation = NAVIGATION;
    configuration.screenLayout = SCREEN_LAYOUT;
    configuration.mnc = MNC;
    configuration.uiMode = UI_MODE;
    configuration.hardKeyboardHidden = HARD_KEYBOARD_HIDDEN;
    configuration.fontScale = FONT_SCALE;
    configuration.locale = LOCALE;

    if (SDK_INT >= HONEYCOMB_MR2) {
      configuration.smallestScreenWidthDp = SMALLEST_SCREEN_WIDTH_DP;
      configuration.screenWidthDp = SCREEN_WIDTH_DP;
      configuration.screenHeightDp = SCREEN_HEIGHT_DP;
    }

    if (SDK_INT >= JELLY_BEAN_MR1) {
      configuration.densityDpi = DENSITY_DPI;
    }
  }
}