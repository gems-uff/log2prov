package util;

import static language.expressions.ExpressionInterface.*;

public class TokenChecker {

	private static TokenChecker instance;

	public static TokenChecker getInstance() {
		if (instance == null) {
			instance = new TokenChecker();
		}
		return instance;
	}

	public boolean isEmpty(String token) {
		return token != null && token.length() == 0;
	}
	
	public boolean checkVarSet(String token) {
		if (token != null && token.contains(",")) {
			String slices[] = token.split("\\s*,\\s*");
			boolean allFilled = true;
			for (int i = 0; i < slices.length; i++) {
				if (!isEmpty(slices[i]) && slices[i].substring(0,1).equals("$")) {
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
	
	public boolean checkIfThenElse(String token) {
		if (token != null && token.contains(CONDITION_SEPARATOR)) {
			String[] slices = token.split("\\s*\\" + CONDITION_SEPARATOR+"\\s*");
			String booleanExpr = slices[0];
			if (booleanExpr != null && checkBooleanExpression(booleanExpr) && slices[1] != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkBooleanExpression(String token) {
		if (token != null && (checkTestRegexp(token)
				|| checkContains(token) || token.contains(TRUE) || token.contains(FALSE))) {
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
			boolean allTrue = true;
			for (int i = 0; i < token.length(); i++) {
				if (!NUMBER_PATTERN.matcher(token.charAt(i) + "").find()) {
					allTrue = false;
				}
			}
			return allTrue;
		}
		return false;
	}

	public boolean checkSubstring(String token) {
		if (token != null && token.contains(".substring(")) {
			String[] slices = token.split("\\.substring\\(");
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
			String[] slices = token.split("\\.match\\("); 
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
			String[] slices = token.split("testRegexp\\(");
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
		return token != null && token.equals("true");
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

}
