package fr.prcaen.externalresources;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import fr.prcaen.externalresources.converter.JsonConverter;
import fr.prcaen.externalresources.exception.ExternalResourceException;
import fr.prcaen.externalresources.exception.NotFoundException;
import fr.prcaen.externalresources.listener.OnExternalResourcesChangeListener;
import fr.prcaen.externalresources.listener.OnExternalResourcesLoadFailedListener;
import fr.prcaen.externalresources.model.Resources;
import fr.prcaen.externalresources.url.DefaultUrl;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("ConstantConditions") @RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", sdk = Build.VERSION_CODES.JELLY_BEAN)
public class ExternalResourcesTest {
  @Mock private Context context;

  private ExternalResources externalResources;
  private Resources resources;

  @Before public void setUp() throws Exception {
    initMocks(this);
    when(context.getApplicationContext()).thenReturn(RuntimeEnvironment.application);

    resources =
        Resources.fromJson(IOUtils.toString(getClass().getResourceAsStream("/test.json"), "UTF-8"));

    externalResources =
        new ExternalResources.Builder(context, new DefaultUrl("/")).defaultResources(resources)
            .build();
  }

  @Test public void testOnConfigurationChanged() throws Exception {
    externalResources.onConfigurationChanged(
        RuntimeEnvironment.application.getResources().getConfiguration());

    assertFalse(externalResources.getBoolean("adjust_view_bounds"));
  }

  @Test public void testOnConfigurationChangedShouldChange() throws Exception {
    Configuration configuration = RuntimeEnvironment.application.getResources().getConfiguration();
    configuration.fontScale = 12;
    externalResources.onConfigurationChanged(configuration);

    assertEquals(externalResources.getString("hello"), "Hello!");
  }

  @Test public void testGetBoolean() throws Exception {
    assertTrue(externalResources.getBoolean("screen_small"));
    assertTrue(externalResources.getBoolean(R.bool.is_enabled));
    assertTrue(externalResources.getBoolean("is_enabled"));
  }

  @Test(expected = NotFoundException.class) public void testGetBooleanNotFound() throws Exception {
    externalResources.getBoolean("unknown");
  }

  @Test(expected = NotFoundException.class) public void testGetResBooleanNotFound()
      throws Exception {
    externalResources.getBoolean(0);
  }

  @Test public void testGetColor() throws Exception {
    assertEquals(Color.parseColor("#80ff0000"), externalResources.getColor("translucent_red"));
    assertEquals(Color.parseColor("#FF0000"), externalResources.getColor(R.color.red));
    assertEquals(Color.parseColor("#FF0000"), externalResources.getColor("red"));
  }

  @Test(expected = NotFoundException.class) public void testGetColorNotFound() throws Exception {
    externalResources.getColor("unknown");
  }

  @Test(expected = NotFoundException.class) public void testGetResColorNotFound() throws Exception {
    externalResources.getColor(0);
  }

  @Test public void testGetDimension() throws Exception {
    assertEquals(25f, externalResources.getDimension("textview_height"));
    assertEquals(13559.055f, externalResources.getDimension(R.dimen.standard_gauge));
    assertEquals(13559.055f, externalResources.getDimension("standard_gauge"));
  }

  @Test(expected = NotFoundException.class) public void testGetDimensionNotFound()
      throws Exception {
    externalResources.getDimension("unknown");
  }

  @Test(expected = NotFoundException.class) public void testWrongDimension() throws Exception {
    externalResources.getDimension("wrong_dimension");
  }

  @Test public void testGetString() throws Exception {
    assertEquals(externalResources.getString("hello"), "Hello!");
    assertEquals(externalResources.getString("hello_world"), "Hello world!");
    assertEquals(externalResources.getString(R.string.hello_world), "Hello world!");
  }

  @SuppressWarnings("ResourceType") @Test(expected = NotFoundException.class)
  public void testGetStringNotFound() throws Exception {
    externalResources.getString("unknown");
  }

  @SuppressWarnings("ResourceType") @Test(expected = NotFoundException.class)
  public void testGetResStringNotFound() throws Exception {
    externalResources.getString(0);
  }

  @Test public void testGetStringWithArguments() throws Exception {
    assertEquals("Hello Peter!", externalResources.getString("string_with_args", "Peter"));
    assertEquals("Hello world Peter!",
        externalResources.getString("hello_world_with_args", "Peter"));
    assertEquals("Hello world Peter!",
        externalResources.getString(R.string.hello_world_with_args, "Peter"));
  }

  @Test(expected = NotFoundException.class) public void testGetStringWithArgumentsNotFound()
      throws Exception {
    externalResources.getString("unknown", "bar");
  }

  @Test(expected = NotFoundException.class) public void testGetResStringWithArgumentsNotFound()
      throws Exception {
    externalResources.getString(0, "bar");
  }

  @Test public void testGetStringArray() throws Exception {
    assertEquals(4, externalResources.getStringArray("planets_array").length);
    assertEquals(2, externalResources.getStringArray("gender").length);
    assertEquals(2, externalResources.getStringArray(R.array.gender).length);
  }

  @Test(expected = NotFoundException.class) public void testGetStringArrayNotFound()
      throws Exception {
    externalResources.getStringArray("unknown");
  }

  @Test(expected = NotFoundException.class) public void testGetResStringArrayNotFound()
      throws Exception {
    externalResources.getStringArray(0);
  }

  @Test public void testGetInteger() throws Exception {
    assertEquals(75, externalResources.getInteger("max_speed"));
    assertEquals(5000, externalResources.getInteger("network_connection_speed"));
    assertEquals(5000, externalResources.getInteger(R.integer.network_connection_speed));
  }

  @Test(expected = NotFoundException.class) public void testGetIntegerNotFound() throws Exception {
    externalResources.getInteger("unknown");
  }

  @Test(expected = NotFoundException.class) public void testGetResIntegerNotFound()
      throws Exception {
    externalResources.getInteger(0);
  }

  @Test public void testGetIntArray() throws Exception {
    assertEquals(4, externalResources.getIntArray("bits").length);
    assertEquals(5, externalResources.getIntArray("prime_number").length);
    assertEquals(5, externalResources.getIntArray(R.array.prime_number).length);
  }

  @Test(expected = NotFoundException.class) public void testGetIntArrayNotFound() throws Exception {
    externalResources.getIntArray("unknown");
  }

  @Test(expected = NotFoundException.class) public void testGetResIntArrayNotFound()
      throws Exception {
    externalResources.getIntArray(0);
  }

  @Test public void testRegister() throws Exception {
    assertEquals(externalResources.listeners.size(), 0);

    externalResources.register(new OnExternalResourcesChangeListener() {
      @Override public void onExternalResourcesChange(ExternalResources externalResources) {

      }
    });

    assertEquals(externalResources.listeners.size(), 1);
  }

  @Test public void testUnRegister() throws Exception {
    OnExternalResourcesChangeListener listener = new OnExternalResourcesChangeListener() {
      @Override public void onExternalResourcesChange(ExternalResources externalResources) {

      }
    };

    externalResources.register(listener);
    assertEquals(externalResources.listeners.size(), 1);
    externalResources.unregister(listener);
    assertEquals(externalResources.listeners.size(), 0);
  }

  @Test(expected = IllegalArgumentException.class) public void testGetInstanceWoInitialize()
      throws Exception {
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

  @Test public void testGetInstance() throws Exception {
    ExternalResources.singleton = null;
    ExternalResources.initialize(externalResources);
    assertNotNull(ExternalResources.getInstance());
  }

  @Test public void testInitializeWithPath() throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(context, "/"));
  }

  @Test(expected = IllegalArgumentException.class) public void testInitializeWithPathWoContext()
      throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(null, "/"));
  }

  @Test(expected = IllegalArgumentException.class) public void testInitializeWithPathWoPath()
      throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(context, (String) null));
  }

  @Test(expected = IllegalStateException.class)
  public void testInitializeWitPathAlreadyInitialize() {
    ExternalResources.singleton = null;
    ExternalResources.initialize(context, "/");
    ExternalResources.initialize(context, "/");
  }

  @Test public void testInitializeWithUrl() throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(context, new DefaultUrl("/")));
  }

  @Test(expected = IllegalArgumentException.class) public void testInitializeWithUrlWoContext()
      throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(null, new DefaultUrl("/")));
  }

  @Test(expected = IllegalArgumentException.class) public void testInitializeWithUrlWoPath()
      throws Exception {
    ExternalResources.singleton = null;
    assertNotNull(ExternalResources.initialize(context, (DefaultUrl) null));
  }

  @Test(expected = IllegalStateException.class)
  public void testInitializeWitUrlAlreadyInitialize() {
    ExternalResources.singleton = null;
    ExternalResources.initialize(context, new DefaultUrl("/"));
    ExternalResources.initialize(context, new DefaultUrl("/"));
  }

  @Test(expected = IllegalArgumentException.class) public void testBuildWithContextNull()
      throws Exception {
    new ExternalResources.Builder(null, new DefaultUrl("/"));
  }

  @Test(expected = IllegalArgumentException.class) public void testBuildWithUrlNull()
      throws Exception {
    new ExternalResources.Builder(context, null);
  }

  @Test public void testBuilderCachePolicy() throws Exception {
    assertNotNull(
        new ExternalResources.Builder(context, new DefaultUrl("/")).cachePolicy(Cache.POLICY_NONE));
  }

  @Test public void testBuilderLogLevel() throws Exception {
    assertNotNull(
        new ExternalResources.Builder(context, new DefaultUrl("/")).logLevel(Logger.LEVEL_DEBUG));
  }

  @Test public void testBuilderDefaultResources() throws Exception {
    assertNotNull(
        new ExternalResources.Builder(context, new DefaultUrl("/")).defaultResources(resources));
  }

  @Test(expected = IllegalArgumentException.class) public void testBuilderDefaultResourcesWithNull()
      throws Exception {
    new ExternalResources.Builder(context, new DefaultUrl("/")).defaultResources(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuilderDefaultResourcesAlreadyDefined() throws Exception {
    ExternalResources.Builder builder =
        new ExternalResources.Builder(context, new DefaultUrl("/")).defaultResources(resources);
    builder.defaultResources(resources);
  }

  @Test public void testBuilderListener() throws Exception {
    assertNotNull(new ExternalResources.Builder(context, new DefaultUrl("/")).failListener(
        new OnExternalResourcesLoadFailedListener() {
          @Override public void onExternalResourcesLoadFailed(ExternalResourceException exception) {
          }
        }));
  }

  @Test(expected = IllegalArgumentException.class) public void testBuilderListenerWithNull()
      throws Exception {
    new ExternalResources.Builder(context, new DefaultUrl("/")).failListener(null);
  }

  @Test(expected = IllegalStateException.class) public void testBuilderListenerAlreadyDefined()
      throws Exception {
    ExternalResources.Builder builder =
        new ExternalResources.Builder(context, new DefaultUrl("/")).failListener(
            new OnExternalResourcesLoadFailedListener() {
              @Override
              public void onExternalResourcesLoadFailed(ExternalResourceException exception) {
              }
            });
    builder.failListener(new OnExternalResourcesLoadFailedListener() {
      @Override public void onExternalResourcesLoadFailed(ExternalResourceException exception) {
      }
    });
  }

  @Test public void testBuilderOptions() throws Exception {
    assertNotNull(new ExternalResources.Builder(context, new DefaultUrl("/")).options(
        Options.createDefault()));
  }

  @Test(expected = IllegalArgumentException.class) public void testBuilderOptionsWithNull()
      throws Exception {
    new ExternalResources.Builder(context, new DefaultUrl("/")).options(null);
  }

  @Test(expected = IllegalStateException.class) public void testBuilderOptionsAlreadyDefined()
      throws Exception {
    ExternalResources.Builder builder =
        new ExternalResources.Builder(context, new DefaultUrl("/")).options(
            Options.createDefault());
    builder.options(Options.createDefault());
  }

  @Test public void testBuilderConverter() throws Exception {
    assertNotNull(
        new ExternalResources.Builder(context, new DefaultUrl("/")).converter(new JsonConverter()));
  }

  @Test(expected = IllegalArgumentException.class) public void testBuilderConverterWithNull()
      throws Exception {
    new ExternalResources.Builder(context, new DefaultUrl("/")).converter(null);
  }

  @Test(expected = IllegalStateException.class) public void testBuilderConverterAlreadyDefined()
      throws Exception {
    ExternalResources.Builder builder =
        new ExternalResources.Builder(context, new DefaultUrl("/")).converter(new JsonConverter());
    builder.converter(new JsonConverter());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetResWithUseApplicationResourcesToFalse() {
    ExternalResources.Builder builder =
        new ExternalResources.Builder(context, new DefaultUrl("/")).useApplicationResources(false);
    builder.build().getString(R.string.hello_world);
  }

  @Test(expected = NotFoundException.class)
  public void testGetWithUseApplicationResourcesToFalse() {
    ExternalResources.Builder builder =
        new ExternalResources.Builder(context, new DefaultUrl("/")).useApplicationResources(false);
    builder.build().getString("hello_world");
  }
}