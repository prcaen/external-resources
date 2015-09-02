package fr.prcaen.externalresources.model;

import com.google.gson.internal.LazilyParsedNumber;

public final class Resource {
  private final Object value;

  public Resource(Boolean value) {
    this.value = value;
  }

  public Resource(Number value) {
    this.value = value;
  }

  public Resource(String value) {
    this.value = value;
  }

  public Resource(Resource[] value) {
    this.value = value;
  }

  public boolean isBoolean() {
    return value instanceof Boolean;
  }

  public boolean isNumber() {
    return value instanceof Number;
  }

  public boolean isString() {
    return value instanceof String;
  }

  public boolean isArray() {
    return value instanceof Resource[];
  }

  public boolean getAsBoolean() {
    if (isBoolean()) {
      return getAsNonPrimitiveBoolean();
    } else {
      return Boolean.parseBoolean(getAsString());
    }
  }

  public Number getAsNumber() {
    return value instanceof String ? new LazilyParsedNumber((String) value) : (Number) value;
  }

  public String getAsString() {
    if (isNumber()) {
      return getAsNumber().toString();
    } else if (isBoolean()) {
      return getAsNonPrimitiveBoolean().toString();
    } else {
      return (String) value;
    }
  }

  public int[] getAsIntegerArray() {
    if (isArray()) {
      Resource[] resources = getAsArray();
      int[] integers = new int[resources.length];

      for (int i = 0; i < resources.length; i++) {
        Resource resource = resources[i];

        integers[i] = resource.getAsInt();
      }

      return integers;
    } else {
      return new int[]{getAsInt()};
    }
  }

  public String[] getAsStringArray() {
    if (isArray()) {
      Resource[] resources = getAsArray();
      String[] strings = new String[resources.length];

      for (int i = 0; i < resources.length; i++) {
        Resource resource = resources[i];

        strings[i] = resource.getAsString();
      }

      return strings;
    } else {
      return getAsString().split("(?!^)");
    }
  }

  public float getAsFloat() {
    return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
  }

  public int getAsInt() {
    return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
  }

  private Resource[] getAsArray() {
    return (Resource[]) value;
  }

  private Boolean getAsNonPrimitiveBoolean() {
    return (Boolean) value;
  }
}
