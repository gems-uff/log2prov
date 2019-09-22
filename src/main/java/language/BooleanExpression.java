package language;

import java.util.Map;

import exception.InvalidExpression;
import util.TokenChecker;

public class BooleanExpression extends Expression {

	public BooleanExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!(TokenChecker.getInstance().checkTestRegexp(expr) || TokenChecker.getInstance().checkContains(expr)
				|| TokenChecker.getInstance().checkTrue(expr) || TokenChecker.getInstance().checkFalse(expr))) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		if (TokenChecker.getInstance().checkTestRegexp(super.getStringExpression())) {
			result = new TestRegexpExpression(super.getStringExpression()).parse(tokens, line);
		} else if (TokenChecker.getInstance().checkContains(super.getStringExpression())) {
			result = new ContainsExpression(super.getStringExpression()).parse(tokens, line);
		} else if (TokenChecker.getInstance().checkTrue(super.getStringExpression())) {
			result = TRUE;
		} else if (TokenChecker.getInstance().checkFalse(super.getStringExpression())) {
			result = FALSE;
		}
		return result;
	}

}
