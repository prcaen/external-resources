package fr.prcaen.externalresources.exception;

import fr.prcaen.externalresources.Cache;

public final class ResponseException extends ExternalResourceException {

  private final boolean localCacheOnly;
  private final int responseCode;

  public ResponseException(String message, @Cache.Policy int networkPolicy, int responseCode) {
    super(message);

    this.localCacheOnly = networkPolicy == Cache.POLICY_OFFLINE;
    this.responseCode = responseCode;
  }

  public boolean isLocalCacheOnly() {
    return localCacheOnly;
  }

  public int getResponseCode() {
    return responseCode;
  }

}