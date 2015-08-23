package com.eebbk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.renderscript.ProgramFragmentFixedFunction.Builder.Format;
import android.text.format.DateFormat;

public class DateFormatUtils {
	
	public static String formatCurrentTimeToString(long current){
		Date mDate = new Date(current);
		SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return mFormat.format(mDate);
	}
	
	
}
