package fr.prcaen.externalresources;

import android.support.annotation.NonNull;

import java.io.IOException;

import fr.prcaen.externalresources.model.Resources;

public final class ResourcesRunnable implements Runnable {

  private static final int RETRY_COUNT = 2;
  private static final String THREAD_NAME_SUFFIX = "-external-resources";

  private final Downloader downloader;
  private final Dispatcher dispatcher;
  @Cache.Policy
  private final int cachePolicy;
  private int retryCount;

  public ResourcesRunnable(@NonNull Downloader downloader, @NonNull Dispatcher dispatcher, @Cache.Policy int policy) {
    this.downloader = downloader;
    this.dispatcher = dispatcher;
    this.cachePolicy = policy;
    this.retryCount = RETRY_COUNT;
  }

  @Override
  public void run() {
    Thread.currentThread().setName(Thread.currentThread().getId() + THREAD_NAME_SUFFIX);

    try {
      Resources resources = downloader.load(cachePolicy);
      if (resources != null) {
        dispatcher.dispatchDone(resources);
      } else {
        dispatcher.dispatchFailed(new NullPointerException("Resources are null."));
      }
    } catch (Downloader.ResponseException e) {
      dispatcher.dispatchFailed(e);
    } catch (IOException e) {
      if (shouldRetry()) {
        dispatcher.dispatchRetry(this);
      } else {
        dispatcher.dispatchFailed(e);
      }
    } catch (Exception e) {
      dispatcher.dispatchFailed(e);
    }
  }

  private boolean shouldRetry() {
    boolean shouldRetry = retryCount > 0;

    retryCount--;

    return shouldRetry;
  }

}