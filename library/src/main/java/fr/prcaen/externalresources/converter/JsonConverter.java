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
import fr.prcaen.externalresources.model.Resource;
import fr.prcaen.externalresources.model.Resources;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public final class JsonConverter implements Converter {
  private static final Gson GSON =
      new GsonBuilder().registerTypeAdapter(Resource.class, new ResourceJsonDeserializer())
          .create();

  @Override @Nullable public Resources fromReader(Reader reader) throws IOException {
    JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);

    if (null == jsonObject) {
      return null;
    }

    Resources resources = new Resources();

    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      Resource resource = GSON.fromJson(entry.getValue(), Resource.class);

      if (null != resource) {
        resources.add(entry.getKey(), resource);
      }
    }

    return resources;
  }

  @SuppressWarnings("unused") public Resources fromString(String string) throws IOException {
    return fromReader(new StringReader(string));
  }

  protected static class ResourceJsonDeserializer implements JsonDeserializer<Resource> {
    @Override
    public Resource deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      return get(json, 0);
    }

    @Nullable private Resource get(JsonElement json, int depth) throws JsonParseException {
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
