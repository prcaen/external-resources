package fr.prcaen.externalresources.listener;

public interface OnExternalResourcesLoadListener extends OnExternalResourcesChangeListener {
  void onExternalResourcesLoadFailed(Exception exception);
}
