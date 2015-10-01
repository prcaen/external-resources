package fr.prcaen.externalresources.converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import fr.prcaen.externalresources.model.Resources;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class JsonConverterTest {
  private final InputStreamReader resourcesSteamReader;
  private final JsonConverter.ResourceJsonDeserializer deserializer;

  public JsonConverterTest() throws IOException {
    this.resourcesSteamReader = new InputStreamReader(getClass().getResourceAsStream("/test.json"));
    this.deserializer = new JsonConverter.ResourceJsonDeserializer();
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testFromReader() throws Exception {
    Resources resources = new JsonConverter().fromReader(resourcesSteamReader);

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
  public void testFromStringNull() throws Exception {
    assertTrue(new JsonConverter().fromString("") == null);
  }

  @Test
  public void testFromString() throws Exception {

  }

  @Test
  public void testDeserialize() throws Exception {
    assertEquals(deserializer.deserialize(new JsonPrimitive(true), null, null).getAsBoolean(), true);
    assertEquals(deserializer.deserialize(new JsonPrimitive(false), null, null).getAsBoolean(), false);

    assertEquals(deserializer.deserialize(new JsonPrimitive(""), null, null).getAsString(), "");
    assertEquals(deserializer.deserialize(new JsonPrimitive("empty"), null, null).getAsString(), "empty");

    assertEquals(deserializer.deserialize(new JsonPrimitive(1), null, null).getAsInt(), Integer.valueOf(1));
    assertEquals(deserializer.deserialize(new JsonPrimitive(1.0f), null, null).getAsFloat(), 1.0f);

    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive(3));
    assertEquals(deserializer.deserialize(array, null, null).getAsIntegerArray().length, 1);
    assertEquals(deserializer.deserialize(array, null, null).getAsIntegerArray()[0], 3);

    assertEquals(deserializer.deserialize(new JsonObject(), null, null), null);
  }
}