package com.gprinter.sample;
import com.gprinter.service.GpPrintService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceBootReceiver extends BroadcastReceiver {
		   /**开机广播**/
		 static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
		/** {@inheritDoc} */
			@Override
			public void onReceive(Context context, Intent intent) {
		        if (intent.getAction().equals(BOOT_COMPLETED)) {
		            Intent i = new Intent(context, GpPrintService.class);
		            context.startService(i);
		            Log.i("DeviceBootReceiver", "GpPrintService.start");
		        }
		}
}
