/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/Cerie/Desktop/GprinterSDKForAndroid_V2.2.1/GprinterSDKForAndroid/工程Demo/GpSample(AndroidStudio)/app/src/main/aidl/com/gprinter/aidl/GpService.aidl
 */
package com.gprinter.aidl;
public interface GpService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.gprinter.aidl.GpService
{
private static final java.lang.String DESCRIPTOR = "com.gprinter.aidl.GpService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.gprinter.aidl.GpService interface,
 * generating a proxy if needed.
 */
public static com.gprinter.aidl.GpService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.gprinter.aidl.GpService))) {
return ((com.gprinter.aidl.GpService)iin);
}
return new com.gprinter.aidl.GpService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_openPort:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
int _result = this.openPort(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_closePort:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.closePort(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getPrinterConnectStatus:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.getPrinterConnectStatus(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_printeTestPage:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.printeTestPage(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_queryPrinterStatus:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
this.queryPrinterStatus(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_getPrinterCommandType:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.getPrinterCommandType(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sendEscCommand:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
int _result = this.sendEscCommand(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sendLabelCommand:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
int _result = this.sendLabelCommand(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_isUserExperience:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.isUserExperience(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getClientID:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getClientID();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_setServerIP:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _result = this.setServerIP(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.gprinter.aidl.GpService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public int openPort(int PrinterId, int PortType, java.lang.String DeviceName, int PortNumber) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(PrinterId);
_data.writeInt(PortType);
_data.writeString(DeviceName);
_data.writeInt(PortNumber);
mRemote.transact(Stub.TRANSACTION_openPort, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void closePort(int PrinterId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(PrinterId);
mRemote.transact(Stub.TRANSACTION_closePort, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getPrinterConnectStatus(int PrinterId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(PrinterId);
mRemote.transact(Stub.TRANSACTION_getPrinterConnectStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int printeTestPage(int PrinterId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(PrinterId);
mRemote.transact(Stub.TRANSACTION_printeTestPage, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void queryPrinterStatus(int PrinterId, int Timesout, int requestCode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(PrinterId);
_data.writeInt(Timesout);
_data.writeInt(requestCode);
mRemote.transact(Stub.TRANSACTION_queryPrinterStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getPrinterCommandType(int PrinterId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(PrinterId);
mRemote.transact(Stub.TRANSACTION_getPrinterCommandType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int sendEscCommand(int PrinterId, java.lang.String b64) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(PrinterId);
_data.writeString(b64);
mRemote.transact(Stub.TRANSACTION_sendEscCommand, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int sendLabelCommand(int PrinterId, java.lang.String b64) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(PrinterId);
_data.writeString(b64);
mRemote.transact(Stub.TRANSACTION_sendLabelCommand, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void isUserExperience(boolean userExperience) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((userExperience)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_isUserExperience, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String getClientID() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getClientID, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int setServerIP(java.lang.String ip, int port) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(ip);
_data.writeInt(port);
mRemote.transact(Stub.TRANSACTION_setServerIP, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_openPort = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_closePort = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getPrinterConnectStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_printeTestPage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_queryPrinterStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getPrinterCommandType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_sendEscCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_sendLabelCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_isUserExperience = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getClientID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_setServerIP = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
}
public int openPort(int PrinterId, int PortType, java.lang.String DeviceName, int PortNumber) throws android.os.RemoteException;
public void closePort(int PrinterId) throws android.os.RemoteException;
public int getPrinterConnectStatus(int PrinterId) throws android.os.RemoteException;
public int printeTestPage(int PrinterId) throws android.os.RemoteException;
public void queryPrinterStatus(int PrinterId, int Timesout, int requestCode) throws android.os.RemoteException;
public int getPrinterCommandType(int PrinterId) throws android.os.RemoteException;
public int sendEscCommand(int PrinterId, java.lang.String b64) throws android.os.RemoteException;
public int sendLabelCommand(int PrinterId, java.lang.String b64) throws android.os.RemoteException;
public void isUserExperience(boolean userExperience) throws android.os.RemoteException;
public java.lang.String getClientID() throws android.os.RemoteException;
public int setServerIP(java.lang.String ip, int port) throws android.os.RemoteException;
}
