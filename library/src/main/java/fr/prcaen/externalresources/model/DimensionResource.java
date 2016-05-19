package fr.prcaen.externalresources.model;

import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public final class DimensionResource {
  private static final String TYPE_DP = "dp";
  private static final String TYPE_DIP = "dip";
  private static final String TYPE_SP = "sp";
  private static final String TYPE_PT = "pt";
  private static final String TYPE_IN = "in";
  private static final String TYPE_MM = "mm";
  private static final String TYPE_PX = "px";

  protected final int type;
  protected final float value;

  public DimensionResource(int type, float value) {
    this.type = type;
    this.value = value;
  }

  public static DimensionResource fromString(@NonNull String resource)
      throws IllegalArgumentException {
    return new DimensionResource(getTypeFromString(resource), getValueFromString(resource));
  }

  protected static float getValueFromString(@NonNull String str) {
    String[] split = str.trim().split("[a-z-A-Z]");

    try {
      return Float.valueOf(split[0]);
    } catch (NumberFormatException | IndexOutOfBoundsException e) {
      throw new IllegalArgumentException(str + " is not a valid dimension format.");
    }
  }

  protected static int getTypeFromString(@NonNull String str) {
    String[] split = str.trim().split("[0-9]");

    if (split.length > 0) {
      final String typeStr = split[split.length - 1];

      switch (typeStr) {
        case TYPE_DP:
        case TYPE_DIP:
          return TypedValue.COMPLEX_UNIT_DIP;
        case TYPE_SP:
          return TypedValue.COMPLEX_UNIT_SP;
        case TYPE_PT:
          return TypedValue.COMPLEX_UNIT_PT;
        case TYPE_IN:
          return TypedValue.COMPLEX_UNIT_IN;
        case TYPE_MM:
          return TypedValue.COMPLEX_UNIT_MM;
        case TYPE_PX:
          return TypedValue.COMPLEX_UNIT_PX;
        default:
          throw new IllegalArgumentException(typeStr + " is not a valid type dimension format.");
      }
    } else {
      return TypedValue.COMPLEX_UNIT_PX;
    }
  }

  public float toFloat(DisplayMetrics metrics) {
    return TypedValue.applyDimension(type, value, metrics);
  }
}
