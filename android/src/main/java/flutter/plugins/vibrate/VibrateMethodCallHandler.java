package flutter.plugins.vibrate;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.view.HapticFeedbackConstants;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

class VibrateMethodCallHandler implements MethodChannel.MethodCallHandler {
    private final Vibrator vibrator;
    private final boolean hasVibrator;
    private final Activity activity;

    VibrateMethodCallHandler(Vibrator vibrator, Activity activity) {
        assert (vibrator != null);
        this.vibrator = vibrator;
        this.hasVibrator = vibrator.hasVibrator();
        this.activity = activity;
    }

    @SuppressWarnings("deprecation")
    private void vibrate(int duration) {
        if (hasVibrator) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(duration);
            }
        }
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        if (!(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED)) {
            // 如果没有权限，需要请求权限
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.VIBRATE}, 0);
        }
        switch (call.method) {
            case "canVibrate":
                result.success(hasVibrator);
                break;
            case "vibrate":
                final int duration = call.argument("duration");
                vibrate(duration);
                result.success(null);
                break;
            case "impact":
                vibrate(HapticFeedbackConstants.VIRTUAL_KEY);
                result.success(null);
                break;
            case "selection":
                vibrate(HapticFeedbackConstants.KEYBOARD_TAP);
                result.success(null);
                break;
            case "success":
                vibrate(50);
                result.success(null);
                break;
            case "warning":
                vibrate(250);
                result.success(null);
                break;
            case "error":
                vibrate(500);
                result.success(null);
                break;
            case "heavy":
                vibrate(100);
                result.success(null);
                break;
            case "medium":
                vibrate(40);
                result.success(null);
                break;
            case "light":
                vibrate(10);
                result.success(null);
                break;
            default:
                result.notImplemented();
                break;
        }

    }
}
