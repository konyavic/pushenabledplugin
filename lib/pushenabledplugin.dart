import 'dart:async';

import 'package:flutter/services.dart';

class Pushenabledplugin {
  static const MethodChannel _channel =
      const MethodChannel('com.konyavic.pushenabledplugin/pushenabledplugin');

  static Future<bool> get areNotificationsEnabled async {
    final bool enabled = await _channel.invokeMethod('areNotificationsEnabled');
    return enabled;
  }
}
