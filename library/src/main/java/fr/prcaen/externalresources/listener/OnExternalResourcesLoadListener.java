package fr.prcaen.externalresources.listener;

import fr.prcaen.externalresources.exception.ExternalResourceException;

public interface OnExternalResourcesLoadListener extends OnExternalResourcesChangeListener {
  void onExternalResourcesLoadFailed(ExternalResourceException e);
}
