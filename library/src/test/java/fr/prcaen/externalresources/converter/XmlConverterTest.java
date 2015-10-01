package fr.prcaen.externalresources.converter;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.AbstractMap;

import fr.prcaen.externalresources.model.Resource;
import fr.prcaen.externalresources.model.Resources;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public final class XmlConverterTest {
  private final InputStreamReader resourcesSteamReader;
  private final XmlConverter converter;

  public XmlConverterTest() throws Exception {
    this.resourcesSteamReader = new InputStreamReader(getClass().getResourceAsStream("/test.xml"));
    this.converter = new XmlConverter();
  }

  @Test
  public void testFromReader() throws Exception {
    Resources resources = new XmlConverter().fromReader(resourcesSteamReader);

    assertTrue("hello key exists", resources.has("hello"));
    assertTrue("screen_small key exists", resources.has("screen_small"));
    assertTrue("adjust_view_bounds key exists", resources.has("adjust_view_bounds"));
    assertTrue("max_speed key exists", resources.has("max_speed"));
    assertTrue("min_speed key exists", resources.has("min_speed"));
    assertTrue("planets_array key exists", resources.has("planets_array"));
    assertTrue("planets_array is array", resources.get("planets_array").isArray());
    assertTrue("bits key exists", resources.has("bits"));
    assertTrue("bits is array", resources.get("bits").isArray());
  }

  @Test
  public void testGetFromNodeIgnore() throws Exception {
    Document doc = XmlConverter.read(new StringReader("<?xml version=\"1.0\" encoding=\"utf-8\"?><resources><test name=\"max_speed\">75</test></resources>"));
    AbstractMap.SimpleEntry<String, Resource> entry = converter.get(doc.getDocumentElement().getChildNodes().item(0));

    assertNull(entry);
  }

  @Test
  public void testGetResourceFromNodeIgnore() throws Exception {
    Resource entry = converter.getResource(String[].class, (Node) null);

    assertNull(entry);
  }

  @Test(expected=IOException.class)
  public void testEmptyString() throws Exception {
    converter.fromString("");
  }

  @Test
  public void testFromString() throws Exception {
    assertNotNull(converter.fromString("<?xml version=\"1.0\" encoding=\"utf-8\"?><resources><test name=\"max_speed\">75</test></resources>"));
  }

  /*@Test
  public void testString() throws Exception {
    assertTrue("hello key exists", resources.has("hello"));
    assertTrue("hello is string", resources.get("hello").isString());
    assertEquals("hello value is Hello!", resources.get("hello").getAsString(), "Hello!");
  }

  @Test
  public void testBoolean() throws Exception {
    assertTrue("screen_small key exists", resources.has("screen_small"));
    assertTrue("adjust_view_bounds key exists", resources.has("adjust_view_bounds"));

    assertTrue("screen_small is boolean", resources.get("screen_small").isBoolean());
    assertTrue("adjust_view_bounds is boolean", resources.get("adjust_view_bounds").isBoolean());

    assertEquals("screen_small is true", resources.get("screen_small").getAsBoolean(), true);
    assertEquals("adjust_view_bounds is false", resources.get("adjust_view_bounds").getAsBoolean(), false);
  }

  @Test
  public void testInteger() throws Exception {
    assertTrue("max_speed key exists", resources.has("max_speed"));
    assertTrue("min_speed key exists", resources.has("min_speed"));
    assertTrue("max_speed key is a number", resources.get("max_speed").isNumber());
    assertTrue("min_speed key is a number", resources.get("min_speed").isNumber());
    assertEquals("max_speed is equal to 75", resources.get("max_speed").getAsInt(), Integer.valueOf(75));
    assertEquals("min_speed is equal to 5", resources.get("min_speed").getAsInt(), Integer.valueOf(5));
  }

  @Test
  public void testStringArray() throws Exception {
    assertTrue("planets_array key exists", resources.has("planets_array"));
    assertTrue("planets_array is array", resources.get("planets_array").isArray());
    assertEquals("planets_array array length is 4", resources.get("planets_array").getAsStringArray().length, 4);
    assertEquals("planets_array[0] equals Mercury", resources.get("planets_array").getAsStringArray()[0], "Mercury");
    assertEquals("planets_array[2] equals Earth", resources.get("planets_array").getAsStringArray()[2], "Earth");
  }

  @Test
  public void testIntegerArray() throws Exception {
    assertTrue("bits key exists", resources.has("bits"));
    assertTrue("bits is array", resources.get("bits").isArray());
    assertEquals("bits array length is 4", resources.get("bits").getAsIntegerArray().length, 4);
    assertEquals("bits[0] equals 4", resources.get("bits").getAsIntegerArray()[0], 4);
    assertEquals("bits[3] equals 32", resources.get("bits").getAsIntegerArray()[3], 32);
  }*/
}