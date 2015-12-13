package fr.prcaen.externalresources;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import fr.prcaen.externalresources.converter.JsonConverter;
import fr.prcaen.externalresources.exception.ExternalResourceException;
import fr.prcaen.externalresources.exception.NotFoundException;
import fr.prcaen.externalresources.listener.OnExternalResourcesLoadListener;
import fr.prcaen.externalresources.model.Resources;
import fr.prcaen.externalresources.url.DefaultUrl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("ConstantConditions")
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ExternalResourcesTest {
  @Mock
  private Context context;

  private ExternalResources externalResources;
  private Resources resources;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(context.getApplicationContext()).thenReturn(RuntimeEnvironment.application);

    resources = Resources.fromJson(IOUtils.toString(getClass().getResourceAsStream("/test.json"), "UTF-8"));

    externalResources = new ExternalResources.Builder(context, "/")
        .defaultResources(resources)
        .build();
  }

  @Test
  public void testOnConfigurationChanged() throws Exception {
    externalResources.onConfigurationChanged(RuntimeEnvironment.application.getResources().getConfiguration());

    assertFalse(externalResources.getBoolean("adjust_view_bounds"));
  }

  @Test
  public void testOnConfigurationChangedShouldChange() throws Exception {
    Configuration configuration = RuntimeEnvironment.application.getResources().getConfiguration();
    configuration.fontScale = 12;
    externalResources.onConfigurationChanged(configuration);

    assertEquals(externalResources.getString("hello"), "Hello!");
  }

  @Test
  public void testGetBoolean() throws Exception {
    assertTrue(externalResources.getBoolean("screen_small"));
  }

  @Test(expected = NotFoundException.class)
  public void testGetBooleanNotFound() throws Exception {
    externalResources.getBoolean("unknown");
  }

  @Test
  public void testGetColor() throws Exception {
    assertEquals(externalResources.getColor("translucent_red"), Color.parseColor("#80ff0000"));
  }

  @Test(expected = NotFoundException.class)
  public void testGetColorNotFound() throws Exception {
    externalResources.getColor("unknown");
  }

  @Test
  public void testGetDimension() throws Exception {
    assertEquals(externalResources.getDimension("textview_height"), 25f);
  }

  @Test(expected = NotFoundException.class)
  public void testGetDimensionNotFound() throws Exception {
    externalResources.getDimension("unknown");
  }

  @Test(expected = NotFoundException.class)
  public void testWrongDimension() throws Exception {
    externalResources.getDimension("wrong_dimension");
  }

  @Test
  public void testGetString() throws Exception {
    assertEquals(externalResources.getString("hello"), "Hello!");
  }

  @Test(expected = NotFoundException.class)
  public void testGetStringNotFound() throws Exception {
    externalResources.getString("unknown");
  }

  @Test
  public void testGetStringWithArguments() throws Exception {
    assertEquals(externalResources.getString("string_with_args", "Peter"), "Hello Peter!");
  }

  @Test(expected = NotFoundException.class)
  public void testGetStringWithArgumentsNotFound() throws Exception {
    externalResources.getString("unknown", "bar");
  }

  @Test
  public void testGetStringArray() throws Exception {
    assertEquals(externalResources.getStringArray("planets_array").length, 4);
  }

  @Test(expected = NotFoundException.class)
  public void testGetStringArrayNotFound() throws Exception {
    externalResources.getStringArray("unknown");
  }

  @Test
  public void testGetInteger() throws Exception {
    assertEquals(externalResources.getInteger("max_speed"), 75);
  }

  @Test(expected = NotFoundException.class)
  public void testGetIntegerNotFound() throws Exception {
    externalResources.getInteger("unknown");
  }

  @Test
  public void testGetIntArray() throws Exception {
    assertEquals(externalResources.getIntArray("bits").length, 4);
  }

  @Test(expected = NotFoundException.class)
  public void testGetIntArrayNotFound() throws Exception {
    externalResources.getIntArray("unknown");
  }

  @Test
  public void testRegister() throws Exception {
    assertEquals(externalResources.listeners.size(), 0);

    externalResources.register(new OnExternalResourcesLoadListener() {
      @Override
      public void onExternalResourcesLoadFailed(ExternalResourceException e) {

      }

      @Override
      public void onExternalResourcesChange(ExternalResources externalResources) {

      }
    });

    assertEquals(externalResources.listeners.size(), 1);
  }

  @Test
  public void testUnRegister() throws Exception {
    OnExternalResourcesLoadListener listener = new OnExternalResourcesLoadListener() {
      @Override
      public void onExternalResourcesLoadFailed(ExternalResourceException exception) {

      }

      @Override
      public void onExternalResourcesChange(ExternalResources externalResources) {

      }
    };

    externalResources.register(listener);
    assertEquals(externalResources.listeners.size(), 1);
    externalResources.unregister(listener);
    assertEquals(externalResources.listeners.size(), 0);
  }

  @Test
  public void testSetLogLevel() throws Exception {
    assertEquals(Logger.getLevel(), Logger.LEVEL_ERROR);
    externalResources.setLogLevel(Logger.LEVEL_DEBUG);
    assertEquals(Logger.getLevel(), Logger.LEVEL_DEBUG);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInstanceWoInitialize() throws Exception {
    ExternalResources.singleton = null;
    ExternalResources.getInstance();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithExternalResourcesWithNull() {
    ExternalResources.singleton = null;
    ExternalResources.initialize(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testInitializeWithExternalResourcesAlreadyInitialize() {
    ExternalResources.singleton = null;
    ExternalResources.initialize(externalResources);
    ExternalResources.initialize(externalResources);
  }

  @Test
  public void testGetInstance() throws Exception {
    ExternalResources.singleton = null;
    ExternalResources.initialize(externalResources);
    assertNotNull(ExternalResources.getInstance());
  }

  @Test
  public void testInitializeWithPath() throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(context, "/"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithPathWoContext() throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(null, "/"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithPathWoPath() throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(context, (String) null));
  }

  @Test(expected = IllegalStateException.class)
  public void testInitializeWitPathAlreadyInitialize() {
    ExternalResources.singleton = null;
    ExternalResources.initialize(context, "/");
    ExternalResources.initialize(context, "/");
  }

  @Test
  public void testInitializeWithUrl() throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(context, new DefaultUrl("/")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithUrlWoContext() throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(null, new DefaultUrl("/")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithUrlWoPath() throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(context, (DefaultUrl) null));
  }

  @Test(expected = IllegalStateException.class)
  public void testInitializeWitUrlAlreadyInitialize() {
    ExternalResources.singleton = null;
    ExternalResources.initialize(context, new DefaultUrl("/"));
    ExternalResources.initialize(context, new DefaultUrl("/"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildWithContextNull() throws Exception {
    new ExternalResources.Builder(null, new DefaultUrl("/"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildWithUrlNull() throws Exception {
    new ExternalResources.Builder(context, (DefaultUrl) null);
  }

  @Test
  public void testBuilderCachePolicy() throws Exception {
    assertNotNull(new ExternalResources.Builder(context, new DefaultUrl("/")).cachePolicy(Cache.POLICY_NONE));
  }

  @Test
  public void testBuilderLogLevel() throws Exception {
    assertNotNull(new ExternalResources.Builder(context, new DefaultUrl("/")).logLevel(Logger.LEVEL_DEBUG));
  }

  @Test
  public void testBuilderDefaultResources() throws Exception {
    assertNotNull(new ExternalResources.Builder(context, new DefaultUrl("/")).defaultResources(resources));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuilderDefaultResourcesWithNull() throws Exception {
    new ExternalResources.Builder(context, new DefaultUrl("/")).defaultResources(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuilderDefaultResourcesAlreadyDefined() throws Exception {
    ExternalResources.Builder builder = new ExternalResources.Builder(context, new DefaultUrl("/")).defaultResources(resources);
    builder.defaultResources(resources);
  }

  @Test
  public void testBuilderListener() throws Exception {
    assertNotNull(new ExternalResources.Builder(context, new DefaultUrl("/")).listener(new OnExternalResourcesLoadListener() {
      @Override
      public void onExternalResourcesLoadFailed(ExternalResourceException exception) {
      }

      @Override
      public void onExternalResourcesChange(ExternalResources externalResources) {
      }
    }));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuilderListenerWithNull() throws Exception {
    new ExternalResources.Builder(context, new DefaultUrl("/")).listener(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuilderListenerAlreadyDefined() throws Exception {
    ExternalResources.Builder builder = new ExternalResources.Builder(context, new DefaultUrl("/")).listener(new OnExternalResourcesLoadListener() {
      @Override
      public void onExternalResourcesLoadFailed(ExternalResourceException exception) {
      }

      @Override
      public void onExternalResourcesChange(ExternalResources externalResources) {
      }
    });
    builder.listener(new OnExternalResourcesLoadListener() {
      @Override
      public void onExternalResourcesLoadFailed(ExternalResourceException exception) {
      }

      @Override
      public void onExternalResourcesChange(ExternalResources externalResources) {
      }
    });
  }

  @Test
  public void testBuilderOptions() throws Exception {
    assertNotNull(new ExternalResources.Builder(context, new DefaultUrl("/")).options(Options.createDefault()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuilderOptionsWithNull() throws Exception {
    new ExternalResources.Builder(context, new DefaultUrl("/")).options(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuilderOptionsAlreadyDefined() throws Exception {
    ExternalResources.Builder builder = new ExternalResources.Builder(context, new DefaultUrl("/")).options(Options.createDefault());
    builder.options(Options.createDefault());
  }

  @Test
  public void testBuilderConverter() throws Exception {
    assertNotNull(new ExternalResources.Builder(context, new DefaultUrl("/")).converter(new JsonConverter()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuilderConverterWithNull() throws Exception {
    new ExternalResources.Builder(context, new DefaultUrl("/")).converter(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuilderConverterAlreadyDefined() throws Exception {
    ExternalResources.Builder builder = new ExternalResources.Builder(context, new DefaultUrl("/")).converter(new JsonConverter());
    builder.converter(new JsonConverter());
  }
}