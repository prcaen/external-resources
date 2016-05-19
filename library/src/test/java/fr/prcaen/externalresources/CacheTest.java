package fr.prcaen.externalresources;

import android.app.Application;
import android.content.Context;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class) @Config(manifest = Config.NONE) public final class CacheTest {

  @Mock private Context context;

  @Mock private Application application;

  private Cache cache;

  @Before public void setUp() throws Exception {
    initMocks(this);
    when(context.getApplicationContext()).thenReturn(application);

    cache = new Cache(context);
  }

  @Test public void testGetCacheDir() throws Exception {
    assertNotNull(cache.getCacheDir());
    assertTrue(cache.getCacheDir().exists());
  }

  @Test public void testGetCacheSize() throws Exception {
    assertTrue(cache.getCacheSize() >= Cache.MIN_DISK_CACHE_SIZE);
  }
}