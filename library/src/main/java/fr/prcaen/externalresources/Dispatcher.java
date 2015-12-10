package fr.prcaen.externalresources;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.prcaen.externalresources.model.Resources;

public final class Dispatcher {

  private static final int REQUEST_LAUNCH = 1;
  private static final int REQUEST_RETRY = 2;
  public static final int REQUEST_DONE = 3;
  public static final int REQUEST_FAILED = 4;

  private static final int RETRY_DELAY = 500;

  private final DispatcherThread dispatcherThread;
  private final ExecutorService service;
  private final Downloader downloader;
  private final Handler handler;
  @Cache.Policy
  private final int cachePolicy;
  private final Handler mainHandler;

  public Dispatcher(@NonNull Context context, @NonNull Downloader downloader, @NonNull Handler mainHandler, @Cache.Policy int cachePolicy) {
    this.service = Executors.newSingleThreadExecutor();
    this.dispatcherThread = new DispatcherThread();
    this.dispatcherThread.start();
    this.downloader = downloader;
    this.mainHandler = mainHandler;
    this.handler = new DispatcherHandler(dispatcherThread.getLooper(), this);
    this.cachePolicy = cachePolicy;
  }

  public void stop() {
    service.shutdown();
    dispatcherThread.quit();
  }

  public void dispatchLaunch() {
    Logger.v(ExternalResources.TAG, "dispatch launch");
    handler.sendMessage(handler.obtainMessage(REQUEST_LAUNCH));
  }

  public void dispatchRetry(ResourcesRunnable resourcesRunnable) {
    Logger.v(ExternalResources.TAG, "dispatch retry");
    handler.sendMessageDelayed(handler.obtainMessage(REQUEST_RETRY, resourcesRunnable), RETRY_DELAY);
  }

  public void dispatchFailed(Exception e) {
    Logger.v(ExternalResources.TAG, "dispatch failed");
    mainHandler.sendMessage(mainHandler.obtainMessage(REQUEST_FAILED, e));
  }

  public void dispatchDone(Resources resources) {
    Logger.v(ExternalResources.TAG, "dispatch launch");
    mainHandler.sendMessage(mainHandler.obtainMessage(REQUEST_DONE, resources));
  }

  private void performLaunch() {
    Logger.v(ExternalResources.TAG, "perform launch");
    service.submit(new ResourcesRunnable(downloader, this, cachePolicy));
  }

  private void performRetry(ResourcesRunnable resourcesRunnable) {
    Logger.v(ExternalResources.TAG, "perform retry");
    service.submit(resourcesRunnable);
  }

  private static class DispatcherHandler extends Handler {

    private final Dispatcher dispatcher;

    public DispatcherHandler(Looper looper, Dispatcher dispatcher) {
      super(looper);
      this.dispatcher = dispatcher;
    }

    @Override
    public void handleMessage(Message message) {
      switch (message.what) {
        case REQUEST_LAUNCH:
          dispatcher.performLaunch();
          break;
        case REQUEST_RETRY:
          dispatcher.performRetry((ResourcesRunnable) message.obj);
          break;
        default:
          Logger.e(ExternalResources.TAG, "Unknown message: " + message.what);
          break;
      }
    }

  }

  private static class DispatcherThread extends HandlerThread {

    private static final String THREAD_NAME = "dispatcher-external-resources";

    DispatcherThread() {
      super(THREAD_NAME, Process.THREAD_PRIORITY_BACKGROUND);
    }

  }

}
