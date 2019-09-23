package language.expressions;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidExpression;
import util.TokenChecker;

public class Expression implements ExpressionInterface {

	private String expr;

	public Expression() {
		super();
	}

	public Expression(String expr) {
		this();
		this.expr = expr;
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
		return !(getType(token) == HEADER_STATEMENTS || getType(token) == HEADER_TOKENS
				|| getType(token) == HEADER_AGENTS || getType(token) == HEADER_ACTIVITIES
				|| getType(token) == HEADER_ENTITIES);
	}

	public int getType() {
		return getType(this.expr);
	}

	protected int getType(String token) {
		int type = EMPTY;
		if (token != null && token.length() > 0) {
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
			if (TokenChecker.getInstance().checkVarSet(token)) {
				return VAR_SET;
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
		return expr;
	}

	public void setStringExpression(String expr) {
		this.expr = expr;
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		int type = getType();
		switch (type) {
		case QUOTED_STRING: {
			result = new QuotedStringExpression(this.expr).parse(tokens, line);
			break;
		}
		case ACCESS_VAR: {
			result = new AccessVarExpression(this.expr).parse(tokens, line);
			break;
		}
		case ATTRIBUTION: {
			result = new AttributionExpression(this.expr).parse(tokens, line);
			break;
		}
		case IF_THEN_ELSE: {
			result = new IfThenElseExpression(this.expr).parse(tokens, line);
			break;
		}
		case BOOLEAN: {
			result = new BooleanExpression(this.expr).parse(tokens, line);
			break;
		}
		case MATCH: {
			result = new MatchExpression(this.expr).parse(tokens, line);
			break;
		}
		case REPLACE: {
			result = new ReplaceExpression(this.expr).parse(tokens, line);
			break;
		}
		case STATEMENT: {
			result = new StatementExpression(this.expr).parse(tokens, line);
			break;
		}
		case SUBSTRING: {
			result = new SubstringExpression(this.expr).parse(tokens, line);
			break;
		}
		case VAR_SET: {
			result = new VarSetExpression(this.expr).parse(tokens, line);
			break;
		}
		case NUMBER: {
			result = new NumberExpression(this.expr).parse(tokens, line);
			break;
		}
		default:
			break;
		}
		return result;
	}

	@Override
	public String toString() {
		return "[expr=" + expr + "]";
	}
}
