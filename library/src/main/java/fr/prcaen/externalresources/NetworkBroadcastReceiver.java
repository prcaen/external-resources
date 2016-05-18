package fr.prcaen.externalresources;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import static android.content.Intent.ACTION_AIRPLANE_MODE_CHANGED;
import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

  private static final String EXTRA_AIRPLANE_STATE = "state";

  private final Context context;
  private final Dispatcher dispatcher;

  public NetworkBroadcastReceiver(@NonNull Context context, @NonNull Dispatcher dispatcher) {
    this.context = context;
    this.dispatcher = dispatcher;
  }

  public void register() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(ACTION_AIRPLANE_MODE_CHANGED);
    if (Utils.hasNetworkStatePermission(context)) {
      filter.addAction(CONNECTIVITY_ACTION);
    }

    context.registerReceiver(this, filter);
  }

  public void unregister() {
    context.unregisterReceiver(this);
  }

  @Override public void onReceive(Context context, Intent intent) {
    if (null == intent) {
      return;
    }

    final String action = intent.getAction();

    if (ACTION_AIRPLANE_MODE_CHANGED.equals(action) && intent.hasExtra(EXTRA_AIRPLANE_STATE)) {
      dispatcher.dispatchAirplaneModeChange(intent.getBooleanExtra(EXTRA_AIRPLANE_STATE, false));
    } else if (CONNECTIVITY_ACTION.equals(action)) {
      NetworkInfo networkInfo = Utils.getActiveNetworkInfo(context);
      if (null != networkInfo) {
        dispatcher.dispatchNetworkStateChange(networkInfo);
      }
    }
  }
}
