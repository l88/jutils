package org.l88.common.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StringsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsEmpty() {
		assertTrue(Strings.isEmpty(null));
		assertTrue(Strings.isEmpty(""));
		assertFalse(Strings.isEmpty(" "));
	}

	@Test
	public void testNullableObjectString() {
		assertEquals("", Strings.nullable(null));
		assertEquals("", Strings.nullable(""));
		assertEquals(" ", Strings.nullable(" "));
	}

	@Test
	public void testToStringObject() {
		assertEquals("e", Strings.toString(null, "e"));
		assertEquals("e", Strings.toString("", "e"));
		assertEquals(" ", Strings.toString(" ", "e"));
	}

	@Test
	public void testJoin() {
		List<String> list = new ArrayList<String>();
		list.add("str1");
		list.add("");
		list.add("str2");
		list.add("");
		assertEquals("str1<>str2", Strings.join(list, "<>"));
	}

	@Test
	public void testSplitLines() {
		String[] lines = Strings.splitLines("line1\nline2\r\n\r\nline3");
		String[] exp = { "line1", "line2", "", "line3" };
		assertArrayEquals(exp, lines);
	}

	@Test
	public void testSplitStringCharBoolean() {
		List<String> vs = Strings.split("aa,bbb,, ,cc,", ',', false);
		String[] exp = { "aa", "bbb", "", " ", "cc", "" };
		assertArrayEquals(exp, vs.toArray());
	}

	@Test
	public void testSplitStringChar() {
		List<String> vs = Strings.split("aa,bbb,, ,cc,", ',');
		String[] exp = { "aa", "bbb", "", "", "cc", "" };
		assertArrayEquals(exp, vs.toArray());
	}

	@Test
	public void testSplitString() {
		List<String> vs = Strings.split("aa,bbb,, ,cc,");
		String[] exp = { "aa", "bbb", "cc" };
		assertArrayEquals(exp, vs.toArray());
	}

	@Test
	public void testSplitStringString() {
		List<String> vs = Strings.split("aa,bbb,, ,cc,", ",");
		String[] exp = { "aa", "bbb", " ", "cc" };
		assertArrayEquals(exp, vs.toArray());
	}

	@Test
	public void testReplace() {
		String v = Strings.replace("aabbcc", "ab", "dd");
		assertEquals("addbcc", v);

	}

	@Test
	public void testEscapeXML() {
		String v = Strings.escapeXML("a<b>c&d;'e\"f");
		assertEquals("a&lt;b&gt;c&amp;d;&apos;e&quot;f", v);
	}

	@Test
	public void testUnescapeXML() {
		String v = Strings.unescapeXML("a&lt;b&gt;c&amp;d;&apos;e&quot;f");
		assertEquals("a<b>c&d;'e\"f", v);
	}

	@Test
	public void testEscapeHTML() {
		String v = Strings.escapeHTML("a<b>c&d;'e\"f\ng\r\nh");
		assertEquals("a&lt;b&gt;c&amp;d;&apos;e&quot;f<br>g<br>h", v);
	}

	@Test
	public void testCapitalize() {
		String v = Strings.capitalize("a<b>c&D ;'e\"f\ng\r\nh");
		assertEquals("A<b>c&D ;'e\"f\ng\r\nh", v);
	}

	@Test
	public void testUnCapitalize() {
		String v = Strings.unCapitalize("A<b>c&D ;'e\"f\ng\r\nh");
		assertEquals("a<b>c&D ;'e\"f\ng\r\nh", v);
	}

	@Test
	public void testReplaceVar() {
		Map<String, String> vs = new HashMap<String, String>();
		vs.put("var1", "v1");
		vs.put("var2", "v2");
		vs.put("var3", "v3");
		String v = Strings.replaceVar("${var3}--${var1}--${var2}", vs);
		assertEquals("v3--v1--v2", v);
	}

	@Test
	public void testToHex() {
		byte[] hash = { 0x00, (byte) 0xff, 0x01, 0x1a, 0x1f };
		String v = Strings.toHex(hash);
		assertEquals("00ff011a1f", v);
	}

	@Test
	public void testHexToBytes() {
		byte[] hash = { 0x00, (byte) 0xff, 0x01, 0x1a, 0x1f };
		byte[] v = Strings.hexToBytes("00ff011a1f");
		assertArrayEquals(hash, v);
	}

	@Test
	public void testCompactSize() {
		assertEquals("1,023B", Strings.compactSize(1023l));
		assertEquals("1K", Strings.compactSize(1024l));
		assertEquals("1M", Strings.compactSize(1024l*1024l));
		assertEquals("1,023M", Strings.compactSize(1024l*1024l*1023l));
		assertEquals("1G", Strings.compactSize(1024l*1024l*1024l));
		assertEquals("1T", Strings.compactSize(1024l*1024l*1024l*1024l));
	}

	@Test
	public void testGetDefaultCharacterEncoding() {
		Strings.getDefaultCharacterEncoding();
	}

	@Test
	public void testSubstr() {
		assertEquals("abc", Strings.substr("abcdef", 0, 3));
		//assertEquals("a中", Strings.substr("a中bcdef", 0, 3));
	}

	@Test
	public void testGbsubstrStringIntIntBoolean() {
		assertEquals("abc", Strings.gbsubstr("abcdef", 0, 3,true));
		//assertEquals("a中b", Strings.gbsubstr("a中bcdef", 0, 4, true));
		//assertEquals("a中", Strings.gbsubstr("a中bcdef", 0, 2, true));
	}

	@Test
	public void testGbsubstrStringIntInt() {
		assertEquals("abc", Strings.gbsubstr("abcdef", 0, 3));
		//assertEquals("a中b", Strings.gbsubstr("a中bcdef", 0, 4));
	}

	@Test
	public void testGbStrLen() {
		assertEquals(3, Strings.gbStrLen("a中"));
		assertEquals(5, Strings.gbStrLen("a中文"));
		assertEquals(3, Strings.gbStrLen("abc"));
	}

	@Test
	public void testReplicateStr() {
		assertEquals("0000000000", Strings.replicateStr('0', 10));
		assertEquals("0", Strings.replicateStr('0', 1));
		assertEquals("", Strings.replicateStr('0', 0));
		assertEquals("", Strings.replicateStr('0', -1));
	}

	@Test
	public void testLFillStr() {
		assertEquals("abc中def00", Strings.lFillStr("abc中def", '0', 10));
		assertEquals("a", Strings.lFillStr("abc中def", '0', 1));
		assertEquals("中文000000", Strings.lFillStr("中文", '0', 10));
	}

	@Test
	public void testRFillStrStringCharInt() {
		assertEquals("00abc中def", Strings.rFillStr("abc中def", '0', 10));
		//assertEquals("f", Strings.rFillStr("abc中def", '0', 1, true));
		assertEquals("000000中文", Strings.rFillStr("中文", '0', 10));
	}

	@Test
	public void testRFillStrStringCharIntBoolean() {
		assertEquals("00abc中def", Strings.rFillStr("abc中def", '0', 10, true));
		//assertEquals("f", Strings.rFillStr("abc中def", '0', 1, true));
		assertEquals("000000中文", Strings.rFillStr("中文", '0', 10, true));
	}

	@Test
	public void testOmitStringInt() {
		assertEquals("abcdef", Strings.omit("abcdef", 8));
		assertEquals("ab...", Strings.omit("abcdef", 5));
		assertEquals("abcdef", Strings.omit("abcdef", 6));
		assertEquals("...", Strings.omit("abcdef", 3));
		assertEquals("...", Strings.omit("abcdef", 2));
		assertEquals("...", Strings.omit("abcdef", 0));
		assertEquals("abc", Strings.omit("abc", 3));
		assertEquals("ab", Strings.omit("ab", 2));
		assertEquals("...", Strings.omit("abc", 2));
	}

	@Test
	public void testOmitStringStringInt() {
		assertEquals("ab中ef", Strings.omit("ab中ef", "GBK", 7));
		assertEquals("中...", Strings.omit("中bcdef", "GBK", 5));
		assertEquals("ab中ef", Strings.omit("ab中ef", "GBK", 6));
		assertEquals("...", Strings.omit("abcdef", "GBK", 3));
		assertEquals("...", Strings.omit("abcdef", "GBK", 2));
		assertEquals("...", Strings.omit("abcdef", "GBK", 0));
		assertEquals("abc", Strings.omit("abc", "GBK", 3));
		assertEquals("ab", Strings.omit("ab", "GBK", 2));
		assertEquals("...", Strings.omit("abc", "GBK", 2));
	}

	@Test
	public void testTrimPunct() {
		String v = Strings.trimPunct("a,b.c:d;e'f\"");
		assertEquals("abcdef", v);
	}

	@Test
	public void testEqualsStringString() {
		assertTrue(Strings.equals("1", "1"));
		assertTrue(Strings.equals(null, null));
		assertFalse(Strings.equals("", null));
		assertFalse(Strings.equals(null, ""));
		assertFalse(Strings.equals("1", "2"));
	}

}
