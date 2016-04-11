/**                                                                                                                                                                                  
 *    Copyright 2016-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.l88.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具
 * 
 * @author alexpaul@126.com
 *
 */
public class Dates {
	public static final String TIME_FORMAT_Y_M_D = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 时间格式(年-月-日）
	 */
	public static final String DATE_FORMAT_YMD_LONG = "yyyy-MM-dd";

	/**
	 * 时间格式(年月日）
	 */
	public static final String DATE_FORMAT_YMD = "yyyyMMdd";

	/**
	 * 时间格式（年月）
	 */
	public static final String DATE_FORMAT_YM = "yyyyMM";

	/**
	 * 时间格式（年）
	 */
	public static final String DATE_FORMAT_Y = "yyyy";
	/**
	 * 使用格式 <b>_pattern </b>格式化日期输出
	 * 
	 * @param _date
	 *            日期对象
	 * @param _pattern
	 *            日期格式
	 * @return 格式化后的日期
	 */
	public static String format(java.util.Date _date, String _pattern) {
		if (_date == null) {
			return "";
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(_pattern);
		String stringDate = simpleDateFormat.format(_date);

		return stringDate;
	}

	/**
	 * 使用格式 {@link #DATE_FORMAT_YMD}格式化日期输出
	 * 
	 * @param _date
	 *            日期对象
	 * @return 格式化后的日期
	 */
	public static String format(java.util.Date _date) {
		return format(_date, DATE_FORMAT_YMD);
	}
	
	/**
	 * 取得当前时间，返回Timestamp对象
	 * 
	 * @param
	 * @return Timestamp。
	 */
	public static Timestamp timestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 根据给定格式，返回字符串格式的当前时间
	 * 
	 * @param format
	 *            给定格式，若给定格式为空，则格式默认为"yyyy-MM-dd HH:mm:ss"
	 * @return String。
	 */
	public static String nowString(String format) {
		if (null == format || "".equals(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		return format(new Date(), format);
	}
	
	/**
	 * 将java.util.Date对象转换为java.sql.Timestamp对象
	 * 
	 * @param value
	 * @return java.sql.Timestamp。
	 */
	public static Timestamp toTimestamp(java.util.Date value) {
		if (value == null)
			return null;
		return new Timestamp(value.getTime());
	}
	
	/**
	 * 日期取整. 仅保留日期部分，时间部分全部置零
	 * <p>
	 * 如sDate为null,则返回当前日期
	 * <p>常用于日期比较
	 * @param sDate
	 * @return
	 */
	public static Date omitForDate(Date sDate) {
		Date tDate = sDate == null ? new Date() : sDate;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 时间取整到秒. 日期置为1900-1-1，时间部分不变
	 * <p>
	 * 如sDate为null,则返回当前时间
	 * <p>常用于时间比较
	 * @param sDate
	 * @return
	 */
	public static Date omitForTime(Date sDate) {
		Date tDate = sDate == null ? new Date() : sDate;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tDate);
		calendar.set(Calendar.YEAR, 1900);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 取得某月总天数
	 * 
	 * @param year
	 *            int 年（例2004）
	 * @param month
	 *            int 月（1-12）
	 * @return int 当月天数
	 */
	public static int daysOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		return calendar.getActualMaximum(Calendar.DATE);
	}
}
