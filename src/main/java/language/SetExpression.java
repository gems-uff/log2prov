package language;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exception.InvalidExpression;

public class SetExpression extends Expression {

	private List<Expression> expressions;

	public SetExpression(String stringExpression) throws InvalidExpression {
		super(stringExpression);
		this.expressions = new ArrayList<Expression>();
		if (getType() != SET) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		} else {
			String[] slices = stringExpression.split("\\s*,\\s*");
			for (int i = 0; i < slices.length; i++) {
				expressions.add(new Expression(slices[i]));
			}
		}
	}

	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			for (Expression expression : expressions) {
				result = expression.parse(tokens, line) + ",";
			}
			if (result.endsWith(",")) {
				result = result.substring(0, result.length() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão " + super.getStringExpression()
					+ "! Detalhes: " + e.getMessage());
		}
		return result;
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
	}

}
