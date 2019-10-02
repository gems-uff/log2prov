package log2prov.util;

import static log2prov.language.expressions.ExpressionInterface.CONDITION_SEPARATOR;
import static log2prov.language.expressions.ExpressionInterface.ELSE_SEPARATOR;
import static log2prov.language.expressions.ExpressionInterface.CONCAT_SEPARATOR;
import static log2prov.language.expressions.ExpressionInterface.VAR_SEPARATOR;
import static log2prov.language.expressions.ExpressionInterface.AND_SEPARATOR;
import static log2prov.language.expressions.ExpressionInterface.OR_SEPARATOR;
import static log2prov.language.expressions.ExpressionInterface.FALSE;
import static log2prov.language.expressions.ExpressionInterface.NUMBER_PATTERN;
import static log2prov.language.expressions.ExpressionInterface.TRUE;
import static log2prov.language.expressions.ExpressionInterface.VAR_PATTERN;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenUtil {

	private static final String REGEXP_STRING = "\"(?:[^\"\\\\]|\\\\.)*\"";
	private static final String CODED_QUESTION_MARK = ">>>>>>>Q<<<<<<<";
	private static final String CODED_COLON_MARK = ">>>>>>>C<<<<<<<";
	private static final String CODED_COMMA_MARK = ">>>>>>>O<<<<<<<";
	private static final String CODED_AND_MARK = ">>>>>>>AND<<<<<<<";
	private static final String CODED_OR_MARK = ">>>>>>>OR<<<<<<<";
	private static final String CODED_TRUE_MARK = ">>>>>>>T<<<<<<<";
	private static final String CODED_FALSE_MARK = ">>>>>>>F<<<<<<<";
	private static final String CODED_CONCAT_MARK = ">>>>>>>P<<<<<<<";

	private static TokenUtil instance;

	public static TokenUtil getInstance() {
		if (instance == null) {
			instance = new TokenUtil();
		}
		return instance;
	}

	public boolean isEmpty(String token) {
		return token != null && token.length() == 0;
	}

	public boolean checkSetExpression(String token) {
		if (token != null && token.contains(",")) {
			String slices[] = token.split("\\s*,\\s*");
			boolean allFilled = true;
			for (int i = 0; i < slices.length; i++) {
				if (!isEmpty(slices[i]) && slices[i].substring(0, 1).equals("$")) {
					allFilled = false;
					break;
				}
			}
			return allFilled;
		}
		return false;
	}

	public boolean checkAccessVar(String token) {
		return token != null && token.substring(0, 1).equals("$") && checkVar(token.substring(1).replace(")", ""));
	}

	public boolean checkVar(String token) {
		return token != null && VAR_PATTERN.matcher(token).find();
	}

	public boolean checkAttribution(String token) {
		if (token != null && token.contains("=")) {
			String[] slices = token.split("\\s*=\\s*");
			String left = slices[0];
			String right = slices[1];
			if (left != null && right != null) {
				if (checkVar(left) && !isEmpty(right)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkConcatenation(String token) {
		if (token != null && supressReserved(token).contains("+")) {
			String[] slices = supressReserved(token).split("\\s*\\+\\s*");
			for (int i = 0; i < slices.length; i++) {
				if (isEmpty(slices[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean checkAndExpression(String token) {
		if (token != null && supressReserved(token).contains("&&")) {
			String[] slices = supressReserved(token).split("\\s*&&\\s*");
			for (int i = 0; i < slices.length; i++) {
				if (isEmpty(slices[i])) {
					return false;
				} else {
					if (!TokenUtil.getInstance().checkBooleanExpression(slices[i])) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean checkOrExpression(String token) {
		if (token != null && supressReserved(token).contains("||")) {
			String[] slices = supressReserved(token).split("\\s*\\|\\|\\s*");
			for (int i = 0; i < slices.length; i++) {
				if (isEmpty(slices[i])) {
					return false;
				} else {
					if (!TokenUtil.getInstance().checkBooleanExpression(slices[i])) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean checkIfThenElse(String token) {
		if (token != null && token.contains(CONDITION_SEPARATOR)) {
			String[] slices = token.split("\\s*\\" + CONDITION_SEPARATOR + "\\s*");
			String booleanExpr = slices[0];
			if (booleanExpr != null && checkBooleanExpression(booleanExpr) && slices[1] != null) {
				return true;
			}
		}
		return false;
	}

	public boolean checkBooleanExpression(String token) {
		if (token != null && (checkTestRegexp(token) || checkContains(token) || checkAndExpression(token)
				|| checkOrExpression(token) || token.contains(TRUE) || token.contains(FALSE))) {
			return true;
		}
		return false;
	}

	public boolean checkContains(String token) {
		if (token != null && token.contains(".contains(")) {
			String[] slices = token.split("\\.contains\\(");
			if (slices.length == 2) {
				String left = slices[0];
				String right = slices[1];
				if (left != null && right != null) {
					if (!isEmpty(left) && !isEmpty(right)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean checkQuotedString(String token) {
		return token != null && token.matches("^\\s*\".*\"\\s*$");
	}

	public boolean checkNumber(String token) {
		if (token != null) {
			for (int i = 0; i < token.length(); i++) {
				if (!NUMBER_PATTERN.matcher(token.charAt(i) + "").find()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean checkSubstring(String token) {
		if (token != null && token.contains(".substring(")) {
			String[] slices = token.split("\\.substring\\(", 2);
			String left = slices[0];
			if (left != null && slices.length > 1) {
				String[] innerSlice = slices[1].split("\\s*,\\s*");
				String firstParam = innerSlice[0];
				String secondParam = innerSlice[1].substring(0, innerSlice[1].lastIndexOf(")"));
				if (!isEmpty(firstParam) && !isEmpty(secondParam)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkMatch(String token) {
		if (token != null && token.contains(".match(")) {
			String[] slices = token.split("\\.match\\(", 2);
			String left = slices[0];
			if (left != null && slices.length > 1) {
				String firstParam = slices[1].substring(0, slices[1].lastIndexOf(")"));
				if (!isEmpty(firstParam)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkReplace(String token) {
		if (token != null && token.contains(".replace(")) {
			String[] slices = token.split("\\.replace\\(");
			String left = slices[0];
			if (left != null && slices.length > 1) {
				String[] innerSlice = slices[1].split("\\s*,\\s*");
				String firstParam = innerSlice[0];
				String secondParam = innerSlice[1].substring(0, innerSlice[1].lastIndexOf(")"));
				if (!isEmpty(firstParam) && !isEmpty(secondParam)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkTestRegexp(String token) {
		if (token != null && token.contains("testRegexp(")) {
			String[] slices = TokenUtil.getInstance().supressReserved(token).split("testRegexp\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				String firstParam = innerSlice[0];
				String secondParam = innerSlice[1].substring(0, innerSlice[1].lastIndexOf(")"));
				if (firstParam != null && secondParam != null) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkTrue(String token) {
		return token != null && token.equals("true");
	}

	public boolean checkFalse(String token) {
		return token != null && token.equals("false");
	}

	public boolean checkStatement(String token) {
		if (token != null && (checkAOB(token) || checkWAT(token) || checkWDF(token) || checkWGB(token)
				|| checkUSD(token) || checkWAW(token) || checkHMB(token))) {
			return true;
		}
		return false;
	}

	public boolean checkAOB(String token) {
		if (token != null && token.contains("actedOnBehalfOf(")) {
			String[] slices = token.split("actedOnBehalfOf\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				if (innerSlice.length >= 2 && innerSlice[0] != null && innerSlice[1] != null) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkWAT(String token) {
		if (token != null && token.contains("wasAttributedTo(")) {
			String[] slices = token.split("wasAttributedTo\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				if (innerSlice.length >= 2 && innerSlice[0] != null && innerSlice[1] != null) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkWDF(String token) {
		if (token != null && token.contains("wasDerivedFrom(")) {
			String[] slices = token.split("wasDerivedFrom\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				if (innerSlice.length >= 2 && innerSlice[0] != null && innerSlice[1] != null) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkWGB(String token) {
		if (token != null && token.contains("wasGeneratedBy(")) {
			String[] slices = token.split("wasGeneratedBy\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				if (innerSlice.length >= 2 && innerSlice[0] != null && innerSlice[1] != null) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkUSD(String token) {
		if (token != null && token.contains("used(")) {
			String[] slices = token.split("used\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				if (innerSlice.length >= 2 && innerSlice[0] != null && innerSlice[1] != null) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkWAW(String token) {
		if (token != null && token.contains("wasAssociatedWith(")) {
			String[] slices = token.split("wasAssociatedWith\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				if (innerSlice.length >= 2 && innerSlice[0] != null && innerSlice[1] != null) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkHMB(String token) {
		if (token != null && token.contains("hadMember(")) {
			String[] slices = token.split("hadMember\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				if (innerSlice.length >= 2 && checkAccessVar(innerSlice[0]) && checkAccessVar(innerSlice[1])) {
					return true;
				}
			}
		}
		return false;
	}

	public String supressReserved(String token) {
		String result = null;
		Map<String, String> replacements = new HashMap<>();
		if (token != null) {
			Pattern p = Pattern.compile(REGEXP_STRING);
			Matcher m = p.matcher(token);
			while (m.find()) {
				String replacement = token.substring(m.start(), m.end());
				replacement = replacement.replace(CONDITION_SEPARATOR, CODED_QUESTION_MARK);
				replacement = replacement.replace(ELSE_SEPARATOR, CODED_COLON_MARK);
				replacement = replacement.replace(AND_SEPARATOR, CODED_AND_MARK);
				replacement = replacement.replace(OR_SEPARATOR, CODED_OR_MARK);
				replacement = replacement.replace(CONCAT_SEPARATOR, CODED_CONCAT_MARK);
				replacement = replacement.replace(TRUE, CODED_TRUE_MARK);
				replacement = replacement.replace(FALSE, CODED_FALSE_MARK);
				replacement = replacement.replace(VAR_SEPARATOR, CODED_COMMA_MARK);
				replacements.put(token.substring(m.start(), m.end()), replacement);
			}
			result = token;
			for (String key : replacements.keySet()) {
				result = result.replace(key, replacements.get(key));
			}
		}
		return result;
	}

	public String impressReserved(String token) {
		String result = null;
		if (token != null) {
			result = token.replace(CODED_QUESTION_MARK, CONDITION_SEPARATOR);
			result = result.replace(CODED_AND_MARK, AND_SEPARATOR);
			result = result.replace(CODED_OR_MARK, OR_SEPARATOR);
			result = result.replace(CODED_TRUE_MARK, TRUE);
			result = result.replace(CODED_FALSE_MARK, FALSE);
			result = result.replace(CODED_COLON_MARK, ELSE_SEPARATOR);
			result = result.replace(CODED_CONCAT_MARK, CONCAT_SEPARATOR);
			result = result.replace(CODED_COMMA_MARK, VAR_SEPARATOR);
		}
		return result;
	}

	public String impressEscaped(String token) {
		String result = null;
		if (token != null) {
			result = token.replace(" ", "_");
			/*
			 * result = result.replace("=", "\\="); result = result.replace("'", "\\'");
			 * result = result.replace("(", "\\("); result = result.replace(")", "\\)");
			 * result = result.replace(",", "\\,"); result = result.replace(";", "\\;");
			 * result = result.replace("[", "\\["); result = result.replace("]", "\\]");
			 * 
			 * result = result.replace(".", "\\."); result = result.replace(":", "\\:");
			 * result = result.replace("-", "\\-");
			 */
		}
		return result;
	}

	public boolean checkLineHeader(String token) {
		return token != null && token.contains("[line]");
	}

	public boolean checkAgentsHeader(String token) {
		return token != null && token.contains("[agents]");
	}

	public boolean checkActivitiesHeader(String token) {
		return token != null && token.contains("[activities]");
	}

	public boolean checkEntitiesHeader(String token) {
		return token != null && token.contains("[entities]");
	}

	public boolean checkStatementsHeader(String token) {
		return token != null && token.contains("[statements]");
	}

	public boolean checkTokensHeader(String token) {
		return token != null && token.contains("[tokens]");
	}

}
