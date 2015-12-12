package fr.prcaen.externalresources.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException() {
  }

  public NotFoundException(String name) {
    super(name);
  }

}
