package fr.prcaen.externalresources;

import android.content.Context;
import android.os.Build;

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import fr.prcaen.externalresources.converter.Converter;
import fr.prcaen.externalresources.converter.JsonConverter;
import fr.prcaen.externalresources.url.DefaultUrl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public final class DownloaderTest {
  private static final String BASE_URL = "http://test.com";
  private final Converter converter = new JsonConverter();
  private final Options options = Options.createDefault();
  private final MockWebServer server = new MockWebServer();

  @Mock
  private Context context;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(context.getApplicationContext()).thenReturn(RuntimeEnvironment.application);

    final String successJson = IOUtils.toString(getClass().getResourceAsStream("/test.json"), "UTF-8");

    server.setDispatcher(new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        if (request.getPath().startsWith("/success")) {
          return new MockResponse().setResponseCode(200).setBody(successJson);
        } else {
          return new MockResponse().setResponseCode(400);
        }
      }
    });
  }

  @Test
  public void testLoad() throws Exception {
    server.start();

    DefaultUrl url = new DefaultUrl(server.getUrl("/success").toString());
    Downloader downloader = new Downloader(context, converter, url, options);

    assertNotNull("From network", downloader.load(Cache.POLICY_ALL));

    server.shutdown();

    assertNotNull("From cache", downloader.load(Cache.POLICY_OFFLINE));
  }

  @Test(expected = Downloader.ResponseException.class)
  public void testLoadWithException() throws Exception {
    server.start();

    DefaultUrl url = new DefaultUrl(server.getUrl("/error").toString());
    Downloader downloader = new Downloader(context, converter, url, options);

    downloader.load(Cache.POLICY_NONE);

    server.shutdown();
  }

  @Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
  @Test
  public void testBuildUrlOnJellyBean() throws Exception {
    DefaultUrl url = new DefaultUrl(BASE_URL);
    Downloader downloader = new Downloader(context, converter, url, options);

    downloader.buildUrl();
    assertEquals("Url is complete", url.build(), "http://test.com?orientation=0&keyboard=0&touch_screen=0&font_scale=1.0&mcc=0&navigation_hidden=0&locale=fr_FR&smallest_screen_width_dp=0&screen_width_dp=0&keyboard_hidden=0&navigation=0&screen_layout=18&screen_height_dp=0&mnc=0&ui_mode=0&hard_keyboard_hidden=0");
  }

  @Config(sdk = Build.VERSION_CODES.LOLLIPOP)
  @Test
  public void testBuildUrlOnLollipop() throws Exception {
    DefaultUrl url = new DefaultUrl(BASE_URL);
    Downloader downloader = new Downloader(context, converter, url, options);

    downloader.buildUrl();
    assertEquals("Url is complete", url.build(), "http://test.com?orientation=0&keyboard=0&touch_screen=0&font_scale=1.0&mcc=0&navigation_hidden=0&locale=fr_FR&smallest_screen_width_dp=0&screen_width_dp=0&keyboard_hidden=0&navigation=0&screen_layout=82&screen_height_dp=0&mnc=0&ui_mode=0&hard_keyboard_hidden=0&density_dpi=160");
  }

  @SuppressWarnings("ThrowableInstanceNeverThrown")
  @Test
  public void testResponseException() {
    Downloader.ResponseException exception = new Downloader.ResponseException("test", Cache.POLICY_NONE, 404);

    assertEquals("Exception message", exception.getMessage(), "test");
    assertFalse("LocalCacheOnly", exception.isLocalCacheOnly());
    assertEquals("Exception response code", exception.getResponseCode(), 404);
  }
}