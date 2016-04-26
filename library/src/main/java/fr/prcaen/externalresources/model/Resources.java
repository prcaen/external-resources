package fr.prcaen.externalresources.model;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import fr.prcaen.externalresources.converter.Converter;
import fr.prcaen.externalresources.converter.JsonConverter;
import fr.prcaen.externalresources.converter.XmlConverter;

@SuppressWarnings("unused")
public final class Resources {
  protected final ConcurrentHashMap<String, Resource> members;

  public Resources() {
    this.members = new ConcurrentHashMap<>();
  }

  public Resources(@NonNull ConcurrentHashMap<String, Resource> members) {
    this.members = members;
  }

  public Resource add(String key, Resource value) {
    return members.put(key, value);
  }

  @NonNull
  protected Set<Entry<String, Resource>> entrySet() {
    return members.entrySet();
  }

  public boolean has(String key) {
    return members.containsKey(key);
  }

  public Resource get(String key) {
    return members.get(key);
  }

  public Resources merge(Resources resources) {
    for (Entry<String, Resource> entry : resources.entrySet()) {
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

  public static Resources fromXml(Reader reader) throws IOException {
    return from(reader, new XmlConverter());
  }

  public static Resources fromXml(String string) throws IOException {
    return from(string, new XmlConverter());
  }

  public static Resources fromXml(InputStream reader) throws IOException {
    return fromXml(new InputStreamReader(reader));
  }
}
