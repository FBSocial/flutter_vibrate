package flutter.plugins.vibrate;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

public class VibratePlugin implements FlutterPlugin, ActivityAware {
    private MethodChannel methodChannel;
    private VibrateMethodCallHandler methodCallHandler;

    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        final Context context = binding.getApplicationContext();
        final BinaryMessenger messenger = binding.getBinaryMessenger();
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        methodCallHandler = new VibrateMethodCallHandler(vibrator, null);

        this.methodChannel = new MethodChannel(messenger, "vibrate");
        this.methodChannel.setMethodCallHandler(methodCallHandler);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        this.methodCallHandler.setActivity(binding.getActivity());
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivity() {
        this.methodCallHandler.setActivity(null);
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding binding) {
        this.methodChannel.setMethodCallHandler(null);
        this.methodChannel = null;
        this.methodCallHandler.setActivity(null);
    }
}
