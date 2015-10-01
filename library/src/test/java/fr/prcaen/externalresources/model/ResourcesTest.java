package fr.prcaen.externalresources.model;

import org.junit.Test;

import java.io.StringReader;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public final class ResourcesTest {
  private static final HashMap<String, Resource> EMPTY_HASH_MAP = new HashMap<>();

  @Test
  public void testAdd() throws Exception {
    Resources resources = new Resources();
    resources.add("foo", new Resource("bar"));

    assertEquals(resources.members.size(), 1);
  }

  @Test
  public void testEntrySet() throws Exception {
    HashMap<String, Resource> map = EMPTY_HASH_MAP;
    map.put("foo", new Resource("bar"));

    Resources resources = new Resources(map);

    assertEquals(resources.entrySet().size(), 1);
  }

  @Test
  public void testHas() throws Exception {
    HashMap<String, Resource> map = EMPTY_HASH_MAP;
    map.put("foo", new Resource("bar"));

    Resources resources = new Resources(map);

    assertTrue(resources.has("foo"));
  }

  @Test
  public void testGet() throws Exception {
    HashMap<String, Resource> map = EMPTY_HASH_MAP;
    map.put("foo", new Resource("bar"));

    Resources resources = new Resources(map);

    assertEquals(resources.get("foo").getAsString(), new Resource("bar").getAsString());
  }

  @Test
  public void testMerge() throws Exception {
    Resources firstResources = new Resources(EMPTY_HASH_MAP);
    firstResources.add("foo", new Resource("bar"));
    firstResources.add("month", new Resource("june"));

    Resources lastResources = new Resources(EMPTY_HASH_MAP);
    lastResources.add("hello", new Resource("world"));
    lastResources.add("month", new Resource("may"));

    firstResources.merge(lastResources);

    assertEquals(firstResources.members.size(), 3);
    assertTrue(firstResources.has("foo"));
    assertTrue(firstResources.has("hello"));
    assertTrue(firstResources.has("month"));

    assertEquals(firstResources.get("month").getAsString(), "may");
  }

  @Test
  public void testFromJsonReader() throws Exception {
    assertNotNull(Resources.fromJson(new StringReader("{\"foo\":\"bar\"}")));
  }

  @Test
  public void testFromJsonString() throws Exception {
    assertNotNull(Resources.fromJson("{\"foo\":\"bar\"}"));
  }

  @Test
  public void testFromJsonInputStream() throws Exception {
    assertNotNull(Resources.fromJson(getClass().getResourceAsStream("/test.json")));
  }

  @Test
  public void testFromXmlReader() throws Exception {
    assertNotNull(Resources.fromXml(new StringReader("<?xml version=\"1.0\" encoding=\"utf-8\"?><resources><integer name=\"max_speed\">75</integer></resources>")));
  }

  @Test
  public void testFromXmlString() throws Exception {
    assertNotNull(Resources.fromXml("<?xml version=\"1.0\" encoding=\"utf-8\"?><resources><integer name=\"max_speed\">75</integer></resources>"));
  }

  @Test
  public void testFromXmlInputStream() throws Exception {
    assertNotNull(Resources.fromXml(getClass().getResourceAsStream("/test.xml")));
  }
}