package language;

import java.util.Map;

import exception.InvalidExpression;

public class NumberExpression extends Expression {

	public NumberExpression(String expression) throws InvalidExpression {
		super(expression);
		if (getType() != NUMBER) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			result = super.getStringExpression();
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão " + super.getStringExpression()
					+ "! Detalhes: " + e.getMessage());
		}
		return result;
	}
}
