package fr.prcaen.externalresources;

import android.support.annotation.NonNull;

import java.io.IOException;

import fr.prcaen.externalresources.model.Resources;

public final class ResourcesRunnable implements Runnable {
  private final Downloader downloader;
  private final Listener listener;
  @Cache.Policy
  private final int cachePolicy;

  public ResourcesRunnable(@NonNull Downloader downloader, @Cache.Policy int policy, @NonNull Listener listener) {
    this.downloader = downloader;
    this.listener = listener;
    this.cachePolicy = policy;
  }

  @Override
  public void run() {
    setName();

    try {
      Resources resources = downloader.load(cachePolicy);
      listener.onResourcesLoadSuccess(resources);
    } catch (IOException e) {
      listener.onResourcesLoadFailed(e);
    }
  }

  private void setName() {
    long threadId = Thread.currentThread().getId();

    Thread.currentThread().setName(threadId + "-external-resources");
  }

  public interface Listener {
    void onResourcesLoadSuccess(Resources resources);
    void onResourcesLoadFailed(Exception e);
  }
}