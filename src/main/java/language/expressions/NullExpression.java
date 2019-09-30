package language.expressions;

import java.util.Map;

import exception.InvalidExpression;

public class NullExpression extends Expression {

	public NullExpression() {
		super(NULL);
	}
	
	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		return "-";
	}

}
