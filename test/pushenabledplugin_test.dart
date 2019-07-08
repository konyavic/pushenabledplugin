import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:pushenabledplugin/pushenabledplugin.dart';

void main() {
  const MethodChannel channel = MethodChannel('pushenabledplugin');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Pushenabledplugin.platformVersion, '42');
  });
}
