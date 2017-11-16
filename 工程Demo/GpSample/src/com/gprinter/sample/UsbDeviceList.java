package com.gprinter.sample;

import java.util.HashMap;
import java.util.Iterator;

import com.sample.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UsbDeviceList extends Activity{
	// Debugging
	private static final String DEBUG_TAG = "DeviceListActivity";
	public static LinearLayout deviceNamelinearLayout;
	// Member fields
	private ListView lvUsbDevice = null;
	private ArrayAdapter<String> mUsbDeviceArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	//	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.dialog_usb_list);		
		lvUsbDevice = (ListView)findViewById(R.id.lvUsbDevices);
		mUsbDeviceArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.usb_device_name_item);
		lvUsbDevice.setOnItemClickListener(mDeviceClickListener);
		lvUsbDevice.setAdapter(mUsbDeviceArrayAdapter);
		getUsbDeviceList();
	}
	void messageBox(String err)
	{
		Toast.makeText(getApplicationContext(),
				err,Toast.LENGTH_SHORT).show();	
	}
	boolean checkUsbDevicePidVid(UsbDevice dev) {
		int pid = dev.getProductId();
		int vid = dev.getVendorId();
		boolean rel = false;
		if ((vid == 34918 && pid == 256) || (vid == 1137 && pid == 85)
				|| (vid == 6790 && pid == 30084)
				|| (vid == 26728 && pid == 256) || (vid == 26728 && pid == 512)
				|| (vid == 26728 && pid == 256) || (vid == 26728 && pid == 768)
				|| (vid == 26728 && pid == 1024)|| (vid == 26728 && pid == 1280)
				|| (vid == 26728 && pid == 1536)) {
			rel = true;
		}
		return rel;
	}
	public void getUsbDeviceList()
	{
		UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
		// Get the list of attached devices
		HashMap<String, UsbDevice> devices = manager.getDeviceList();
		Iterator<UsbDevice> deviceIterator = devices.values().iterator();
		int count = devices.size();
		Log.d(DEBUG_TAG, "count "+ count);
		if(count > 0)
		{
			while (deviceIterator.hasNext()) {
				UsbDevice device = deviceIterator.next();
				String devicename = device.getDeviceName();
				if(checkUsbDevicePidVid(device)){
					mUsbDeviceArrayAdapter.add(devicename);	
				}
			} 
		}
		else
		{
			String noDevices = getResources().getText(R.string.none_usb_device)
					.toString();
			Log.d(DEBUG_TAG, "noDevices "+ noDevices);
			mUsbDeviceArrayAdapter.add(noDevices);
		}
	}
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        @Override
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String noDevices = getResources().getText(R.string.none_usb_device).toString();  
            if (! info.equals(noDevices)) {
				String address = info;
				// Create the result Intent and include the MAC address
				Intent intent = new Intent();
				intent.putExtra(PortConfigurationActivity.EXTRA_DEVICE_ADDRESS, address);
				// Set result and finish this Activity
				setResult(Activity.RESULT_OK, intent);
			}
			finish();
        }
    };	
}
