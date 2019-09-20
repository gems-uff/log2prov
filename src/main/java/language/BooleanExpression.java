package language;

import java.util.Map;

import exception.InvalidExpression;

public class BooleanExpression extends Expression {

	public BooleanExpression(String expr) throws InvalidExpression {
		if (getType() != BOOLEAN) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		int type = getType(super.getStringExpression());
		switch (type) {
		case TEST_REGEXP: {
			result = new TestRegexpExpression(super.getStringExpression()).parse(tokens, line);
			break;
		}
		case CONTAINS: {
			result = new ContainsExpression(super.getStringExpression()).parse(tokens, line);
			break;
		}
		case TRUE_TYPE: {
			result = TRUE;
			break;
		}
		case FALSE_TYPE: {
			result = FALSE;
			break;
		}
		default:
			break;
		}
		return result;
	}

}
