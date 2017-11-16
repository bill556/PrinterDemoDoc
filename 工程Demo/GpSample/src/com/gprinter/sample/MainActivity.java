package com.gprinter.sample;

import java.util.Vector;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.EscCommand.ENABLE;
import com.gprinter.command.EscCommand.FONT;
import com.gprinter.command.EscCommand.HRI_POSITION;
import com.gprinter.command.EscCommand.JUSTIFICATION;
import com.gprinter.command.GpCom;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.io.utils.GpUtils;
import com.gprinter.service.GpPrintService;
import com.sample.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private GpService mGpService = null;
	public static final String CONNECT_STATUS = "connect.status";
	private static final String DEBUG_TAG = "MainActivity";
	private PrinterServiceConnection conn = null;
	private int mPrinterIndex = 0;
	private int mTotalCopies = 0;
	private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;

	class PrinterServiceConnection implements ServiceConnection {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("ServiceConnection", "onServiceDisconnected() called");
			mGpService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mGpService = GpService.Stub.asInterface(service);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e(DEBUG_TAG, "onResume");
	}

	private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver(){@Override public void onReceive(Context context,Intent intent){String action=intent.getAction();

	// GpCom.ACTION_DEVICE_REAL_STATUS 为广播的IntentFilter
	if(action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)){

	// 业务逻辑的请求码，对应哪里查询做什么操作
	int requestCode=intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE,-1);
	// 判断请求码，是则进行业务操作
	if(requestCode==MAIN_QUERY_PRINTER_STATUS){

	int status=intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS,16);String str;if(status==GpCom.STATE_NO_ERR){str="打印机正常";}else{str="打印机 ";if((byte)(status&GpCom.STATE_OFFLINE)>0){str+="脱机";}if((byte)(status&GpCom.STATE_PAPER_ERR)>0){str+="缺纸";}if((byte)(status&GpCom.STATE_COVER_OPEN)>0){str+="打印机开盖";}if((byte)(status&GpCom.STATE_ERR_OCCURS)>0){str+="打印机出错";}if((byte)(status&GpCom.STATE_TIMES_OUT)>0){str+="查询超时";}}Toast.makeText(getApplicationContext(),"打印机："+mPrinterIndex+" 状态："+str,Toast.LENGTH_SHORT).show();}}}};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.e(DEBUG_TAG, "onCreate");
		// startService();
		connection();
		CheckBox checkbox = (CheckBox) findViewById(R.id.btCheckBox);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				try {
					mGpService.isUserExperience(isChecked);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
	}

	private void connection() {
		conn = new PrinterServiceConnection();
		Intent intent = new Intent(this, GpPrintService.class);
		bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
	}

	public boolean[] getConnectState() {
		boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
		for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
			state[i] = false;
		}
		for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
			try {
				if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
					state[i] = true;
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return state;
	}

	public void openPortDialogueClicked(View view) {
		if (mGpService == null) {
			Toast.makeText(this, "Print Service is not start, please check it", Toast.LENGTH_SHORT).show();
			return;
		}
		Log.d(DEBUG_TAG, "openPortConfigurationDialog ");
		Intent intent = new Intent(this, PrinterConnectDialog.class);
		boolean[] state = getConnectState();
		intent.putExtra(CONNECT_STATUS, state);
		this.startActivity(intent);
	}

	public void printTestPageClicked(View view) {
		try {
			int rel = mGpService.printeTestPage(mPrinterIndex); //
			Log.i("ServiceConnection", "rel " + rel);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void getPrinterStatusClicked(View view) {
		try {
			mTotalCopies = 0;
			mGpService.queryPrinterStatus(mPrinterIndex, 500, MAIN_QUERY_PRINTER_STATUS);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void getPrinterCommandTypeClicked(View view) {
		try {
			int type = mGpService.getPrinterCommandType(mPrinterIndex);
			if (type == GpCom.ESC_COMMAND) {
				Toast.makeText(getApplicationContext(), "打印机使用ESC命令", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "打印机使用TSC命令", Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	void sendReceipt() {
		EscCommand esc = new EscCommand();
		esc.addPrintAndFeedLines((byte) 3);
		esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印居中
		esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.ON, ENABLE.ON, ENABLE.OFF);// 设置为倍高倍宽
		esc.addText("Sample\n"); // 打印文字
		esc.addPrintAndLineFeed();

		/* 打印文字 */
		esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF);// 取消倍高倍宽
		esc.addSelectJustification(JUSTIFICATION.LEFT);// 设置打印左对齐
		esc.addText("Print text\n"); // 打印文字
		esc.addText("Welcome to use Gprinter!\n"); // 打印文字

		/* 打印繁体中文 需要打印机支持繁体字库 */
		String message = "佳博票據打印機\n";
		// esc.addText(message,"BIG5");
		esc.addText(message, "GB2312");
		esc.addPrintAndLineFeed();

		/* 绝对位置 具体详细信息请查看GP58编程手册 */
		esc.addText("你好啊");
		esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
		esc.addSetAbsolutePrintPosition((short) 6);
		esc.addText("你好啊");
		esc.addSetAbsolutePrintPosition((short) 10);
		esc.addText("你好啊");
		esc.addPrintAndLineFeed();

		/* 打印图片 */
		// esc.addText("Print bitmap!\n"); // 打印文字
		// Bitmap b = BitmapFactory.decodeResource(getResources(),
		// R.drawable.gprinter);
		// esc.addRastBitImage(b, b.getWidth(), 0); // 打印图片

		/* 打印一维条码 */
		// esc.addText("Print code128\n"); // 打印文字
		// esc.addSelectPrintingPositionForHRICharacters(HRI_POSITION.BELOW);//
		// 设置条码可识别字符位置在条码下方
		// esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
		// esc.addSetBarcodeWidth((byte) 1); // 设置条码单元宽度为1
		// esc.addCODE128(esc.genCodeB("Gprinter")); // 打印Code128码
		// esc.addPrintAndLineFeed();

		/*
		 * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
		 */
		// esc.addText("Print QRcode\n"); // 打印文字
		// esc.addSelectErrorCorrectionLevelForQRCode((byte)0x31); //设置纠错等级
		// esc.addSelectSizeOfModuleForQRCode((byte)3);//设置qrcode模块大小
		// esc.addStoreQRCodeData("www.gprinter.com.cn");//设置qrcode内容
		// esc.addPrintQRCode();//打印QRCode
		// esc.addPrintAndLineFeed();

		/* 打印文字 */
		esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印左对齐
		esc.addText("Completed!\r\n"); // 打印结束
		esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
		// esc.addGeneratePluseAtRealtime(LabelCommand.FOOT.F2, (byte) 8);

		esc.addPrintAndFeedLines((byte) 8);

		Vector<Byte> datas = esc.getCommand(); // 发送数据
		Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
		Log.d("diaonilaomu", datas.toString());
		byte[] bytes = GpUtils.ByteTo_byte(Bytes);
		String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rs;
		try {
			rs = mGpService.sendEscCommand(mPrinterIndex, sss);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void sendReceipt3() {
		EscCommand esc = new EscCommand();
		esc.addText("1234567890\n"); // 打印文字
		Vector<Byte> datas = esc.getCommand(); // 发送数据
		Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
		byte[] bytes = GpUtils.ByteTo_byte(Bytes);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel;
		try {
			rel = mGpService.sendEscCommand(mPrinterIndex, str);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void sendReceipt(int i) {
		EscCommand esc = new EscCommand();
		esc.addPrintAndFeedLines((byte) 3);
		esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印居中
		esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.ON, ENABLE.ON, ENABLE.OFF);// 设置为倍高倍宽
		esc.addText("Sample\n"); // 打印文字
		esc.addPrintAndLineFeed();

		/* 打印文字 */
		esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF);// 取消倍高倍宽
		esc.addSelectJustification(JUSTIFICATION.LEFT);// 设置打印左对齐
		esc.addText("Print text\n"); // 打印文字
		esc.addText("Welcome to use Gprinter!\n"); // 打印文字

		/* 打印繁体中文 需要打印机支持繁体字库 */
		String message = Util.SimToTra("佳博票据打印机\n");
		// esc.addText(message,"BIG5");
		esc.addText(message, "GB2312");
		esc.addPrintAndLineFeed();

		/* 打印图片 */
		esc.addText("Print bitmap!\n"); // 打印文字
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
		esc.addRastBitImage(b, b.getWidth(), 0); // 打印图片

		/* 打印一维条码 */
		esc.addText("Print code128\n"); // 打印文字
		esc.addSelectPrintingPositionForHRICharacters(HRI_POSITION.BELOW);// 设置条码可识别字符位置在条码下方
		esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
		esc.addSetBarcodeWidth((byte) 1); // 设置条码单元宽度为1
		esc.addCODE128("Gprinter"); // 打印Code128码
		esc.addPrintAndLineFeed();

		/*
		 * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
		 */
		esc.addText("Print QRcode\n"); // 打印文字
		esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
		esc.addSelectSizeOfModuleForQRCode((byte) 3);// 设置qrcode模块大小
		esc.addStoreQRCodeData("www.gprinter.com.cn");// 设置qrcode内容
		esc.addPrintQRCode();// 打印QRCode
		esc.addPrintAndLineFeed();

		esc.addText("第 " + i + " 份\n"); // 打印文字
		/* 打印文字 */
		esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印左对齐
		esc.addText("Completed!\r\n"); // 打印结束
		esc.addPrintAndFeedLines((byte) 8);

		Vector<Byte> datas = esc.getCommand(); // 发送数据
		Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
		byte[] bytes = GpUtils.ByteTo_byte(Bytes);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel;
		try {
			rel = mGpService.sendEscCommand(mPrinterIndex, str);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void sendReceiptBmp(int i) {
		EscCommand esc = new EscCommand();
		/* 打印图片 */
		esc.addText("Print bitmap!\n"); // 打印文字
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.test);
		esc.addRastBitImage(b, 384, 0); // 打印图片
		esc.addText("第 " + i + " 份\n"); // 打印文字

		Vector<Byte> datas = esc.getCommand(); // 发送数据
		Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
		byte[] bytes = GpUtils.ByteTo_byte(Bytes);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel;
		try {
			rel = mGpService.sendEscCommand(mPrinterIndex, str);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void sendLabel() {
//		LabelCommand tsc = new LabelCommand();
//		tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
//		tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
//		tsc.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);// 设置打印方向
//		tsc.addReference(0, 0);// 设置原点坐标
//		tsc.addTear(ENABLE.ON); // 撕纸模式开启
//		tsc.addCls();// 清除打印缓冲区
//		// 绘制简体中文
//		tsc.addText(20, 20, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//				"Welcome to use Gprinter!");
//		// 绘制图片
//		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//		tsc.addBitmap(20, 50, BITMAP_MODE.OVERWRITE, b.getWidth(), b);
//
//		tsc.addQRCode(250, 80, EEC.LEVEL_L, 5, ROTATION.ROTATION_0, " www.gprinter.com.cn");
//		// 绘制一维条码
//		tsc.add1DBarcode(20, 250, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, "Gprinter");
//		tsc.addPrint(1, 1); // 打印标签
//		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
//		tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
//		Vector<Byte> datas = tsc.getCommand(); // 发送数据
//		Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
//		byte[] bytes = GpUtils.ByteTo_byte(Bytes);
//		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
//		int rel;
//		try {
//			rel = mGpService.sendLabelCommand(mPrinterIndex, str);
//			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
//			if (r != GpCom.ERROR_CODE.SUCCESS) {
//				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
//			}
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	 Huibo huibo = Huibo.getHuiboTestData(this);
	    LabelCommand labelCommand = new LabelCommand();
	    labelCommand.addSize(80, 72);
	    labelCommand.addGap(0);
	    labelCommand.addCls();
	    //画框
	    //左边框
	    labelCommand.addBar(16, 0, 4, 556);
	    //上边框
	    labelCommand.addBar(16, 0, 608, 8);
	    //右边框
	    labelCommand.addBar(624, 0, 4, 556);
	    //下边框
	    labelCommand.addBar(16, 556, 608, 4);
	
	    //图片单元格竖线
	    labelCommand.addBar(16 + 130, 0, 2, 106);
	
	
	    labelCommand.addBitmap(16 + 8, 8,
	            LabelCommand.BITMAP_MODE.OVERWRITE,
	            110,
	            huibo.getLogo());
	
	    labelCommand.addText(16 + 130 + 2, 8,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getCompanyName());
	
	    //图片单元格横线
	    labelCommand.addBar(16 + 130, 56, 640 - 130 - 16 - 16, 2);
	
	
	    labelCommand.addText(16 + 130 + 2, 58,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getAddress());
	
	
	    //二维码旁边竖线
	    labelCommand.addBar(16 + 320, 106, 2, 556 - 106);
	
	    //横线
	    labelCommand.addBar(16, 106, 608, 2);
	    labelCommand.addText(16 + 4, 106 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2, huibo.getBindNumber());
	
	    labelCommand.addText(16 + 320 + 2, 106 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getResNumber());
	
	    //横线
	    labelCommand.addBar(16 + 320 + 2, 156, 608, 2);
	    labelCommand.add1DBarcode(16 + 16, 156 + 2,
	            LabelCommand.BARCODETYPE.CODE128,
	            48, LabelCommand.READABEL.DISABLE,
	            LabelCommand.ROTATION.ROTATION_0,
	            huibo.getCode128Number());
	
	    labelCommand.addText(16 + 320 + 2, 156 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getMujuanNumber());
	
	    //横线
	    labelCommand.addBar(16, 206, 608, 2);
	    labelCommand.addText(16 + 4, 206 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getProductName());
	
	    labelCommand.addText(16 + 320 + 2, 206 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getTrademark());
	
	
	
	    //横线
	    labelCommand.addBar(16, 256, 608, 2);
	    labelCommand.addText(16 + 4, 256 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getSize());
	
	    labelCommand.addText(16 + 320 + 2, 256 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getNw());
	
	
	
	
	    //横线
	    labelCommand.addBar(16, 306, 608, 2);
	    labelCommand.addText(16 + 4, 306 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getGw());
	
	    labelCommand.addText(16 + 320 + 2, 306 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getNumber());
	
	
	
	    //横线
	    labelCommand.addBar(16, 356, 608, 2);
	    labelCommand.addText(16 + 4, 356 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getReservoir());
	
	    labelCommand.addQRCode(16 + 320 + 2 + 100,356 + 2 + 25,
	            LabelCommand.EEC.LEVEL_L,
	            5,
	            LabelCommand.ROTATION.ROTATION_0,
	            huibo.getQrcodeContent());
	
	    //横线
	    labelCommand.addBar(16, 406, 320, 2);
	    labelCommand.addText(16 + 4, 406 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getProductLocation());
	
	    //横线
	    labelCommand.addBar(16, 456, 320, 2);
	    labelCommand.addText(16 + 4, 456 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getReceiveUnit());
	
	
	
	
	    //横线
	    labelCommand.addBar(16, 506, 320, 2);
	    labelCommand.addText(16 + 4, 506 + 2,
	            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
	            LabelCommand.ROTATION.ROTATION_0,
	            LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
	            huibo.getDate());
	
	
	
	    labelCommand.addPrint(1);
	    
		Vector<Byte> datas = labelCommand.getCommand(); // 发送数据
		Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
		byte[] bytes = GpUtils.ByteTo_byte(Bytes);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel;
		try {
			rel = mGpService.sendLabelCommand(mPrinterIndex, str);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }



	public void printReceiptClicked(View view) {
		try {
			int type = mGpService.getPrinterCommandType(mPrinterIndex);
			if (type == GpCom.ESC_COMMAND) {
				sendReceipt();
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void printLabelClicked(View view) {
		try {
			int type = mGpService.getPrinterCommandType(mPrinterIndex);
			if (type == GpCom.LABEL_COMMAND) {
				sendLabel();
				mGpService.queryPrinterStatus(mPrinterIndex, 1000, MAIN_QUERY_PRINTER_STATUS);
				// if (status == GpCom.STATE_NO_ERR) {
				// } else {
				// Toast.makeText(getApplicationContext(), "打印机错误！",
				// Toast.LENGTH_SHORT).show();
				// }
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void printTestClicked(View view) {
		try {
			int type = mGpService.getPrinterCommandType(mPrinterIndex);
			if (type == GpCom.ESC_COMMAND) {
				EditText etCopies = (EditText) findViewById(R.id.etPrintCopies);
				String str = etCopies.getText().toString();
				int copies = 0;
				if (!str.equals("")) {
					copies = Integer.parseInt(str);
				}
				mTotalCopies += copies;
				for (int i = 0; i < copies; i++) {
					sendReceipt();
				}
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void customerDisplayerClicked(View view) {
		Intent intent = new Intent(this, CustomerDiaplayActivity.class);
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		Log.e(DEBUG_TAG, "onDestroy");
		super.onDestroy();
		if (conn != null) {
			unbindService(conn); // unBindService
		}
		unregisterReceiver(mBroadcastReceiver);
	}

}
