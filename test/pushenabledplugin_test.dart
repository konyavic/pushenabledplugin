import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:pushenabledplugin/pushenabledplugin.dart';

void main() {
  const MethodChannel channel = MethodChannel('com.konyavic.pushenabledplugin/pushenabledplugin');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return true;
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('areNotificationsEnabled', () async {
    expect(await Pushenabledplugin.areNotificationsEnabled, true);
  });
}
