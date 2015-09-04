package fr.prcaen.externalresources.converter;

import junit.framework.TestCase;

import fr.prcaen.externalresources.model.Resources;

public class JsonConverterTest extends TestCase {
  private final JsonConverter converter = new JsonConverter();

  public void testString() throws Exception {
    Resources resources = converter.fromString("{\"foo\":\"bar\"}");

    assertTrue(resources.has("foo"));
    assertTrue(resources.get("foo").isString());
    assertEquals(resources.get("foo").getAsString(), "bar");
  }

  public void testBoolean() throws Exception {
    Resources resources = converter.fromString("{\"is_accepted\":true}");

    assertTrue(resources.has("is_accepted"));
    assertTrue(resources.get("is_accepted").isBoolean());
    assertEquals(resources.get("is_accepted").getAsBoolean(), true);
  }

  public void testInteger() throws Exception {
    Resources resources = converter.fromString("{\"a_int\":1}");

    assertTrue(resources.has("a_int"));
    assertTrue(resources.get("a_int").isNumber());
    assertEquals(resources.get("a_int").getAsInt(), 1);
  }

  public void testFloat() throws Exception {
    Resources resources = converter.fromString("{\"a_float\":2.1,\"another_float\":2.0}");

    assertTrue(resources.has("a_float"));
    assertTrue(resources.get("a_float").isNumber());
    assertEquals(resources.get("a_float").getAsFloat(), 2.1f);

    assertTrue(resources.has("another_float"));
    assertTrue(resources.get("another_float").isNumber());
    assertEquals(resources.get("another_float").getAsFloat(), 2.0f);
  }

  public void testStringArray() throws Exception {
    Resources resources = converter.fromString("{\"an_array\":[\"a_string\",\"foobar\"]}");

    assertTrue(resources.has("an_array"));
    assertTrue(resources.get("an_array").isArray());
    assertEquals(resources.get("an_array").getAsStringArray().length, 2);
    assertEquals(resources.get("an_array").getAsStringArray()[0], "a_string");
    assertEquals(resources.get("an_array").getAsStringArray()[1], "foobar");
  }

  public void testIntegerArray() throws Exception {
    Resources resources = converter.fromString("{\"an_array\":[123,456]}");

    assertTrue(resources.has("an_array"));
    assertTrue(resources.get("an_array").isArray());
    assertEquals(resources.get("an_array").getAsIntegerArray().length, 2);
    assertEquals(resources.get("an_array").getAsIntegerArray()[0], 123);
    assertEquals(resources.get("an_array").getAsIntegerArray()[1], 456);
  }
}