package log2prov.language.expressions;

import java.util.Map;

import log2prov.exception.InvalidExpression;

public class StringExpression extends Expression {

	public StringExpression(String line) {
		super(line);
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		return super.getStringExpression();
	}

}
