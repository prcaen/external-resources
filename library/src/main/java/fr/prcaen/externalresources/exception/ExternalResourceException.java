package fr.prcaen.externalresources.exception;

public class ExternalResourceException extends RuntimeException {

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
