package fr.prcaen.externalresources.listener;

import fr.prcaen.externalresources.ExternalResources;

/**
 * Callback interface for use with {@link ExternalResources#register}
 * and {@link  ExternalResources#unregister}.
 */
public interface OnExternalResourcesChangeListener {
  /**
   * This is called when external resources are loaded or when configuration changed.
   * {@see} ExternalResources.Builder#options
   *
   * @param externalResources the ExternalResources instance.
   */
  void onExternalResourcesChange(ExternalResources externalResources);
}
