package fr.prcaen.externalresources.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

public final class Resources {
  private static final Gson gson = new Gson();

  private final JsonObject elements;

  public Resources() {
    this.elements = new JsonObject();
  }

  public Resources(@NonNull JsonObject resources) {
    this.elements = resources;
  }

  public JsonObject getElements() {
    return elements;
  }

  public boolean getBoolean(@NonNull String key) throws NotFoundException {
    return getAs(key, boolean.class);
  }

  public String getString(@NonNull String key) throws NotFoundException {
    return getAs(key, String.class);
  }

  public int getInteger(@NonNull String key) throws NotFoundException {
    return getAs(key, int.class);
  }

  public String[] getStringArray(@NonNull String key) throws NotFoundException {
    return getAs(key, String[].class);
  }

  public int[] getIntArray(@NonNull String key) throws NotFoundException {
    return getAs(key, int[].class);
  }

  private <T> T getAs(@NonNull String key, Class<T> classOfT) {
    if (!TextUtils.isEmpty(key)) {
      JsonElement element = elements.get(key);

      if (element != null) {
        try {
          gson.fromJson(element, classOfT);
        } catch (JsonSyntaxException ignored) {
        }
      }
    }

    throw new NotFoundException(classOfT.getCanonicalName() + " resource with key: " + key);
  }

  public Resources merge(Resources resources) {
    JsonObject elements = getElements();

    for(Map.Entry<String, JsonElement> entry : resources.getElements().entrySet()) {
      elements.add(entry.getKey(), entry.getValue());
    }

    return new Resources(elements);
  }

  public static Resources fromJson(Reader json) {
    return new Resources(gson.fromJson(json, JsonObject.class));
  }

  public static Resources fromJson(InputStream stream) {
    return fromJson(new InputStreamReader(stream));
  }

  @SuppressWarnings("unused")
  public static Resources fromJson(String json) {
    return new Resources(gson.fromJson(json, JsonObject.class));
  }

  @SuppressWarnings("unused")
  public static class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(String name) {
      super(name);
    }
  }
}
