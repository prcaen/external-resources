package fr.prcaen.externalresources;

public final class Options {
  private final boolean useFontScale;
  private final boolean useHardKeyboardHidden;
  private final boolean useKeyboard;
  private final boolean useLocale;
  private final boolean useKeyboardHidden;
  private final boolean useMcc;
  private final boolean useMnc;
  private final boolean useNavigation;
  private final boolean useNavigationHidden;
  private final boolean useOrientation;
  private final boolean useScreenLayout;
  private final boolean useTouchscreen;
  private final boolean useUiMode;
  private final boolean useDensityDpi;
  private final boolean useScreenWidthDp;
  private final boolean useScreenHeightDp;
  private final boolean useSmallestScreenWidthDp;

  private Options(Builder builder) {
    this.useFontScale = builder.useFontScale;
    this.useHardKeyboardHidden = builder.useHardKeyboardHidden;
    this.useKeyboard = builder.useKeyboard;
    this.useLocale = builder.useLocale;
    this.useKeyboardHidden = builder.useKeyboardHidden;
    this.useMcc = builder.useMcc;
    this.useMnc = builder.useMnc;
    this.useNavigation = builder.useNavigation;
    this.useNavigationHidden = builder.useNavigationHidden;
    this.useOrientation = builder.useOrientation;
    this.useScreenLayout = builder.useScreenLayout;
    this.useTouchscreen = builder.useTouchscreen;
    this.useUiMode = builder.useUiMode;
    this.useDensityDpi = builder.useDensityDpi;
    this.useScreenWidthDp = builder.useScreenWidthDp;
    this.useScreenHeightDp = builder.useScreenHeightDp;
    this.useSmallestScreenWidthDp = builder.useSmallestScreenWidthDp;
  }

  public static Options createDefault() {
    return new Builder().build();
  }

  public boolean isUseFontScale() {
    return useFontScale;
  }

  public boolean isUseHardKeyboardHidden() {
    return useHardKeyboardHidden;
  }

  public boolean isUseKeyboard() {
    return useKeyboard;
  }

  public boolean isUseLocale() {
    return useLocale;
  }

  public boolean isUseKeyboardHidden() {
    return useKeyboardHidden;
  }

  public boolean isUseMcc() {
    return useMcc;
  }

  public boolean isUseMnc() {
    return useMnc;
  }

  public boolean isUseNavigation() {
    return useNavigation;
  }

  public boolean isUseNavigationHidden() {
    return useNavigationHidden;
  }

  public boolean isUseOrientation() {
    return useOrientation;
  }

  public boolean isUseScreenLayout() {
    return useScreenLayout;
  }

  public boolean isUseTouchscreen() {
    return useTouchscreen;
  }

  public boolean isUseUiMode() {
    return useUiMode;
  }

  public boolean isUseDensityDpi() {
    return useDensityDpi;
  }

  public boolean isUseScreenWidthDp() {
    return useScreenWidthDp;
  }

  public boolean isUseScreenHeightDp() {
    return useScreenHeightDp;
  }

  public boolean isUseSmallestScreenWidthDp() {
    return useSmallestScreenWidthDp;
  }

  public static class Builder {
    private boolean useFontScale = true;
    private boolean useHardKeyboardHidden = true;
    private boolean useKeyboard = true;
    private boolean useLocale = true;
    private boolean useKeyboardHidden = true;
    private boolean useMcc = true;
    private boolean useMnc = true;
    private boolean useNavigation = true;
    private boolean useNavigationHidden = true;
    private boolean useOrientation = true;
    private boolean useScreenLayout = true;
    private boolean useTouchscreen = true;
    private boolean useUiMode = true;
    private boolean useDensityDpi = true;
    private boolean useScreenWidthDp = true;
    private boolean useScreenHeightDp = true;
    private boolean useSmallestScreenWidthDp = true;

    public Builder setUseFontScale(boolean useFontScale) {
      this.useFontScale = useFontScale;

      return this;
    }

    public Builder setUseHardKeyboardHidden(boolean useHardKeyboardHidden) {
      this.useHardKeyboardHidden = useHardKeyboardHidden;

      return this;
    }

    public Builder setUseKeyboard(boolean useKeyboard) {
      this.useKeyboard = useKeyboard;

      return this;
    }

    public Builder setUseLocale(boolean useLocale) {
      this.useLocale = useLocale;

      return this;
    }

    public Builder setUseKeyboardHidden(boolean useKeyboardHidden) {
      this.useKeyboardHidden = useKeyboardHidden;

      return this;
    }

    public Builder setUseMcc(boolean useMcc) {
      this.useMcc = useMcc;

      return this;
    }

    public Builder setUseMnc(boolean useMnc) {
      this.useMnc = useMnc;

      return this;
    }

    public Builder setUseNavigation(boolean useNavigation) {
      this.useNavigation = useNavigation;

      return this;
    }

    public Builder setUseNavigationHidden(boolean useNavigationHidden) {
      this.useNavigationHidden = useNavigationHidden;

      return this;
    }

    public Builder setUseOrientation(boolean useOrientation) {
      this.useOrientation = useOrientation;

      return this;
    }

    public Builder setUseScreenLayout(boolean useScreenLayout) {
      this.useScreenLayout = useScreenLayout;

      return this;
    }

    public Builder setUseTouchscreen(boolean useTouchscreen) {
      this.useTouchscreen = useTouchscreen;

      return this;
    }

    public Builder setUseUiMode(boolean useUiMode) {
      this.useUiMode = useUiMode;

      return this;
    }

    public Builder setUseDensityDpi(boolean useDensityDpi) {
      this.useDensityDpi = useDensityDpi;

      return this;
    }

    public Builder setUseScreenWidthDp(boolean useScreenWidthDp) {
      this.useScreenWidthDp = useScreenWidthDp;

      return this;
    }

    public Builder setUseScreenHeightDp(boolean useScreenHeightDp) {
      this.useScreenHeightDp = useScreenHeightDp;

      return this;
    }

    public Builder setUseSmallestScreenWidthDp(boolean useSmallestScreenWidthDp) {
      this.useSmallestScreenWidthDp = useSmallestScreenWidthDp;

      return this;
    }

    public Options build() {
      return new Options(this);
    }
  }
}
