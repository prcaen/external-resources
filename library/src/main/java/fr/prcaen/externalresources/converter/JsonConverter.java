package fr.prcaen.externalresources.converter;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import fr.prcaen.externalresources.model.Resource;
import fr.prcaen.externalresources.model.Resources;

public final class JsonConverter implements Converter {
  private static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(Resource.class, new ResourceJsonDeserializer())
      .create();

  @Override
  public Resources fromReader(Reader reader) throws IOException {
    JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);

    Resources resources = new Resources();

    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      Resource resource = GSON.fromJson(entry.getValue(), Resource.class);

      if (resource != null) {
        resources.add(entry.getKey(), resource);
      }
    }

    return resources;
  }

  public Resources fromString(String string) throws IOException {
    return fromReader(new StringReader(string));
  }

  private static class ResourceJsonSerializer implements JsonSerializer<Resource> {

    @Override
    public JsonElement serialize(Resource src, Type typeOfSrc, JsonSerializationContext context) {
      return null;
    }
  }

  private static class ResourceJsonDeserializer implements JsonDeserializer<Resource> {
    @Override
    public Resource deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return get(json, 0);
    }

    @Nullable
    private Resource get(JsonElement json, int depth) throws JsonParseException {
      if (json.isJsonPrimitive()) {
        return get(json.getAsJsonPrimitive());
      } else if (json.isJsonArray() && depth == 0) {
        ArrayList<Resource> resources = new ArrayList<>();

        for (JsonElement element : json.getAsJsonArray()) {
          resources.add(get(element, 1));
        }

        Resource[] resourcesArray = new Resource[resources.size()];
        resourcesArray = resources.toArray(resourcesArray);

        return new Resource(resources.toArray(resourcesArray));
      } else {
        return null;
      }
    }

    private Resource get(JsonPrimitive primitive) {
      if (primitive.isBoolean()) {
        return new Resource(primitive.getAsBoolean());
      } else if (primitive.isNumber()) {
        return new Resource(primitive.getAsNumber());
      } else {
        return new Resource(primitive.getAsString());
      }
    }
  }
}
