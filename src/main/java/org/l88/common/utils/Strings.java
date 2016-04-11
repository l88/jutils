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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具
 * 
 * @author alexpaul@126.com
 *
 */
public class Strings {

	// {{ Testing
	/**
	 * 判断是否是空字符串 null和"" 都返回 true
	 *
	 * @param str
	 *            判断的字符串
	 * @return 是否有效
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.equals("");
	}

	/**
	 * 将对象转换为字符串. 若对象为NULL或空则返回缺省字符串. 相当于oracle的Nvl函数
	 * 
	 * @param obj
	 *            待转换对象
	 * @param defaultValue
	 *            缺省值
	 * @return 对象的字符串内容(调用toString()方法)
	 */
	public static String toString(Object obj, String defaultValue) {
		if (obj == null)
			return defaultValue;
		return isEmpty(obj.toString()) ? defaultValue : obj.toString();
	}

	/**
	 * 将对象转换为字符串. 若对象为NULL或空则返回空(0长度)字符串
	 * 
	 * @param obj
	 *            待转换对象
	 * @return 对象的字符串内容(调用toString()方法)
	 */
	public static String nullable(Object obj) {
		return toString(obj, "");
	}
	// }}

	// {{ join & splits
	/**
	 * 把string array or list用给定的符号symbol连接成一个字符串
	 * <p>
	 * null or empty item will be ignored.
	 *
	 * @param list
	 *            需要处理的列表
	 * @param symbol
	 *            链接的符号
	 * @return 处理后的字符串
	 */
	public static String join(List<?> list, String symbol) {
		StringBuffer result = new StringBuffer();
		if (list != null) {
			for (Object o : list) {
				String temp = toString(o, "");
				if (temp.trim().length() > 0)
					result.append(temp).append(symbol);
			}
			if (result.length() > 1) {
				result.setLength(result.length() - symbol.length());
			}
		}
		return result.toString();
	}

	/**
	 * 将多行文本分割为字符串数组（每行一组）.
	 * <p>
	 * 分行符可以是：换行\n（Unix格式)，或回车加换行\r\n(Dos格式).
	 * 
	 * @param str
	 *            待分割文本字符串
	 * @return an array of lines that comprise the string, or null if the string
	 *         specified was null
	 */
	public static String[] splitLines(String str) {
		if (str == null)
			return null;
		BufferedReader br = new BufferedReader(new StringReader(str));
		ArrayList<String> linesList = new ArrayList<String>();
		try {
			String line = br.readLine();
			while (line != null) {
				linesList.add(line);
				line = br.readLine();
			}
		} catch (IOException notGoingToHappenWithAnInMemoryStringReaderEx) {
		}
		return (String[]) linesList.toArray(new String[linesList.size()]);
	}

	/**
	 * 以单个字符为分隔符分割字符串（可选是否截断空格).
	 * 
	 * <p>
	 * 不使用正则表达式，提高性能。
	 * 
	 * @param s
	 *            待分割字符串
	 * @param delimiter
	 *            字符分融符
	 * @param trim
	 *            是否截去前后空格
	 * @return List
	 *         <ul>
	 *         <li><tt>空列表</tt> 如果对象为null.
	 *         <li><tt>含一个空字符串的列表</tt> 如果对象是空字符串.
	 *         </ul>
	 */
	public static List<String> split(String s, char delimiter, boolean trim) {
		List<String> ret = new ArrayList<String>();
		if (s == null) {
			return ret;
		}
		int lastIdx = 0;
		int idx = s.indexOf(delimiter);

		while (idx > 0) {
			String s1 = s.substring(lastIdx, idx);
			if (trim)
				s1 = s1.trim();
			ret.add(s1);

			lastIdx = idx + 1;
			idx = s.indexOf(delimiter, lastIdx);
		}

		String s1 = s.substring(lastIdx);
		if (trim)
			s1 = s1.trim();
		ret.add(s1);

		return ret;
	}

	/**
	 * 以单个字符为分隔符分割字符串，并截去前后空格.
	 * 不使用正则表达式，提高性能。 相当于<code>split(s, delimiter, true)</code>的快捷方式.
	 * <table border="1">
	 * <tr>
	 * <td><strong>源串</strong></td>
	 * <td><strong>结果</strong></td>
	 * </tr>
	 * <tr>
	 * <td>a,&nbsp;,&nbsp;&nbsp;,,&nbsp;b</td>
	 * <td>[a,,,,b]</td>
	 * </tr>
	 * <tr>
	 * <td>a,b&nbsp;&nbsp;&nbsp;c&nbsp;,d</td>
	 * <td>[a,b&nbsp;&nbsp;&nbsp;c,d]</td>
	 * </tr>
	 * </table>
	 * 
	 * @param s
	 *            待分割字符串
	 * @param delimiter
	 *            分融符
	 * @return List
	 *         <ul>
	 *         <li><tt>空列表</tt> 如果对象为null.
	 *         <li><tt>含一个空字符串的列表</tt> 如果对象是空字符串或全空格字符串.
	 *         </ul>
	 */
	public static List<String> split(String s, char delimiter) {
		return split(s, delimiter, true);
	}

	/**
	 * 按空白字符(空格)或逗号分隔字符串. 本方法的返回结果中不包括零长字符串，示例如下：
	 * <table border="1">
	 * <tr>
	 * <td><strong>源串</strong></td>
	 * <td><strong>结果</strong></td>
	 * </tr>
	 * <tr>
	 * <td>a,,,,b</td>
	 * <td>[a, b]</td>
	 * </tr>
	 * <tr>
	 * <td>a,b&nbsp;&nbsp;&nbsp;c,d</td>
	 * <td>[a, b, c, d]</td>
	 * </tr>
	 * </table>
	 * <b>使用了正则表达式，注意性能问题，不能在调用频繁度的地方使用</b>
	 * 
	 * @param input
	 *            源串
	 * @return 字符串列表. <tt>null</tt> 如果源串为null
	 */
	public static List<String> split(String input) {
		return split(input, "[\\s,]+");
	}

	/**
	 * 使用指定的正则表达式分隔字符串.
	 * <p>
	 * 本方法的返回结果中不包括零长字符串（这是与String.split方法的不同之处）
	 * 
	 * @param input
	 *            源串
	 * @param sep
	 *            正则表达式
	 * @return 字符串列表. <tt>null</tt> 如果源串为null
	 */
	public static List<String> split(String input, String sep) {
		if (input == null)
			return null;
		int index = 0;
		List<String> matchList = new ArrayList<String>();
		Matcher m = Pattern.compile(sep).matcher(input);

		// Add segments before each match found
		while (m.find()) {
			if (index < m.start()) {
				String match = input.subSequence(index, m.start()).toString();
				matchList.add(match);
			}
			index = m.end();
		}

		if (index < input.length())
			matchList.add(input.subSequence(index, input.length()).toString());

		return matchList;
	}

	// }}

	// {{replace & escape

	/**
	 * 将源字符串中所有匹配的字符串替换为提供的新字符串(不使用正则表达式).
	 * 
	 * @param input
	 *            源字符串
	 * @param matchString
	 *            匹配字符串
	 * @param newString
	 *            新字符串
	 * @return 替换后的新字符串
	 */
	public static final String replace(String input, String matchString, String newString) {
		int i = 0;
		if ((i = input.indexOf(matchString, i)) >= 0) {
			char[] line2 = input.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = matchString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = input.indexOf(matchString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return input;
	}

	/**
	 * 编码XML标记.
	 * <p>
	 * 将字符串中的XML关键字符用XML标记替换.使其可置于xml文档中.
	 * <ul>
	 * <li>&amp; &nbsp; &amp;amp;
	 * <li>&lt; &nbsp; &amp;lt;
	 * <li>&gt; &nbsp; &amp;gt;
	 * <li>" &nbsp; &amp;quot;
	 * <li>' &nbsp; &amp;apos;
	 * <li>\ &nbsp; \\
	 * </ul>
	 * 
	 * @param xml
	 *            待编码字符串.
	 * @return 编码后的字符串.
	 */
	public static final String escapeXML(String xml) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (xml == null || xml.length() == 0) {
			return xml;
		}
		// Use a StringBuffer in lieu of String concatenation -- it is
		// much more efficient this way.
		StringBuffer buf = new StringBuffer(xml.length());
		char ch = ' ';
		for (int i = 0; i < xml.length(); i++) {
			ch = xml.charAt(i);
			buf.append(escapeXML(ch));
		}
		return buf.toString();
	}

	private static String escapeXML(char ch) {
		if (ch == '<') {
			return ("&lt;");
		} else if (ch == '>') {
			return ("&gt;");
		} else if (ch == '"') {
			return ("&quot;");
		} else if (ch == '\'') {
			return ("&apos;");
		} else if (ch == '&') {
			return ("&amp;");
		}
		return String.valueOf(ch);
	}

	/**
	 * 反编码XML内容字符串. 以得到编码前的内容.
	 * 
	 * @see Strings#escapeForXML(String)
	 * @param input
	 *            待反编码字符串
	 * @return 反编码后的字符串.
	 */
	public static final String unescapeXML(String input) {
		input = replace(input, "&lt;", "<");
		input = replace(input, "&gt;", ">");
		input = replace(input, "&quot;", "\"");
		input = replace(input, "&apos;", "\'");
		return replace(input, "&amp;", "&");
	}

	/**
	 * 编码HTML标记为浏览器可显示格式.
	 * <p>
	 * 将字符串中的HTML关键字符用HTML标记替换 .将换行符转为&lt;br&gt;
	 * <ul>
	 * <li>&amp; &nbsp; &amp;amp;
	 * <li>&lt; &nbsp; &amp;lt;
	 * <li>&gt; &nbsp; &amp;gt;
	 * <li>" &nbsp; &amp;quot;
	 * <li>' &nbsp; &amp;apos;
	 * <li>换行 &nbsp; &lt;br&gt;
	 * <li>回车 &nbsp;
	 * </ul>
	 * 
	 * @see Strings#escapeXML(String).
	 * @param input
	 *            待编码字符串.
	 * @return 编码后的字符串.
	 */
	public static final String escapeHTML(String input) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (input == null || input.length() == 0) {
			return input;
		}
		// Use a StringBuffer in lieu of String concatenation -- it is
		// much more efficient this way.
		StringBuffer buf = new StringBuffer(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '\r') {
				// buf.append("<br>");
			} else if (ch == '\n') {
				buf.append("<br>");
			} else {
				buf.append(escapeXML(ch));
			}
		}
		return buf.toString();
	}

	/**
	 * 将首字母大写,其它不变
	 * 
	 * @param name
	 * @return
	 */
	public static String capitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		char chars[] = name.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}

	/**
	 * 将首字母小写,其它不变
	 * 
	 * @param name
	 * @return
	 */
	public static String unCapitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		char chars[] = name.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);
		return new String(chars);
	}

	/**
	 * 用提供的属性替换字符串中的${NAME}变量
	 * 
	 * @param value
	 *            待替换的字符串
	 * @param staticProp
	 *            静态属性对照表
	 * @return 替换后的字符串
	 */
	public static String replaceVar(final String value, final Map<String, ?> staticProp) {
		StringBuffer sb = new StringBuffer();
		int prev = 0;
		// assert value!=nil
		int pos;
		while ((pos = value.indexOf("$", prev)) >= 0) {
			if (pos > 0) {
				sb.append(value.substring(prev, pos));
			}
			if (pos == (value.length() - 1)) {// $在最后
				sb.append('$');
				prev = pos + 1;
				break;
			} else if (value.charAt(pos + 1) != '{') {
				sb.append('$');
				// sb.append(value.charAt(pos + 1));
				prev = pos + 1; // XXX
			} else {
				int endName = value.indexOf('}', pos);
				if (endName < 0) {
					sb.append(value.substring(pos));
					prev = value.length();
					continue;
				}
				String n = value.substring(pos + 2, endName);
				String v = null;
				if (n != null && staticProp != null && staticProp.get(n) != null) {
					v = staticProp.get(n).toString();
				}
				if (v == null)
					v = "${" + n + "}";

				sb.append(v);
				prev = endName + 1;
			}
		}
		if (prev < value.length())
			sb.append(value.substring(prev));
		return sb.toString();
	}
	// }}

	// {{ HEX，16进制转换
	/**
	 * 将字节转换为16进制字符串显示.
	 * 
	 * @param hash
	 *            待转换字节数组
	 * @return 16进制表示的字符串
	 */
	public static final String toHex(byte hash[]) {
		StringBuffer buf = new StringBuffer(hash.length * 2);
		String stmp = "";

		for (int i = 0; i < hash.length; i++) {
			stmp = (java.lang.Integer.toHexString(hash[i] & 0XFF));
			if (stmp.length() == 1) {
				buf.append(0).append(stmp);
			} else {
				buf.append(stmp);
			}
		}
		return buf.toString();
	}

	/**
	 * 将16进制表示的字符串转换为字节数组.
	 * 
	 * @param hex
	 *            待转换16进制表示的字符串
	 * @return 字节数组
	 *         <ul>
	 *         <li>null:输入字符串为null
	 *         <li>new byte[0]:输入字符串为非法16进制字符串
	 *         </ul>
	 */
	public static final byte[] hexToBytes(String hex) {
		if (null == hex) {
			return null;
		}
		int len = hex.length();
		byte[] bytes = new byte[len / 2];
		String stmp = null;
		try {
			for (int i = 0; i < bytes.length; i++) {
				stmp = hex.substring(i * 2, i * 2 + 2);
				bytes[i] = (byte) Integer.parseInt(stmp, 16);
			}
		} catch (Exception e) {
			return new byte[0];
		}

		return bytes;
	}

	// }}

	// {{ misc
	/**
	 * 转换为紧凑格式的字节数表示
	 * <P>
	 * 最多保留两位小数
	 * 
	 * @param number
	 *            字节数(单位:B)
	 * @return
	 */
	public static final String compactSize(long num) {
		String[] end = new String[] { "B", "K", "M", "G", "T" };
		int i = 0;

		while (num >= 1024l && i < end.length) {
			num /= 1024l;
			i++;
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);

		return nf.format(num) + end[i];
	}

	/**
	 * 获取平台缺省的字符集
	 * 
	 * @return JVM中配置使用的字符集
	 */
	public static String getDefaultCharacterEncoding() {
		// Not available on all platforms
		String charEnc = System.getProperty("file.encoding");
		if (charEnc != null)
			return charEnc;

		// JDK1.4 onwards
		charEnc = new java.io.OutputStreamWriter(new java.io.ByteArrayOutputStream()).getEncoding();

		// jdk1.5
		// charEnc = Charset.defaultCharset().name();
		return charEnc != null ? charEnc : "<unknown charset encoding>";
	}
	// }}

	// {{ 中文
	/**
	 * 将带字符串按Byte位长度取子字符串. 防止带汉字的字符串长度取错 防止出现Exception
	 * 
	 * @param String
	 *            src 源字符串
	 * @param int
	 *            beginIndex 起始位置,0为第一位
	 * @param int
	 *            endIndex 终止位置,1为第一位
	 * @return String
	 */
	public static String substr(String src, int beginIndex, int endIndex) {
		String dest = "";
		if (src == null) {
			return dest;
		}

		byte[] srcByte = src.getBytes();
		byte[] destByte = null;
		int srclen = srcByte.length;
		if (srclen <= beginIndex || beginIndex >= endIndex) {
			return "";
		}

		if (srclen >= endIndex) {
			destByte = new byte[endIndex - beginIndex];
			System.arraycopy(srcByte, beginIndex, destByte, 0, endIndex - beginIndex);
			dest = new String(destByte);
			return dest;
		} else {
			destByte = new byte[srclen - beginIndex];
			System.arraycopy(srcByte, beginIndex, destByte, 0, srclen - beginIndex);
			dest = new String(destByte);
			return dest;
		}
	}

	/**
	 * 处理汉字字串的substr. 将带字符串按Byte位长度取子字符串 防止带汉字的字符串长度取错 防止出现Exception 防止出现半个汉字
	 * 
	 * @param String
	 *            src 源字符串
	 * @param int
	 *            beginIndex 起始位置,0为第一位
	 * @param int
	 *            endIndex 终止位置,1为第一位
	 * @param boolean
	 *            ifAdd
	 *            <ul>
	 *            <li>==true 如果最后是半个汉字，返回长度加一
	 *            <li>==false 如果最后是半个汉字，返回长度减一
	 *            </ul>
	 * @return String
	 * 
	 */
	public static String gbsubstr(String src, int beginIndex, int endIndex, boolean ifAdd) {
		String dest = "";
		dest = substr(src, beginIndex, endIndex);
		if (dest.length() == 0) {
			if (ifAdd) {
				dest = substr(src, beginIndex, endIndex + 1);
			} else {
				dest = substr(src, beginIndex, endIndex - 1);
			}
		}
		return dest;
	}

	/**
	 * 处理汉字字串的substr 将带字符串按Byte位长度取子字符串 防止带汉字的字符串长度取错 防止出现Exception 防止出现半个汉字
	 * 
	 * @param String
	 *            src 源字符串
	 * @param int
	 *            beginIndex 起始位置,0为第一位
	 * @param int
	 *            endIndex 终止位置,1为第一位 如果最后是半个汉字，返回长度减一
	 * 
	 * @return String
	 * 
	 */
	public static String gbsubstr(String src, int beginIndex, int endIndex) {
		return gbsubstr(src, beginIndex, endIndex, false);
	}

	/**
	 * 取带汉字字串的length 将带字符串按Byte位长度取子字符串 防止带汉字的字符串长度取错
	 * 
	 * @param String
	 *            str 源字符串
	 * 
	 * @return int Byte位长度
	 * 
	 */
	public static int gbStrLen(String str) {
		if (str == null) {
			return 0;
		}
		byte[] strByte;
		try {
			strByte = str.getBytes("GB18030");
		} catch (UnsupportedEncodingException e) {
			strByte = str.getBytes();
		}
		return strByte.length;
	}
	// }}

	// {{ 填充
	/**
	 * 用单个字符重复填充一定长度的字符串
	 * 
	 * @param ch
	 *            用于填充的字符
	 * @param len
	 *            被填充的字符串长度
	 * @return 填充后的字符串
	 */
	public static String replicateStr(char ch, int len) {
		String tmpstr = null;
		char[] tmparr = null;

		if (len <= 0) {
			return "";
		}

		tmparr = new char[len];
		for (int i = 0; i < len; i++) {
			tmparr[i] = ch;
		}
		tmpstr = new String(tmparr);

		return tmpstr;
	}

	/**
	 * 左对齐填充定长字符串.
	 * <br/>
	 * 长度不足，则向字符串尾部添加字符填充长度, 可以有汉字
	 * <br/>
	 * 长度超长，则从后截断字符串
	 * 
	 * @param src
	 *            源字符串
	 * @param ch
	 *            用于填充的字符
	 * @param len
	 *            新字符串总长度
	 * @return
	 */
	public static String lFillStr(String src, char ch, int len) {
		int srclen = gbStrLen(src);
		if (srclen == len)
			return src;
		if (srclen > len) {
			return gbsubstr(src, 0, len);
		}
		return src + replicateStr(ch, len - srclen);
	}

	/**
	 * 右对齐填充定长字符串. 向字符串前部添加字符, 处理汉字
	 * 
	 * @param src
	 * @param ch
	 * @param len
	 * @return
	 */
	public static String rFillStr(String src, char ch, int len) {
		return rFillStr(src, ch, len, true);
	}

	/**
	 * 右对齐填充定长字符串.
	 * <br/>
	 * 长度不足，向字符串前部添加字符, 可选是否处理汉字
	 * <br/>
	 * 超长， 从前部截断字符串
	 * 
	 * @param src
	 *            源字符串
	 * @param ch
	 *            用于填充的字符
	 * @param len
	 *            新字符串长度
	 * @param gb
	 *            源字符串是否包含有中文
	 * @return
	 */
	public static String rFillStr(String src, char ch, int len, boolean gb) {
		int srclen = gb ? gbStrLen(src) : src.length();
		if (srclen == len)
			return src;
		else if (srclen > len) {
			return gbsubstr(src, srclen - len, srclen);
		}
		return replicateStr(ch, len - srclen) + src;
	}

	/**
	 * 截断字符串，当字符长度超过指定长度时截断，并将最后3位用“...”替换(长度按字节计算)
	 * 
	 * @param s
	 *            源字符串
	 * @param maxlength
	 *            指定最大长度
	 * @return 字符串
	 *         <ul>
	 *         <li><tt>null</tt> 如果指定的编码不支持.
	 *         <li><tt>原值</tt> 如果小于或等于指定长度.
	 *         <li><tt>""</tt> 如果字符串为null.
	 *         <li><tt>新字符串</tt> 字节长度为指定长度，且最后三位替换为"...".
	 *         </ul>
	 */
	public static String omit(String s, int maxlength) {
		return omit(s, "utf8", maxlength);
	}

	/**
	 * 截断字符串，当字符长度超过指定长度时截断，并将最后3位用“...”替换(长度按字节计算)
	 * 
	 * @param s
	 *            源字符串
	 * @param encoding
	 *            编码
	 * @param maxlength
	 *            指定最大长度
	 * @return 字符串
	 *         <ul>
	 *         <li><tt>null</tt> 如果指定的编码不支持.
	 *         <li><tt>原值</tt> 如果小于或等于指定长度.
	 *         <li><tt>""</tt> 如果字符串为null.
	 *         <li><tt>"..."</tt> 当指定长度小于等于0,或(指定长度小于3,且字符串超长时).
	 *         <li><tt>新字符串</tt> 字节长度为指定长度，且最后三位替换为"...".
	 *         </ul>
	 */
	public static String omit(String s, String encoding, int maxlength) {
		if (s == null)
			return "";
		if (maxlength <= 0)
			return "...";
		try {
			byte[] bytes = encoding == null ? s.getBytes() : s.getBytes(encoding);
			if (bytes.length <= maxlength)
				return s;
			if (maxlength < 3)
				return "...";
			return new String(bytes, 0, maxlength - 3, encoding) + "...";
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 删除所有的标点符号
	 *
	 * @param str
	 *            处理的字符串
	 */
	public static String trimPunct(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return str.replaceAll("[\\pP\\p{Punct}]", "");
	}
	// }}

	// {{

	/**
	 * 比较两个字符串是否相等
	 * 
	 * @param val1
	 * @param val2
	 * @return
	 *         <ul>
	 *         <li>都为null时，true</li>
	 *         <li>仅一个为null,false</li>
	 *         <li>其它,val1.equals(val2)</li>
	 *         </ul>
	 */
	public static boolean equals(String val1, String val2) {
		if (val1 == null) {
			if (val2 == null)
				return true;
			else
				return false;
		}
		return val1.equals(val2);
	}
	// }}

}
