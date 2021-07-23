package com.softtek.assetworx_api.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssetworxUtil {

	public static List<Date> getReminderDates(Date from, Date to) {
		List<Date> dates = new ArrayList<Date>();
		long diff = to.getTime() - from.getTime();
		dates.add(new Date(from.getTime() + ((75 * diff) / 100)));
		dates.add(new Date(from.getTime() + ((95 * diff) / 100)));
		dates.add(new Date(from.getTime() + ((100 * diff) / 100)));
		return dates;
	}



}
