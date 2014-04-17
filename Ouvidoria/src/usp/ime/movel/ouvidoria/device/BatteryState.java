package usp.ime.movel.ouvidoria.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

/// Versão simplificada do exemplo do DJ.
public class BatteryState extends BroadcastReceiver {

	private static String TAG = "MyBroadcastReceiver";
	
    private int level = -1;
	
    @Override
	public void onReceive(Context context, Intent intent) {
        int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if (rawlevel >= 0 && scale > 0) {
            setLevel((rawlevel * 100) / scale);
        }
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
    	Log.w(TAG, "BATTERY CHANGED:"+getLevel());
	}
}
