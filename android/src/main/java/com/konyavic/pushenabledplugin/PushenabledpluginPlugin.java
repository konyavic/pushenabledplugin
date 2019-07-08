package com.konyavic.pushenabledplugin;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** PushenabledpluginPlugin */
public class PushenabledpluginPlugin implements MethodCallHandler {
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "com.konyavic.pushenabledplugin/pushenabledplugin");
    channel.setMethodCallHandler(new PushenabledpluginPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("areNotificationsEnabled")) {
      result.success(areNotificationsEnabled());
    } else {
      result.notImplemented();
    }
  }

  private static Application application = null;

  static Application getApplication() {
      if (application == null) {
        try {
          @SuppressLint("PrivateApi") Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
          Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
          application = (Application)(activityThreadClass.getMethod("getApplication").invoke(activityThread));
        } catch (Throwable t) {
          Log.e("pushenabledplugin", "failed to get Application object.");
        }
      }
      return application;
  }

  static boolean areNotificationsEnabled() {
    // Check whether the notification is enabled or not.
    // The following implementation is refer to android.support.v4.app.NotificationManagerCompat.areNotificationsEnabled() in support-compat-24 library.

    final Context context = getApplication();

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      // API 18 or below
      return true;
    } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
      // API 19 - 23
      AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
      ApplicationInfo appInfo = context.getApplicationInfo();
      String pkg = context.getApplicationContext().getPackageName();
      int uid = appInfo.uid;
      try {
        Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
        Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
        Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
        int value = (int) opPostNotificationValue.get(Integer.class);
        return ((int) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
      } catch (Throwable t) {
        return true;
      }
    } else {
      // API 24 or above
      final NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
      return notificationManager.areNotificationsEnabled();
    }
  }
}
