package fr.prcaen.externalresources.model;

import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
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
  public void testFrom() throws Exception {

  }

  @Test
  public void testFrom1() throws Exception {

  }

  @Test
  public void testFromJson() throws Exception {

  }

  @Test
  public void testFromJson1() throws Exception {

  }

  @Test
  public void testFromJson2() throws Exception {

  }
}