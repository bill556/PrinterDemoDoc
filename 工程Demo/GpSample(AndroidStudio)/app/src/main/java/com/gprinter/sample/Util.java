package com.gprinter.sample;

import java.io.IOException;

import taobe.tec.jcc.JChineseConvertor;

public class Util {
	public static String SimToTra(String simpStr) {
		String traditionalStr = null;
		try {
			JChineseConvertor jChineseConvertor = JChineseConvertor
					.getInstance();
			traditionalStr = jChineseConvertor.s2t(simpStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return traditionalStr;
	}
}
