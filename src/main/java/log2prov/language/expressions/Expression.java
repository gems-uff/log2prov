package log2prov.language.expressions;

import java.util.Map;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class Expression implements ExpressionInterface {

	private String expr;

	public Expression() {
		super();
	}

	public Expression(String expr) {
		this();
		this.expr = expr;
	}

	public boolean isEndHeaderToken(String token) {
		return !(TokenUtil.getInstance().checkStatementsHeader(token) 
				|| TokenUtil.getInstance().checkTokensHeader(token)
				|| TokenUtil.getInstance().checkAgentsHeader(token) 
				|| TokenUtil.getInstance().checkActivitiesHeader(token)
				|| TokenUtil.getInstance().checkEntitiesHeader(token));
	}

	public int getType() {
		return getType(this.expr);
	}

	protected int getType(String token) {
		int type = EMPTY;
		if (token != null && token.length() > 0) {
			if (TokenUtil.getInstance().checkLineHeader(token)) {
				return HEADER_LINE;
			}
			if (TokenUtil.getInstance().checkTokensHeader(token)) {
				return HEADER_TOKENS;
			}
			if (TokenUtil.getInstance().checkAgentsHeader(token)) {
				return HEADER_AGENTS;
			}
			if (TokenUtil.getInstance().checkEntitiesHeader(token)) {
				return HEADER_ENTITIES;
			}
			if (TokenUtil.getInstance().checkActivitiesHeader(token)) {
				return HEADER_ACTIVITIES;
			}
			if (TokenUtil.getInstance().checkStatementsHeader(token)) {
				return HEADER_STATEMENTS;
			}
			if (TokenUtil.getInstance().checkAttribution(token)) {
				return ATTRIBUTION;
			}
			if (TokenUtil.getInstance().checkIfThenElse(token)) {
				return IF_THEN_ELSE;
			}
			if (TokenUtil.getInstance().checkConcatenation(token)) {
				return CONCATENATION;
			}
			if (TokenUtil.getInstance().checkBooleanExpression(token)) {
				return BOOLEAN;
			}
			if (TokenUtil.getInstance().checkNumber(token)) {
				return NUMBER;
			}
			if (TokenUtil.getInstance().checkQuotedString(token)) {
				return QUOTED_STRING;
			}
			if (TokenUtil.getInstance().checkSubstring(token)) {
				return SUBSTRING;
			}
			if (TokenUtil.getInstance().checkReplace(token)) {
				return REPLACE;
			}
			if (TokenUtil.getInstance().checkMatch(token)) {
				return MATCH;
			}
			if (TokenUtil.getInstance().checkStatement(token)) {
				return STATEMENT;
			}
			if (TokenUtil.getInstance().checkSetExpression(token)) {
				return VAR_SET;
			}
			if (TokenUtil.getInstance().checkVar(token)) {
				return VAR;
			}
			if (TokenUtil.getInstance().checkAccessVar(token)) {
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
		case CONCATENATION: {
			result = new ConcatExpression(this.expr).parse(tokens, line);
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
			result = new SetExpression(this.expr).parse(tokens, line);
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
