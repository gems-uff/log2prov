package log2prov.language.expressions;

import java.util.Map;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class BooleanExpression extends Expression {

	public BooleanExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!(TokenUtil.getInstance().checkAndExpression(expr) || TokenUtil.getInstance().checkOrExpression(expr)
				|| TokenUtil.getInstance().checkTestRegexp(expr) || TokenUtil.getInstance().checkContains(expr)
				|| TokenUtil.getInstance().checkNotExpression(expr) || TokenUtil.getInstance().checkTrue(expr)
				|| TokenUtil.getInstance().checkFalse(expr))) {
			InvalidExpression e = new InvalidExpression();
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		if (TokenUtil.getInstance().checkAndExpression(super.getStringExpression())) {
			result = new AndExpression(super.getStringExpression()).parse(tokens, line);
		} else if (TokenUtil.getInstance().checkOrExpression(super.getStringExpression())) {
			result = new OrExpression(super.getStringExpression()).parse(tokens, line);
		} else if (TokenUtil.getInstance().checkTestRegexp(super.getStringExpression())) {
			result = new TestRegexpExpression(super.getStringExpression()).parse(tokens, line);
		} else if (TokenUtil.getInstance().checkContains(super.getStringExpression())) {
			result = new ContainsExpression(super.getStringExpression()).parse(tokens, line);
		} else if (TokenUtil.getInstance().checkNotExpression(super.getStringExpression())) {
			result = new NotExpression(super.getStringExpression()).parse(tokens, line);
		} else if (TokenUtil.getInstance().checkTrue(super.getStringExpression())) {
			result = TRUE;
		} else if (TokenUtil.getInstance().checkFalse(super.getStringExpression())) {
			result = FALSE;
		}
		return result;
	}

}
