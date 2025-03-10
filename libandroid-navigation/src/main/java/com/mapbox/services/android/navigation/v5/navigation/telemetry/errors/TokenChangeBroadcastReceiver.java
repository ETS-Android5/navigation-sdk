package com.mapbox.services.android.navigation.v5.navigation.telemetry.errors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.mapbox.services.android.navigation.v5.navigation.telemetry.MapboxTelemetryConstants;

/**
 * This broadcast receiver notifies when valid token becomes available.
 * Note: we're not broadcasting users token, we're just interested in getting
 * notified when it becomes available for the first time.
 */
public class TokenChangeBroadcastReceiver extends BroadcastReceiver {
  private static final String LOG_TAG = "TknBroadcastReceiver";

  /**
   * Register receiver with local broadcast manager.
   *
   * @param context valid context.
   */
  public static void register(@NonNull Context context) {
    LocalBroadcastManager.getInstance(context).registerReceiver(new TokenChangeBroadcastReceiver(),
      new IntentFilter(MapboxTelemetryConstants.ACTION_TOKEN_CHANGED));
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    try {
      // Start background job
      ErrorReporterJobIntentService.enqueueWork(context);
      // Unregister receiver - we need it once at startup
      LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    } catch (Throwable throwable) {
      // TODO: log silent crash
      Log.e(LOG_TAG, throwable.toString());
    }
  }
}
