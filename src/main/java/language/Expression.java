package language;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidExpression;

public class Expression implements ExpressionInterface {

	private String stringExpression;

	public Expression() {
		super();
	}

	public Expression(String stringExpression) {
		this();
		this.stringExpression = stringExpression;
	}

	public String matchFirst(String line, String regexp) {
		String match = null;
		Pattern p = Pattern.compile(regexp);
		Matcher matcher = p.matcher(line);
		if (matcher.find()) {
			match = line.substring(matcher.start(), matcher.end());
		}
		return match;
	}

	public boolean isEndHeaderToken(String token) {
		int type = getType(token);
		return type > 0 && type < 6;
	}

	public int getType() {
		return getType(this.stringExpression);
	}

	protected int getType(String token) {
		int type = EMPTY;
		if (token != null && checkLiteral(token)) {
			if (token.contains(FALSE)) {
				return FALSE_TYPE;
			}
			if (token.contains(TRUE)) {
				return TRUE_TYPE;
			}
			if (token.contains("[line]")) {
				return HEADER_LINE;
			}
			if (token.contains("[tokens]")) {
				return HEADER_TOKENS;
			}
			if (token.contains("[agents]")) {
				return HEADER_AGENTS;
			}
			if (token.contains("[entities]")) {
				return HEADER_ENTITIES;
			}
			if (token.contains("[activities]")) {
				return HEADER_ACTIVITIES;
			}
			if (token.contains("[statements]")) {
				return HEADER_STATEMENTS;
			}
			if (checkStatement(token)) {
				return STATEMENT;
			}
			if (checkVar(token)) {
				return VAR;
			}
			if (checkAccessVar(token)) {
				return ACCESS_VAR;
			}
			if (checkSetVars(token)) {
				return SET_ACCESS_VAR;
			}
			if (checkAttribution(token)) {
				return ATTRIBUTION;
			}
			if (checkSubstring(token)) {
				return SUBSTRING;
			}
			if (checkContains(token)) {
				return CONTAINS;
			}
			if (checkReplace(token)) {
				return REPLACE;
			}
			if (checkTestRegexp(token)) {
				return TEST_REGEXP;
			}
			if (checkMatch(token)) {
				return MATCH;
			}
			if (checkBooleanExpression(token)) {
				return BOOLEAN;
			}
			if (checkIfThenElse(token)) {
				return IF_THEN_ELSE;
			}
			if (checkLiteral(token)) {
				return LITERAL;
			}
		}
		return type;
	}

	private boolean checkSetVars(String token) {
		if (token != null && token.contains(",")) {
			String slices[] = token.split(",");
			boolean allVars = true;
			for (int i = 0; i < slices.length; i++) {
				if (!checkAccessVar(slices[i]) || !checkVar(slices[i]) || !checkIfThenElse(slices[i])) {
					allVars = false;
					break;
				}
			}
			return allVars;
		}
		return false;
	}

	private boolean checkLiteral(String token) {
		return token != null && token.length() > 0 && !token.substring(0, 1).matches("\\d");
	}

	private boolean checkAccessVar(String token) {
		return token != null && token.substring(0, 1).equals("$") && checkVar(token.substring(1));
	}

	private boolean checkVar(String token) {
		return token != null && VAR_PATTERN.matcher(token).find();
	}

	private boolean checkNumber(String token) {
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

	private boolean checkIfThenElse(String token) {
		if (token != null && token.contains(CONDITION_SEPARATOR)) {
			String[] slices = token.split(CONDITION_SEPARATOR);
			String booleanExpr = slices[0];
			String[] thenElse = slices[1].split(ELSE_SEPARATOR);
			String then = thenElse[0];
			String elze = null;
			if (thenElse.length == 2) {
				elze = thenElse[1];
			}

			if (booleanExpr != null && thenElse.length > 0) {
				if (checkBooleanExpression(booleanExpr) && (getType(then) == LITERAL
						&& (elze == null || checkLiteral(elze) || checkIfThenElse(elze)))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkAttribution(String token) {
		if (token != null && token.contains("=")) {
			String[] slices = token.split("=");
			String left = slices[0];
			String right = slices[1];
			if (left != null && right != null) {
				if (checkVar(left) && (checkLiteral(token) || (checkIfThenElse(token)))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkBooleanExpression(String token) {
		if (token != null
				&& (checkTestRegexp(token) || checkContains(token) || token.contains(TRUE) || token.contains(FALSE))) {
			return true;
		}
		return false;
	}

	private boolean checkContains(String token) {
		if (token != null && token.contains(".")) {
			String[] slices = token.split("\\.");
			String left = slices[0];
			String right = slices[1];
			if (left != null && right != null) {
				String[] innerSlice = right.split("contains\\(");
				if (checkLiteral(left) && innerSlice.length == 2) {
					String literal = innerSlice[1].replace(")", "");
					if (checkLiteral(literal))
						return true;
				}
			}
		}
		return false;
	}

	private boolean checkTestRegexp(String token) {
		if (token != null && token.contains("testRegexp(")) {
			String[] slices = token.split("testRegexp\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split(",");
				if (innerSlice.length == 2 && checkLiteral(innerSlice[0]) && checkLiteral(innerSlice[1])) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkSubstring(String token) {
		if (token != null && token.contains(".substring(")) {
			String[] slices = token.split("\\.substring\\(");
			String left = slices[0];
			if (left != null && slices.length > 1) {
				if (checkLiteral(left)) {
					boolean allTrue = true;
					for (int i = 1; i < slices.length; i++) {
						if (!checkInnerNumberParams(slices[i])) {
							allTrue = false;
						}
					}
					return allTrue;
				}
			}
		}
		return false;
	}

	private boolean checkMatch(String token) {
		if (token != null && token.contains(".match(")) {
			String[] slices = token.split("\\.match\\(");
			String left = slices[0];
			if (left != null && slices.length > 1) {
				if (checkLiteral(left)) {
					boolean allTrue = true;
					for (int i = 1; i < slices.length; i++) {
						if (!checkInnerLiteralsParams(slices[i])) {
							allTrue = false;
						}
					}
					return allTrue;
				}
			}
		}
		return false;
	}

	private boolean checkReplace(String token) {
		if (token != null && token.contains(".replace(")) {
			String[] slices = token.split("\\.replace\\(");
			String left = slices[0];
			if (left != null && slices.length > 1) {
				if (checkLiteral(left)) {
					boolean allTrue = true;
					for (int i = 1; i < slices.length; i++) {
						if (!checkInnerLiteralsParams(slices[i])) {
							allTrue = false;
						}
					}
					return allTrue;
				}
			}
		}
		return false;
	}

	private boolean checkInnerNumberParams(String params) {
		if (params != null) {
			String[] slices = params.split(",");
			String innerLeft = slices[0];
			String innerRight = slices[1].replace(")", "");
			if (checkNumber(innerLeft) && checkNumber(innerRight)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkInnerLiteralsParams(String params) {
		if (params != null) {
			String[] slices = params.split(",");
			String innerLeft = slices[0];
			String innerRight = slices[1].replace(")", "");
			if (checkLiteral(innerLeft) && checkLiteral(innerRight)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkStatement(String token) {
		if (token != null && (checkAOB(token) || checkWAT(token) || checkWDF(token) || checkWGB(token)
				|| checkUSD(token) || checkWAW(token) || checkHMB(token))) {
			return true;
		}
		return false;
	}

	private boolean checkAOB(String token) {
		if (token != null && token.contains("actedOnBehalfOf(")) {
			String[] slices = token.split("actedOnBehalfOf\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split(",");
				if (innerSlice.length == 2 && checkAccessVar(innerSlice[0]) && checkAccessVar(innerSlice[1])) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkWAT(String token) {
		if (token != null && token.contains("wasAttributedTo(")) {
			String[] slices = token.split("wasAttributedTo\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split(",");
				if (innerSlice.length == 2 && checkAccessVar(innerSlice[0]) && checkAccessVar(innerSlice[1])) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkWDF(String token) {
		if (token != null && token.contains("wasDerivedFrom(")) {
			String[] slices = token.split("wasDerivedFrom\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split(",");
				if (innerSlice.length == 2 && checkAccessVar(innerSlice[0]) && checkAccessVar(innerSlice[1])) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkWGB(String token) {
		if (token != null && token.contains("wasGeneratedBy(")) {
			String[] slices = token.split("wasGeneratedBy\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split(",");
				if (innerSlice.length == 2 && checkAccessVar(innerSlice[0]) && checkAccessVar(innerSlice[1])) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkUSD(String token) {
		if (token != null && token.contains("used(")) {
			String[] slices = token.split("used\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split(",");
				if (innerSlice.length == 2 && checkAccessVar(innerSlice[0]) && checkAccessVar(innerSlice[1])) {
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
				String[] innerSlice = right.split(",");
				if (innerSlice.length == 2 && checkAccessVar(innerSlice[0]) && checkAccessVar(innerSlice[1])) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkHMB(String token) {
		if (token != null && token.contains("hadMember(")) {
			String[] slices = token.split("hadMember\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split(",");
				if (innerSlice.length == 2 && checkAccessVar(innerSlice[0]) && checkAccessVar(innerSlice[1])) {
					return true;
				}
			}
		}
		return false;
	}

	public String getStringExpression() {
		return stringExpression;
	}

	public void setStringExpression(String stringExpression) {
		this.stringExpression = stringExpression;
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		int type = getType();
		switch (type) {
		case ATTRIBUTION: {
			result = new AttributionExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case BOOLEAN: {
			result = new BooleanExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case CONTAINS: {
			result = new ContainsExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case IF_THEN_ELSE: {
			result = new IfThenElseExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case LITERAL: {
			result = new LiteralExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case MATCH: {
			result = new MatchExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case REPLACE: {
			result = new ReplaceExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case STATEMENT: {
			result = new StatementExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case SUBSTRING: {
			result = new SubstringExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case TEST_REGEXP: {
			result = new TestRegexpExpression(this.stringExpression).parse(tokens, line);
			break;
		}

		default:
			break;
		}
		return result;
	}

	public static void main(String[] args) {
		String token1 = "teste.substring(0,3).substring(1,8)";
		String token2 = "teste.substring(0,3)";
		Expression expr = new Expression(token1);
		System.out.println(expr.checkSubstring(token1));
		System.out.println(expr.checkSubstring(token2));
	}

}
