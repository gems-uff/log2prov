package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpUtil {

	private static RegexpUtil instance;

	public static RegexpUtil getInstance() {
		if (instance == null) {
			instance = new RegexpUtil();
		}
		return instance;
	}

	public String replace(String token, String regexp, String replacement) {
		String result = null;
		if (token != null && regexp != null) {
			Pattern p = Pattern.compile(regexp);
			Matcher m = p.matcher(token);
			if (m.find()) {
				result = token.replace(token.substring(m.start(), m.end()), replacement);
			}
		}
		return result;
	}

}
