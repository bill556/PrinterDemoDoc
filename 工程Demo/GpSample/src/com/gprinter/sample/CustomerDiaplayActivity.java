package com.gprinter.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.gprinter.io.CustomerDisplay;
import com.gprinter.io.GpEquipmentPort;
import com.sample.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CustomerDiaplayActivity extends Activity implements View.OnClickListener, GpEquipmentPort.OnDataReceived {

	private CustomerDisplay port;

	private EditText mInputText;
	private EditText mEtDisplayTimeout;
	private EditText mEtX;
	private EditText mEtY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom);

		mEtDisplayTimeout = (EditText) findViewById(R.id.et_display_timeout);
		mEtX = (EditText) findViewById(R.id.et_x);
		mEtY = (EditText) findViewById(R.id.et_y);
		mInputText = (EditText) findViewById(R.id.et_input_text);

		findViewById(R.id.btnClear).setOnClickListener(this);
		findViewById(R.id.btnReset).setOnClickListener(this);
		findViewById(R.id.btnDisplayBitmap).setOnClickListener(this);
		findViewById(R.id.btnSetTextCurrentCursor).setOnClickListener(this);
		findViewById(R.id.btnSetTextBebindCursor).setOnClickListener(this);
		findViewById(R.id.btnTurnOnBackLight).setOnClickListener(this);
		findViewById(R.id.btnSetDisplayTimeout).setOnClickListener(this);
		findViewById(R.id.btnSetCursorPosition).setOnClickListener(this);
		findViewById(R.id.btnOffBackLight).setOnClickListener(this);
		findViewById(R.id.btnGetCursorPosition).setOnClickListener(this);
		findViewById(R.id.btnGetDisplayRowAndColumn).setOnClickListener(this);
		findViewById(R.id.btnGetDisplayStatus).setOnClickListener(this);
		findViewById(R.id.btnGetDisplayTimeout).setOnClickListener(this);
		findViewById(R.id.btnSetContrast).setOnClickListener(this);
		findViewById(R.id.btnBrightness).setOnClickListener(this);
		findViewById(R.id.btnOpenPort).setOnClickListener(this);
		findViewById(R.id.btnClosePort).setOnClickListener(this);
		findViewById(R.id.btnGetPortStatus).setOnClickListener(this);

		findViewById(R.id.btnAllBlack).setOnClickListener(this);
		findViewById(R.id.btnAllWhite).setOnClickListener(this);
		findViewById(R.id.btnRecovery).setOnClickListener(this);
		findViewById(R.id.btnUsCn).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int clickId = v.getId();
		if (clickId == R.id.btnOpenPort) {
			openPort();
			return;
		} else if (clickId == R.id.btnClosePort) {

			closePort();
			return;
		}
		if (port == null || !port.isPortOpen()) {
			toast("请打开端口");

			return;
		}
		switch (clickId) {
		case R.id.btnGetPortStatus:
			getPortStatus();
			break;
		case R.id.btnReset:
			reset();
			break;
		case R.id.btnDisplayBitmap:
			displayBitmap();
			break;
		case R.id.btnClear:
			clear();
			break;
		case R.id.btnTurnOnBackLight:
			turnOnBacklight();
			break;
		case R.id.btnOffBackLight:
			turnOffBacklight();
			break;
		case R.id.btnSetDisplayTimeout:
			setDisplayTimeout();
			break;
		case R.id.btnSetCursorPosition:
			setCursorPosition();
			break;
		case R.id.btnSetTextCurrentCursor:
			setTextCurrentCursor();
			break;
		case R.id.btnSetTextBebindCursor:
			setTextBebindCursor();
			break;
		case R.id.btnGetDisplayStatus:
			getDisplayStatus();
			break;
		case R.id.btnGetCursorPosition:
			getCursorPosition();
			break;
		case R.id.btnGetDisplayRowAndColumn:
			getDisplayRowAndColumn();
			break;
		case R.id.btnGetDisplayTimeout:
			getDisplayTimeout();
			break;
		case R.id.btnSetContrast:
			setContrast();
			break;
		case R.id.btnBrightness:
			setBrightness();
			break;
		case R.id.btnAllBlack:
			allBack();
			break;
		case R.id.btnAllWhite:
			allWhite();
			break;
		case R.id.btnRecovery:
			recovery();
			break;
		case R.id.btnUsCn:
			ench();
			break;
		}
	}


	/**
	 * 测试用
	 */
	private void allBack() {
		port.setContrast((byte) 21);
	}

	private void recovery() {
		port.setContrast((byte) 11);
	}

	private void allWhite() {
		port.setContrast((byte) 0);
	}

	private void ench() {
		String english = "abcdefghijklmnopqrstuvwxyz";
		String chinese = "自摸连连\n东南西北中发白\n春夏秋冬梅兰菊";
		port.setTextBebindCursor(english);
		port.setTextBebindCursor(chinese);
	}

	//
	private void openPort() {
		port = CustomerDisplay.getInstance(this);
		try {
			// 打开端口
			port.openPort();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 设置监听回调数据
		port.setReceivedListener(this);
		port.getBacklightTimeout();
	}

	private void closePort() {
		if (port != null) {
			port.closeSerialPort();
			toast("关闭端口");
		}
	}

	private void getPortStatus() {

		if (port != null) {
			toast("端口状态：" + (port.isPortOpen() ? "打开" : "关闭"));
		} else {
			toast("端口状态：关闭");
		}

	}

	private void setBrightness() {
		byte brightness = Byte.valueOf(((Spinner) findViewById(R.id.spinner_brightness)).getSelectedItem().toString());
		port.setBrightness(brightness);
	}

	private void setContrast() {
		byte contrast = Byte.valueOf(((Spinner) findViewById(R.id.spinner_contrast)).getSelectedItem().toString());
		port.setContrast(contrast);
	}

	/**
	 * set 部分
	 */
	private void turnOffBacklight() {
		port.setBacklight(false);
	}

	private void reset() {
		port.reset();
	}

	private void clear() {
		port.clear();
	}

	private void displayBitmap() {
		Bitmap bitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.raw.hani));
		port.displayBitmap(bitmap, 48);
	}

	private void turnOnBacklight() {
		port.setBacklight(true);
	}

	private void setDisplayTimeout() {
		String displayTimeoutStr = mEtDisplayTimeout.getText().toString();
		if (TextUtils.isEmpty(displayTimeoutStr)) {
			Toast.makeText(CustomerDiaplayActivity.this, R.string.str_input_display_timeout, Toast.LENGTH_SHORT).show();
			return;
		}

		int timeout = Integer.parseInt(displayTimeoutStr);
		port.setBacklightTimeout(timeout);

	}

	private void setCursorPosition() {
		String xStr = mEtX.getText().toString();
		String yStr = mEtY.getText().toString();
		if (TextUtils.isEmpty(xStr) || TextUtils.isEmpty(yStr)) {
			Toast.makeText(this, R.string.str_please_input_x_y, Toast.LENGTH_SHORT).show();
			return;
		}

		int x = Integer.parseInt(xStr);
		int y = Integer.parseInt(yStr);

		port.setCursorPosition(x, y);
	}

	/**
	 * 得到的inputText的GB2312编码的长度小于256且大于0
	 */
	private void setTextCurrentCursor() {
		String inputText = mInputText.getText().toString();
		port.setTextCurrentCursor(inputText);
	}

	/**
	 * 得到的inputText的GB2312编码的长度小于256且大于0
	 */
	private void setTextBebindCursor() {
		String inputText = mInputText.getText().toString();
		port.setTextBebindCursor(inputText);
	}

	/**
	 * get 部分
	 */

	private void getDisplayStatus() {
		port.getBacklight();
	}

	private void getDisplayTimeout() {
		port.getBacklightTimeout();
	}

	private void getCursorPosition() {
		port.getCursorPosition();
	}

	private void getDisplayRowAndColumn() {
		port.getDisplayRowAndColumn();
	}

	/**
	 * 获取客显屏的状态开启或关闭
	 *
	 * @param isOpen
	 *            客显屏背光灯开启或关闭
	 */
	@Override
	public void onPortOpen(boolean isOpen) {
		if (isOpen) {
			toast("打开端口成功");
		} else {
			toast("打开端口失败");
		}

	}

	/**
	 * 获取客显屏背光灯开启或关闭
	 *
	 * @param isOn
	 *            客显屏背光灯开启或关闭
	 */
	@Override
	public void onBacklightStatus(final boolean isOn) {
		Log.d("==onBacklightStatus==", String.valueOf(isOn));

		toast("==onBacklightStatus== 背光灯状态->" + String.valueOf(isOn));
	}

	/**
	 * 获取客显屏光标的位置
	 *
	 * @param x
	 *            横坐标
	 * @param y
	 *            纵坐标
	 */
	@Override
	public void onCursorPosition(final int x, final int y) {
		toast("==onCursorPosition==x = " + x + ",y =" + y);
		Log.d("==onCursorPosition==", "x坐标 = " + x + ",y坐标 =" + y);
	}

	/**
	 * 获取客显屏的行和列
	 *
	 * @param row
	 *            行
	 * @param column
	 *            列
	 */
	@Override
	public void onDisplayRowAndColumn(final int row, final int column) {
		toast("行数 = " + row + ",列数 =" + column);
		Log.d("==onCursorPosition==", "row = " + row + ",column =" + column);
	}

	/**
	 * 获取客显屏背光灯超时时间
	 *
	 * @param timeout
	 *            单位：秒
	 */
	@Override
	public void onBacklightTimeout(final int timeout) {
		toast("超时时间 = " + timeout);
		Log.d("==onBacklightTimeout==", "timeout = " + timeout);
	}

	/**
	 * 更新客显固件完成回调方法
	 */
	@Override
	public void onUpdateSuccess() {
//		if (mProgressDialog != null) {
//			mProgressDialog.dismiss();
//		}
//		Toast.makeText(this, "更新完成", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpdateFail(String error) {
//		if (mProgressDialog != null) {
//			mProgressDialog.dismiss();
//		}
//		Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 销毁activity时，关闭端口
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (port != null) {
			port.closeSerialPort();
		}
	}

	private void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
