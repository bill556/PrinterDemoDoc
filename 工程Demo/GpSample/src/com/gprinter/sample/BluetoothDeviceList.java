package com.gprinter.sample;
import java.util.Set;

import com.sample.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BluetoothDeviceList extends Activity {
	// Debugging
	private static final String DEBUG_TAG = "DeviceListActivity";
	public static LinearLayout deviceNamelinearLayout;
	// Member fields
	private ListView lvPairedDevice = null, lvNewDevice = null;
	private TextView tvPairedDevice = null, tvNewDevice = null;
	private Button btDeviceScan = null;
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	//	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.dialog_bluetooth_list);		
		Log.e(DEBUG_TAG, "On Create");
		// TextView �����?
		tvPairedDevice = (TextView)findViewById(R.id.tvPairedDevices);
        //ListView �����?
		lvPairedDevice = (ListView)findViewById(R.id.lvPairedDevices);
	  // TextView �µ�
		tvNewDevice = (TextView)findViewById(R.id.tvNewDevices);
		// ListView �µ�
		lvNewDevice = (ListView)findViewById(R.id.lvNewDevices);		
		// Button ɨ���豸
		btDeviceScan = (Button)findViewById(R.id.btBluetoothScan);
		btDeviceScan.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setVisibility(View.GONE);
				discoveryDevice();
			}		
		});			
		getDeviceList();
	}
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        if(mFindBlueToothReceiver != null)
        	this.unregisterReceiver(mFindBlueToothReceiver);
    }
    
	protected void getDeviceList() {
		// Initialize array adapters. One for already paired devices and
		// one for newly discovered devices
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.bluetooth_device_name_item);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.bluetooth_device_name_item);
		lvPairedDevice.setAdapter(mPairedDevicesArrayAdapter);
		lvPairedDevice.setOnItemClickListener(mDeviceClickListener);
		lvNewDevice.setAdapter(mNewDevicesArrayAdapter);
		lvNewDevice.setOnItemClickListener(mDeviceClickListener);
		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mFindBlueToothReceiver, filter);
		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mFindBlueToothReceiver, filter);
		// Get the local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			tvPairedDevice.setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device.getName() + "\n"
						+ device.getAddress());
			}
		} else {
			String noDevices = getResources().getText(R.string.none_paired)
					.toString();
			mPairedDevicesArrayAdapter.add(noDevices);
		}
	}
	// changes the title when discovery is finished
	private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					mNewDevicesArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle(R.string.select_bluetooth_device);
				Log.i("tag", "finish discovery" +mNewDevicesArrayAdapter.getCount()); 
				if (mNewDevicesArrayAdapter.getCount() == 0) {
					String noDevices = getResources().getText(
							R.string.none_bluetooth_device_found).toString();
  				    mNewDevicesArrayAdapter.add(noDevices);
				}
			}
		}
	};
	private void discoveryDevice() {
        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scaning);
        // Turn on sub-title for new devices
        tvNewDevice.setVisibility(View.VISIBLE);

        lvNewDevice.setVisibility(View.VISIBLE);
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }
	  // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        @Override
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String noDevices = getResources().getText(R.string.none_paired).toString();
            String noNewDevice = getResources().getText(R.string.none_bluetooth_device_found).toString();
            Log.i("tag", info);       
            if (! info.equals(noDevices) && ! info.equals(noNewDevice)) {
				String address = info.substring(info.length() - 17);
				// Create the result Intent and include the MAC address
				Intent intent = new Intent();
				intent.putExtra(PortConfigurationActivity.EXTRA_DEVICE_ADDRESS, address);
				// Set result and finish this Activity
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
        }
    };

}
