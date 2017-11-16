package com.gprinter.sample;

import com.sample.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by fycsb on 2016/7/8.
 */
public class Huibo {

    private Bitmap mLogo;
    private String companyName;
    private String address;
    private String bindNumber;
    private String resNumber;
    private String code128Number;
    private String mujuanNumber;
    private String productName;
    private String trademark;
    private String size;
    private String nw;
    private String gw;
    private String number;
    private String reservoir;
    private String productLocation;
    private String receiveUnit;
    private String date;

    public String getQrcodeContent() {
        return qrcodeContent;
    }

    public void setQrcodeContent(String qrcodeContent) {
        this.qrcodeContent = qrcodeContent;
    }

    public Bitmap getLogo() {
        return mLogo;
    }

    public void setLogo(Bitmap logo) {
        mLogo = logo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBindNumber() {
        return bindNumber;
    }

    public void setBindNumber(String bindNumber) {
        this.bindNumber = bindNumber;
    }

    public String getResNumber() {
        return resNumber;
    }

    public void setResNumber(String resNumber) {
        this.resNumber = resNumber;
    }

    public String getCode128Number() {
        return code128Number;
    }

    public void setCode128Number(String code128Number) {
        this.code128Number = code128Number;
    }

    public String getMujuanNumber() {
        return mujuanNumber;
    }

    public void setMujuanNumber(String mujuanNumber) {
        this.mujuanNumber = mujuanNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTrademark() {
        return trademark;
    }

    public void setTrademark(String trademark) {
        this.trademark = trademark;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getNw() {
        return nw;
    }

    public void setNw(String nw) {
        this.nw = nw;
    }

    public String getGw() {
        return gw;
    }

    public void setGw(String gw) {
        this.gw = gw;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReservoir() {
        return reservoir;
    }

    public void setReservoir(String reservoir) {
        this.reservoir = reservoir;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public String getReceiveUnit() {
        return receiveUnit;
    }

    public void setReceivedUnit(String receiveUnit) {
        this.receiveUnit = receiveUnit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String qrcodeContent;

    public static Huibo getHuiboTestData(Context context) {
        Huibo huibo = new Huibo();
        huibo.setLogo(BitmapFactory.decodeResource(context.getResources(), R.drawable.huibo_logo));
        huibo.setCompanyName("青岛汇博通商贸有限公司");
        huibo.setAddress("地址：青岛");
        huibo.setBindNumber("捆包号：R103571234");
        huibo.setResNumber("钢厂资源号：ML123456789");
        huibo.setCode128Number("1234567890");
        huibo.setMujuanNumber("母卷号：P123456789");
        huibo.setProductName("品名：冷轧卷");
        huibo.setTrademark("牌号：DC06");
        huibo.setSize("规格：1.2*1250*C");
        huibo.setNw("净重：10.5t");
        huibo.setGw("毛重：10.8t");
        huibo.setNumber("件数：1");
        huibo.setReservoir("库区：原料区");
        huibo.setProductLocation("库位：YC-02");
        huibo.setReceivedUnit("收货单位:英大");
        huibo.setDate("日期:2016.06.12");
        huibo.setQrcodeContent("捆包号|钢厂资源号|母卷号|品名|牌号|规格|净重|毛重");
        return huibo;
    }
}
