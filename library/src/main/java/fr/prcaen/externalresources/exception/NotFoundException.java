package fr.prcaen.externalresources.exception;

public class NotFoundException extends ExternalResourceException {

  public NotFoundException(String name) {
    super(name);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotFoundException(Throwable cause) {
    super(cause);
  }
}
