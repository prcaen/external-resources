package fr.prcaen.externalresources.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public final class ResourceTest {
  private static final Resource RESOURCE_BOOLEAN = new Resource(true);
  private static final Resource RESOURCE_STRING = new Resource("foo");
  private static final Resource RESOURCE_FLOAT = new Resource(2.0f);
  private static final Resource RESOURCE_INTEGER = new Resource(1);
  private static final Resource RESOURCE_ARRAY = new Resource(new Resource[] {});

  @Test public void testIsBoolean() throws Exception {
    assertTrue("true is a boolean", RESOURCE_BOOLEAN.isBoolean());
    assertFalse("test is a boolean", RESOURCE_STRING.isBoolean());
    assertFalse("2.0 is a boolean", RESOURCE_FLOAT.isBoolean());
    assertFalse("1 is a boolean", RESOURCE_INTEGER.isBoolean());
    assertFalse("resource array is an boolean", RESOURCE_ARRAY.isBoolean());
  }

  @Test public void testIsNumber() throws Exception {
    assertFalse("true is a number", RESOURCE_BOOLEAN.isNumber());
    assertFalse("test is a number", RESOURCE_STRING.isNumber());
    assertTrue("2.0 is a number", RESOURCE_FLOAT.isNumber());
    assertTrue("1 is a number", RESOURCE_INTEGER.isNumber());
    assertFalse("resource array is an number", RESOURCE_ARRAY.isNumber());
  }

  @Test public void testIsString() throws Exception {
    assertFalse("true is a string", RESOURCE_BOOLEAN.isString());
    assertTrue("test is a string", RESOURCE_STRING.isString());
    assertFalse("2.0 is a string", RESOURCE_FLOAT.isString());
    assertFalse("1 is a string", RESOURCE_INTEGER.isString());
    assertFalse("resource array is a string", RESOURCE_ARRAY.isString());
  }

  @Test public void testIsArray() throws Exception {
    assertFalse("true is an array", RESOURCE_BOOLEAN.isArray());
    assertFalse("test is an array", RESOURCE_STRING.isArray());
    assertFalse("2.0 is an array", RESOURCE_FLOAT.isArray());
    assertFalse("1 is an array", RESOURCE_INTEGER.isArray());
    assertTrue("resource array is an array", RESOURCE_ARRAY.isArray());
  }

  @Test public void testGetAsBoolean() throws Exception {
    assertEquals(RESOURCE_BOOLEAN.getAsBoolean(), true);
    assertEquals(RESOURCE_STRING.getAsBoolean(), false);
    assertEquals(RESOURCE_FLOAT.getAsBoolean(), false);
    assertEquals(RESOURCE_INTEGER.getAsBoolean(), false);
    assertEquals(RESOURCE_ARRAY.getAsBoolean(), false);
    assertEquals(new Resource("true").getAsBoolean(), true);
  }

  @Test public void testGetAsFloat() throws Exception {
    assertEquals(RESOURCE_BOOLEAN.getAsFloat(), null);
    assertEquals(RESOURCE_STRING.getAsFloat(), null);
    assertEquals(RESOURCE_FLOAT.getAsFloat(), 2.0f);
    assertEquals(RESOURCE_INTEGER.getAsFloat(), 1f);
    assertEquals(RESOURCE_ARRAY.getAsFloat(), null);
    assertEquals(new Resource("3.0").getAsFloat(), 3.0f);
  }

  @Test public void testGetAsInt() throws Exception {
    assertEquals(RESOURCE_BOOLEAN.getAsInt(), null);
    assertEquals(RESOURCE_STRING.getAsInt(), null);
    assertEquals(RESOURCE_FLOAT.getAsInt(), Integer.valueOf(2));
    assertEquals(RESOURCE_INTEGER.getAsInt(), Integer.valueOf(1));
    assertEquals(RESOURCE_ARRAY.getAsInt(), null);
    assertEquals(new Resource("2").getAsInt(), Integer.valueOf(2));
    assertEquals(new Resource("3.0").getAsInt(), null);
  }

  @Test public void testGetAsString() throws Exception {
    assertEquals(RESOURCE_BOOLEAN.getAsString(), "true");
    assertEquals(RESOURCE_STRING.getAsString(), "foo");
    assertEquals(RESOURCE_FLOAT.getAsString(), "2.0");
    assertEquals(RESOURCE_INTEGER.getAsString(), "1");
    assertEquals(RESOURCE_ARRAY.getAsString(), null);
  }

  @Test public void testGetAsIntegerArray() throws Exception {
    assertEquals(RESOURCE_BOOLEAN.getAsIntegerArray().length, 0);
    assertEquals(RESOURCE_STRING.getAsIntegerArray().length, 0);
    assertEquals(RESOURCE_FLOAT.getAsIntegerArray().length, 0);
    assertEquals(RESOURCE_INTEGER.getAsIntegerArray().length, 0);
    assertEquals(RESOURCE_ARRAY.getAsIntegerArray().length, 0);
    assertEquals(new Resource(
        new Resource[] { RESOURCE_INTEGER, RESOURCE_FLOAT }).getAsIntegerArray().length, 2);
    assertEquals(
        new Resource(new Resource[] { RESOURCE_INTEGER, RESOURCE_FLOAT }).getAsIntegerArray()[0],
        1);
    assertEquals(
        new Resource(new Resource[] { RESOURCE_INTEGER, RESOURCE_FLOAT }).getAsIntegerArray()[1],
        2);
  }

  @Test public void testGetAsStringArray() throws Exception {
    assertEquals(RESOURCE_BOOLEAN.getAsStringArray().length, 4);
    assertEquals(RESOURCE_STRING.getAsStringArray().length, 3);
    assertEquals(RESOURCE_FLOAT.getAsStringArray().length, 3);
    assertEquals(RESOURCE_INTEGER.getAsStringArray().length, 1);
    assertEquals(RESOURCE_ARRAY.getAsStringArray().length, 0);
    assertEquals(
        new Resource(new Resource[] { RESOURCE_INTEGER, RESOURCE_FLOAT }).getAsStringArray().length,
        2);
    assertEquals(
        new Resource(new Resource[] { RESOURCE_INTEGER, RESOURCE_FLOAT }).getAsStringArray()[0],
        "1");
    assertEquals(
        new Resource(new Resource[] { RESOURCE_INTEGER, RESOURCE_FLOAT }).getAsStringArray()[1],
        "2.0");
    assertEquals(new Resource((Resource[]) null).getAsStringArray().length, 0);
  }

  @SuppressWarnings("ConstantConditions") @Test public void testGetAsNumber() throws Exception {
    assertEquals(RESOURCE_BOOLEAN.getAsNumber(), null);
    assertEquals(new Resource("1").getAsNumber().intValue(), 1);
    assertEquals(new Resource("3.1").getAsNumber().floatValue(), 3.1f);
    assertEquals(RESOURCE_FLOAT.getAsNumber(), 2.0f);
    assertEquals(RESOURCE_INTEGER.getAsNumber(), 1);
    assertEquals(RESOURCE_ARRAY.getAsNumber(), null);
  }

  @Test public void testGetAsArray() throws Exception {
    assertEquals(RESOURCE_BOOLEAN.getAsArray().length, 0);
    assertEquals(
        new Resource(new Resource[] { RESOURCE_INTEGER, RESOURCE_FLOAT }).getAsArray().length, 2);
  }

  @Test public void testGetAsNonPrimitiveBoolean() throws Exception {
    assertEquals(RESOURCE_BOOLEAN.getAsNonPrimitiveBoolean(), Boolean.valueOf(true));
    assertEquals(RESOURCE_FLOAT.getAsNonPrimitiveBoolean(), null);
  }
}