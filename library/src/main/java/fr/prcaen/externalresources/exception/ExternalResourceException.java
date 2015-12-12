package fr.prcaen.externalresources.exception;

import java.io.IOException;

public class ExternalResourceException extends IOException {

  public ExternalResourceException() {
  }

  public ExternalResourceException(String detailMessage) {
    super(detailMessage);
  }

  public ExternalResourceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExternalResourceException(Throwable cause) {
    super(cause);
  }

}
