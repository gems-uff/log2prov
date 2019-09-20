package language;

import java.util.Map;

import exception.InvalidExpression;

public class LiteralExpression extends Expression {

	public LiteralExpression(String expression) throws InvalidExpression {
		super(expression);
		if (getType() != LITERAL && getType() != ACCESS_VAR) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) {
		String result = "";
		if (getType() == ACCESS_VAR) {
			String var = super.getStringExpression().substring(1);
			result = result.replace(var, matchFirst(line, tokens.get(var).getStringExpression()));
		} else {
			result = super.getStringExpression();
		}
		return result;
	}

}
