#import "PushenabledpluginPlugin.h"
#import <UserNotifications/UserNotifications.h>

@implementation PushenabledpluginPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"com.konyavic.pushenabledplugin/pushenabledplugin"
            binaryMessenger:[registrar messenger]];
  PushenabledpluginPlugin* instance = [[PushenabledpluginPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"areNotificationsEnabled" isEqualToString:call.method]) {
      [UNUserNotificationCenter.currentNotificationCenter getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings * _Nonnull settings) {
          BOOL enabled = (settings.authorizationStatus == UNAuthorizationStatusAuthorized);
          result([NSNumber numberWithBool:enabled]);
      }];
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end
