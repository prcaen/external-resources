package fr.prcaen.externalresources.listener;

import fr.prcaen.externalresources.exception.ExternalResourceException;

public interface OnExternalResourcesLoadFailedListener {
  void onExternalResourcesLoadFailed(ExternalResourceException e);
}
