package language;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidExpression;
import util.TokenChecker;

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
		if (token != null && token.length() > 0) {
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
			if (TokenChecker.getInstance().checkIfThenElse(token)) {
				return IF_THEN_ELSE;
			}
			if (TokenChecker.getInstance().checkAttribution(token)) {
				return ATTRIBUTION;
			}
			if (TokenChecker.getInstance().checkBooleanExpression(token)) {
				return BOOLEAN;
			}
			if (TokenChecker.getInstance().checkNumber(token)) {
				return NUMBER;
			}
			if (TokenChecker.getInstance().checkQuotedString(token)) {
				return QUOTED_STRING;
			}
			if (TokenChecker.getInstance().checkSubstring(token)) {
				return SUBSTRING;
			}
			if (TokenChecker.getInstance().checkReplace(token)) {
				return REPLACE;
			}
			if (TokenChecker.getInstance().checkMatch(token)) {
				return MATCH;
			}
			if (TokenChecker.getInstance().checkStatement(token)) {
				return STATEMENT;
			}
			if (TokenChecker.getInstance().checkSet(token)) {
				return SET;
			}
			if (TokenChecker.getInstance().checkVar(token)) {
				return VAR;
			}
			if (TokenChecker.getInstance().checkAccessVar(token)) {
				return ACCESS_VAR;
			}
		}
		return type;
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
		case QUOTED_STRING: {
			result = new QuotedStringExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case ACCESS_VAR: {
			result = new AccessVarExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case ATTRIBUTION: {
			result = new AttributionExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case IF_THEN_ELSE: {
			result = new IfThenElseExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case BOOLEAN: {
			result = new BooleanExpression(this.stringExpression).parse(tokens, line);
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
		case SET: {
			result = new SetExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		case NUMBER: {
			result = new NumberExpression(this.stringExpression).parse(tokens, line);
			break;
		}
		default:
			break;
		}
		return result;
	}

	@Override
	public String toString() {
		return "[stringExpression=" + stringExpression + "]";
	}
}
