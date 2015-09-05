package fr.prcaen.externalresources.model;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.prcaen.externalresources.converter.Converter;
import fr.prcaen.externalresources.converter.JsonConverter;

public final class Resources {
  private final HashMap<String, Resource> members;

  public Resources() {
    this.members = new HashMap<>();
  }

  public Resources(@NonNull HashMap<String, Resource> members) {
    this.members = members;
  }

  public Resource add(String key, Resource value) {
    return members.put(key, value);
  }

  public Resource remove(String key) {
    return members.remove(key);
  }

  public Set<Map.Entry<String, Resource>> entrySet() {
    return members.entrySet();
  }

  public boolean has(String key) {
    return members.containsKey(key);
  }

  public Resource get(String key) {
    return members.get(key);
  }

  public void putAll(Map<String, Resource> elements) {
    members.putAll(elements);
  }

  public Resources merge(Resources resources) {
    for (Map.Entry<String, Resource> entry : resources.entrySet()) {
      members.put(entry.getKey(), entry.getValue());
    }

    return this;
  }

  public static Resources from(Reader reader, Converter converter) throws IOException {
    return converter.fromReader(reader);
  }

  public static Resources from(String string, Converter converter) throws IOException {
    return from(new StringReader(string), converter);
  }

  public static Resources fromJson(Reader reader) throws IOException {
    return from(reader, new JsonConverter());
  }

  public static Resources fromJson(String string) throws IOException {
    return from(string, new JsonConverter());
  }

  public static Resources fromJson(InputStream reader) throws IOException {
    return fromJson(new InputStreamReader(reader));
  }
}
