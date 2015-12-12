package fr.prcaen.externalresources;

import android.content.Context;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.prcaen.externalresources.exception.ExternalResourceException;
import fr.prcaen.externalresources.model.Resources;

import static fr.prcaen.externalresources.ResourcesRunnable.RETRY_COUNT;

public final class Dispatcher {

  private static final int REQUEST_LAUNCH = 1;
  private static final int REQUEST_RETRY = 2;
  private static final int NETWORK_STATE_CHANGE = 3;
  private static final int AIRPLANE_MODE_CHANGE = 4;

  public static final int REQUEST_DONE = 5;
  public static final int REQUEST_FAILED = 6;

  private static final int RETRY_DELAY = 1000;

  private final Context context;
  private final ExecutorService service;
  private final DispatcherThread dispatcherThread;
  private final NetworkBroadcastReceiver networkBroadcastReceiver;
  private final Handler handler;
  private final Handler mainHandler;

  @Nullable
  private NetworkInfo networkInfo;
  private boolean airPlaneMode;
  private ResourcesRunnable resourcesRunnable;
  private boolean needReplay = false;

  public Dispatcher(@NonNull Context context, @NonNull Downloader downloader, @NonNull Handler mainHandler, @Cache.Policy int cachePolicy) {
    this.context = context;
    this.service = Executors.newSingleThreadExecutor();
    this.dispatcherThread = new DispatcherThread();
    this.dispatcherThread.start();
    this.mainHandler = mainHandler;
    this.handler = new DispatcherHandler(dispatcherThread.getLooper(), this);
    this.networkBroadcastReceiver = new NetworkBroadcastReceiver(context, this);
    this.networkBroadcastReceiver.register();

    this.networkInfo = Utils.getActiveNetworkInfo(context);
    this.airPlaneMode = Utils.isAirplaneModeOn(context);
    this.resourcesRunnable = new ResourcesRunnable(downloader, this, cachePolicy);
  }

  public void stop() {
    service.shutdown();
    dispatcherThread.quit();
    networkBroadcastReceiver.unregister();
  }

  public void dispatchLaunch() {
    Logger.v(ExternalResources.TAG, "dispatch launch");
    handler.sendMessage(handler.obtainMessage(REQUEST_LAUNCH));
  }

  public void dispatchRetry() {
    Logger.v(ExternalResources.TAG, "dispatch retry");
    handler.sendMessageDelayed(handler.obtainMessage(REQUEST_RETRY), RETRY_DELAY);
  }

  public void dispatchFailed(ExternalResourceException e) {
    Logger.v(ExternalResources.TAG, "dispatch failed");
    mainHandler.sendMessage(mainHandler.obtainMessage(REQUEST_FAILED, e));
  }

  public void dispatchDone(Resources resources) {
    Logger.v(ExternalResources.TAG, "dispatch launch");
    mainHandler.sendMessage(mainHandler.obtainMessage(REQUEST_DONE, resources));
  }

  public void dispatchAirplaneModeChange(boolean airPlaneMode) {
    handler.sendMessage(handler.obtainMessage(AIRPLANE_MODE_CHANGE, airPlaneMode));
  }

  public void dispatchNetworkStateChange(@NonNull NetworkInfo networkInfo) {
    handler.sendMessage(handler.obtainMessage(NETWORK_STATE_CHANGE, networkInfo));
  }

  private void performLaunch() {
    boolean canRetryConnectivity = networkInfo == null || networkInfo.isConnected();

    if (!airPlaneMode && canRetryConnectivity) {
      Logger.v(ExternalResources.TAG, "perform launch");
      service.submit(resourcesRunnable);
    } else {
      Logger.v(ExternalResources.TAG, "wait until connectivity");
      this.needReplay = true;
    }
  }

  private void performRetry() {
    boolean canRetryConnectivity = networkInfo == null || networkInfo.isConnected();

    if (resourcesRunnable.canRetry() && !airPlaneMode && canRetryConnectivity) {
      Logger.v(ExternalResources.TAG, "perform retry");
      resourcesRunnable.decreaseRetryCount();
      service.submit(resourcesRunnable);
    } else if (resourcesRunnable.canRetry()) {
      Logger.v(ExternalResources.TAG, "wait until connectivity");
      this.needReplay = true;
    } else {
      dispatchFailed(new ExternalResourceException("Perform retry failed after " + RETRY_COUNT));
    }
  }

  private void performAirPlaneModeChange(boolean airPlaneMode) {
    this.airPlaneMode = airPlaneMode;

    if(!Utils.hasNetworkStatePermission(context)) {
      performReplay();
    }
  }

  private void performNetworkStateChange(NetworkInfo networkInfo) {
    this.networkInfo = networkInfo;

    performReplay();
  }

  private void performReplay() {
    if (!needReplay || airPlaneMode || (networkInfo != null && !networkInfo.isConnected())) {
      return;
    }

    Logger.v(ExternalResources.TAG, "perform replay");
    performLaunch();
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
          dispatcher.performRetry();
          break;
        case NETWORK_STATE_CHANGE:
          dispatcher.performNetworkStateChange((NetworkInfo) message.obj);
          break;
        case AIRPLANE_MODE_CHANGE:
          dispatcher.performAirPlaneModeChange((Boolean) message.obj);
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
