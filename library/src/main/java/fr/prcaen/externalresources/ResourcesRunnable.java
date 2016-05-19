package fr.prcaen.externalresources;

import android.support.annotation.NonNull;
import fr.prcaen.externalresources.exception.ExternalResourceException;
import fr.prcaen.externalresources.exception.ResponseException;
import fr.prcaen.externalresources.model.Resources;

public final class ResourcesRunnable implements Runnable {

  public static final int RETRY_COUNT = 2;
  private static final String THREAD_NAME_SUFFIX = "-external-resources";

  private final Downloader downloader;
  private final Dispatcher dispatcher;
  @Cache.Policy private final int cachePolicy;
  private int retryCount;

  public ResourcesRunnable(@NonNull Downloader downloader, @NonNull Dispatcher dispatcher,
      @Cache.Policy int policy) {
    this.downloader = downloader;
    this.dispatcher = dispatcher;
    this.cachePolicy = policy;
    this.retryCount = RETRY_COUNT;
  }

  @Override public void run() {
    Thread.currentThread().setName(Thread.currentThread().getId() + THREAD_NAME_SUFFIX);

    try {
      Resources resources = downloader.load(cachePolicy);
      if (null != resources) {
        dispatcher.dispatchDone(resources);
      } else {
        dispatcher.dispatchFailed(new ExternalResourceException("Resources are null."));
      }
    } catch (ResponseException e) {
      dispatcher.dispatchFailed(e);
    } catch (ExternalResourceException e) {
      dispatcher.dispatchRetry();
    } catch (Exception e) {
      dispatcher.dispatchFailed(new ExternalResourceException(e));
    }
  }

  public void decreaseRetryCount() {
    retryCount--;
  }

  public boolean canRetry() {
    return retryCount > 0;
  }
}