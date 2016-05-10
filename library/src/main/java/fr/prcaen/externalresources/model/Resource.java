package fr.prcaen.externalresources.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    if (isBoolean() && null != getAsNonPrimitiveBoolean()) {
      return getAsNonPrimitiveBoolean();
    } else {
      return Boolean.parseBoolean(getAsString());
    }
  }

  @Nullable
  public String getAsString() {
    if (isNumber() && null != getAsNumber()) {
      return getAsNumber().toString();
    } else if (isBoolean() && null != getAsNonPrimitiveBoolean()) {
      return getAsNonPrimitiveBoolean().toString();
    } else if (isString()) {
      return (String) value;
    } else {
      return null;
    }
  }

  @Nullable
  public Float getAsFloat() {
    if (isNumber() && null != getAsNumber()) {
      return getAsNumber().floatValue();
    } else if (null != getAsString()) {
      try {
        return Float.parseFloat(getAsString());
      } catch (NumberFormatException e) {
        return null;
      }
    } else {
      return null;
    }
  }

  @Nullable
  public Integer getAsInt() {
    if (isNumber() && null != getAsNumber()) {
      return getAsNumber().intValue();
    } else if (null != getAsString()) {
      try {
        return Integer.parseInt(getAsString());
      } catch (NumberFormatException e) {
        return null;
      }
    } else {
      return null;
    }
  }

  public int[] getAsIntegerArray() {
    if (isArray()) {
      Resource[] resources = getAsArray();
      int[] integers = new int[resources.length];

      for (int i = 0; i < resources.length; i++) {
        Resource resource = resources[i];

        if (null != resource.getAsInt()) {
          integers[i] = resource.getAsInt();
        }
      }

      return integers;
    } else {
      return new int[]{};
    }
  }

  @NonNull
  public String[] getAsStringArray() {
    if (isArray()) {
      Resource[] resources = getAsArray();
      String[] strings = new String[resources.length];

      for (int i = 0; i < resources.length; i++) {
        Resource resource = resources[i];

        strings[i] = resource.getAsString();
      }

      return strings;
    } else if (null != getAsString()) {
      return getAsString().split("(?!^)");
    } else {
      return new String[]{};
    }
  }

  @Nullable
  protected Number getAsNumber() {
    if (isString()) {
      return new LazilyParsedNumber((String) value);
    } else if (isNumber()) {
      return (Number) value;
    } else {
      return null;
    }
  }

  @NonNull
  protected Resource[] getAsArray() {
    try {
      return (Resource[]) value;
    } catch (ClassCastException e) {
      return new Resource[]{};
    }
  }

  @Nullable
  protected Boolean getAsNonPrimitiveBoolean() {
    try {
      return (Boolean) value;
    } catch (ClassCastException e) {
      return null;
    }
  }
}
