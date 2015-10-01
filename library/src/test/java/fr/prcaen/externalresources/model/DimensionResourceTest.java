package fr.prcaen.externalresources.model;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public final class DimensionResourceTest {

  private DimensionResource resource;

  @Test
  public void testToFloat() throws Exception {
    resource = new DimensionResource(TypedValue.COMPLEX_UNIT_DIP, 2.0f);

    DisplayMetrics metrics = new DisplayMetrics();
    metrics.density = 200;
    assertTrue(resource.toFloat(metrics) == 400.0f);
  }

  @Test
  public void testFromString() throws Exception {
    resource = DimensionResource.fromString("16dp");
    assertEquals(resource.value, 16.0f);
    assertEquals(resource.type, TypedValue.COMPLEX_UNIT_DIP);

    resource = DimensionResource.fromString("17dip");
    assertEquals(resource.value, 17.0f);
    assertEquals(resource.type, TypedValue.COMPLEX_UNIT_DIP);

    resource = DimensionResource.fromString("18sp");
    assertEquals(resource.value, 18.0f);
    assertEquals(resource.type, TypedValue.COMPLEX_UNIT_SP);

    resource = DimensionResource.fromString("19pt");
    assertEquals(resource.value, 19.0f);
    assertEquals(resource.type, TypedValue.COMPLEX_UNIT_PT);

    resource = DimensionResource.fromString("20in");
    assertEquals(resource.value, 20.0f);
    assertEquals(resource.type, TypedValue.COMPLEX_UNIT_IN);

    resource = DimensionResource.fromString("21mm");
    assertEquals(resource.value, 21.0f);
    assertEquals(resource.type, TypedValue.COMPLEX_UNIT_MM);

    resource = DimensionResource.fromString("22px");
    assertEquals(resource.value, 22.0f);
    assertEquals(resource.type, TypedValue.COMPLEX_UNIT_PX);

    resource = DimensionResource.fromString("23");
    assertEquals(resource.value, 23.0f);
    assertEquals(resource.type, TypedValue.COMPLEX_UNIT_PX);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromStringWithUnknownUnit() throws Exception {
    resource = DimensionResource.fromString("23sd");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromStringWithOnlyUnit() throws Exception {
    resource = DimensionResource.fromString("dp");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromStringWithError() throws Exception {
    resource = DimensionResource.fromString("15$$$*dp");
  }
}