/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author os_linhlh2
 */
public class HtmlUtil {

	private static final String[] _MS_WORD_HTML = new String[] { "&reg;", StringPool.APOSTROPHE, StringPool.QUOTE,
			StringPool.QUOTE };
	private static final String[] _MS_WORD_UNICODE = new String[] { "\u00ae", "\u2019", "\u201c", "\u201d" };
	private static final char[] _TAG_SCRIPT = { 's', 'c', 'r', 'i', 'p', 't' };
	public static final int ESCAPE_MODE_ATTRIBUTE = 1;
	public static final int ESCAPE_MODE_CSS = 2;

	public static final int ESCAPE_MODE_JS = 3;

	public static final int ESCAPE_MODE_TEXT = 4;

	public static final int ESCAPE_MODE_URL = 5;

	public static String escape(String text) {
		if (text == null) {
			return null;
		}

		if (text.length() == 0) {
			return StringPool.BLANK;
		}

		// Escape using XSS recommendations from
		// http://www.owasp.org/index.php/Cross_Site_Scripting
		// #How_to_Protect_Yourself

		StringBuilder sb = null;

		int lastReplacementIndex = 0;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			String replacement = null;

			switch (c) {
			case '<':
				replacement = "&lt;";

				break;

			case '>':
				replacement = "&gt;";

				break;

			case '&':
				replacement = "&amp;";

				break;

			case '"':
				replacement = "&#034;";

				break;

			case '\'':
				replacement = "&#039;";

				break;

			case '\u00bb': // '�'
				replacement = "&#187;";

				break;

			case '\u2013':
				replacement = "&#x2013;";

				break;

			case '\u2014':
				replacement = "&#x2014;";

				break;
			}

			if (replacement != null) {
				if (sb == null) {
					sb = new StringBuilder();
				}

				if (i > lastReplacementIndex) {
					sb.append(text.substring(lastReplacementIndex, i));
				}

				sb.append(replacement);

				lastReplacementIndex = i + 1;
			}
		}

		if (sb == null) {
			return text;
		} else {
			if (lastReplacementIndex < text.length()) {
				sb.append(text.substring(lastReplacementIndex));
			}

			return sb.toString();
		}
	}

	public static String escape(String text, int type) {
		if (text == null) {
			return null;
		}

		if (text.length() == 0) {
			return StringPool.BLANK;
		}

		String prefix = StringPool.BLANK;
		String postfix = StringPool.BLANK;

		if (type == ESCAPE_MODE_ATTRIBUTE) {
			prefix = "&#x";
			postfix = StringPool.SEMICOLON;
		} else if (type == ESCAPE_MODE_CSS) {
			prefix = StringPool.BACK_SLASH;
		} else if (type == ESCAPE_MODE_JS) {
			prefix = "\\x";
		} else {
			return escape(text);
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if ((Character.isLetterOrDigit(c)) || (c == CharPool.DASH) || (c == CharPool.UNDERLINE)) {

				sb.append(c);
			} else {
				sb.append(prefix);

				String hexString = StringUtil.toHexString(c);

				if (hexString.length() == 1) {
					sb.append(StringPool.ASCII_TABLE[48]);
				}

				sb.append(hexString);
				sb.append(postfix);
			}
		}

		if (sb.length() == text.length()) {
			return text;
		} else {
			return sb.toString();
		}
	}

	public static String escapeAttribute(String attribute) {
		return escape(attribute, ESCAPE_MODE_ATTRIBUTE);
	}

	public static String escapeCSS(String css) {
		return escape(css, ESCAPE_MODE_CSS);
	}

	public static String escapeHREF(String href) {
		if (href == null) {
			return null;
		}

		if (href.length() == 0) {
			return StringPool.BLANK;
		}

		if (href.indexOf(StringPool.COLON) == 10) {
			String protocol = href.substring(0, 10).toLowerCase();

			if (protocol.equals("javascript")) {
				return StringUtil.replaceFirst(href, StringPool.COLON, "%3a");
			}
		}

		return href;
	}

	public static String escapeJS(String js) {
		return escape(js, ESCAPE_MODE_JS);
	}

	public static String escapeURL(String url) {
		return escape(url, ESCAPE_MODE_URL);
	}

	public static String fromInputSafe(String text) {
		return StringUtil.replace(text, "&amp;", "&");
	}

	protected static boolean isScriptTag(String text, int pos) {
		if (pos + _TAG_SCRIPT.length + 1 <= text.length()) {
			char item;

			for (int i = 0; i < _TAG_SCRIPT.length; i++) {
				item = text.charAt(pos++);

				if (Character.toLowerCase(item) != _TAG_SCRIPT[i]) {
					return false;
				}
			}

			item = text.charAt(pos);

			// Check that char after "script" is not a letter (i.e. another tag)

			return !Character.isLetter(item);
		} else {
			return false;
		}
	}

	public static String replaceMsWordCharacters(String text) {
		return StringUtil.replace(text, _MS_WORD_UNICODE, _MS_WORD_HTML);
	}

	public static String stripBetween(String text, String tag) {
		return StringUtil.stripBetween(text, "<" + tag, "</" + tag + ">");
	}

	public static String stripComments(String text) {
		return StringUtil.stripBetween(text, "<!--", "-->");
	}

	public static String stripHtml(String text) {
		if (text == null) {
			return null;
		}

		text = stripComments(text);

		StringBuilder sb = new StringBuilder(text.length());

		int x = 0;
		int y = text.indexOf("<");

		while (y != -1) {
			sb.append(text.substring(x, y));
			sb.append(StringPool.SPACE);

			// Look for text enclosed by <script></script>

			boolean scriptFound = isScriptTag(text, y + 1);

			if (scriptFound) {
				int pos = y + _TAG_SCRIPT.length;

				// Find end of the tag

				pos = text.indexOf(">", pos);

				if (pos >= 0) {

					// Check if preceding character is / (i.e. is this instance
					// of <script/>)

					if (text.charAt(pos - 1) != '/') {

						// Search for the ending </script> tag

						for (;;) {
							pos = text.indexOf("</", pos);

							if (pos >= 0) {
								if (isScriptTag(text, pos + 2)) {
									y = pos;

									break;
								} else {

									// Skip past "</"

									pos += 2;
								}
							} else {
								break;
							}
						}
					}
				}
			}

			x = text.indexOf(">", y);

			if (x == -1) {
				break;
			}

			x++;

			if (x < y) {

				// <b>Hello</b

				break;
			}

			y = text.indexOf("<", x);
		}

		if (y == -1) {
			sb.append(text.substring(x, text.length()));
		}

		return sb.toString();
	}

	public static String toInputSafe(String text) {
		return StringUtil.replace(text, new String[] { "&", "\"" }, new String[] { "&amp;", "&quot;" });
	}

	public static String unescape(String text) {
		if (text == null) {
			return null;
		}

		if (text.length() == 0) {
			return StringPool.BLANK;
		}

		// Optimize this

		text = StringUtil.replace(text, "&lt;", "<");
		text = StringUtil.replace(text, "&gt;", ">");
		text = StringUtil.replace(text, "&amp;", "&");
		text = StringUtil.replace(text, "&#034;", "\"");
		text = StringUtil.replace(text, "&#039;", "'");
		text = StringUtil.replace(text, "&#040;", "(");
		text = StringUtil.replace(text, "&#041;", ")");
		text = StringUtil.replace(text, "&#044;", ",");
		text = StringUtil.replace(text, "&#035;", "#");
		text = StringUtil.replace(text, "&#037;", "%");
		text = StringUtil.replace(text, "&#059;", ";");
		text = StringUtil.replace(text, "&#061;", "=");
		text = StringUtil.replace(text, "&#043;", "+");
		text = StringUtil.replace(text, "&#045;", "-");

		return text;
	}

	public static String unescapeCDATA(String text) {
		if (text == null) {
			return null;
		}

		if (text.length() == 0) {
			return StringPool.BLANK;
		}

		text = StringUtil.replace(text, "&lt;![CDATA[", "<![CDATA[");
		text = StringUtil.replace(text, "]]&gt;", "]]>");

		return text;
	}

	public static String wordBreak(String text, int columns) {
		StringBuilder sb = new StringBuilder();

		int length = 0;
		int lastWrite = 0;
		int pos = 0;

		Pattern pattern = Pattern.compile("([\\s<&]|$)");

		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			if (matcher.start() < pos) {
				continue;
			}

			while ((length + matcher.start() - pos) >= columns) {
				pos += columns - length;

				sb.append(text.substring(lastWrite, pos));
				sb.append("<wbr/>&shy;");

				length = 0;
				lastWrite = pos;
			}

			length += matcher.start() - pos;

			String group = matcher.group();

			if (group.equals(StringPool.AMPERSAND)) {
				int x = text.indexOf(StringPool.SEMICOLON, matcher.start());

				if (x != -1) {
					length++;
					pos = x + 1;
				}

				continue;
			}

			if (group.equals(StringPool.LESS_THAN)) {
				int x = text.indexOf(StringPool.GREATER_THAN, matcher.start());

				if (x != -1) {
					pos = x + 1;
				}

				continue;
			}

			if (group.equals(StringPool.SPACE) || group.equals(StringPool.NEW_LINE)) {

				length = 0;
				pos = matcher.start() + 1;
			}
		}

		sb.append(text.substring(lastWrite));

		return sb.toString();
	}
}
