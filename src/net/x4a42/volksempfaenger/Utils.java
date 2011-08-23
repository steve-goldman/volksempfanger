package net.x4a42.volksempfaenger;

import java.util.Date;

public class Utils {
	public static String joinArray(Object[] objects, CharSequence sep) {
		if (objects == null) {
			return null;
		}

		StringBuilder buf = new StringBuilder();

		int i = 0;
		while (i < objects.length) {
			if (objects[i] != null) {
				buf.append(objects[i]);
			}
			if (++i < objects.length) {
				buf.append(sep);
			}
		}

		return buf.toString();
	}

	public static long toUnixTimestamp(Date date) {
		return date.getTime() / 1000L;
	}

	public static String normalizeString(String string) {
		return string.replaceAll("\\s+", " ");
	}
}
