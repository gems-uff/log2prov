package language;

import java.util.Map;

import exception.InvalidExpression;

public class AttributionExpression extends Expression {

	private String var;
	private Expression valueExpression;

	public AttributionExpression() {
		super();
	}

	public AttributionExpression(String stringExpression) throws InvalidExpression {
		super(stringExpression);
		if (getType() != ATTRIBUTION) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		} else {
			String[] slices = stringExpression.split("=");
			String left = slices[0];
			String right = slices[1];
			if (left != null && right != null) {
				var = left;
				valueExpression = new Expression(right);
			}
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		if (valueExpression.getType() == LITERAL) {
			LiteralExpression literal = new LiteralExpression(valueExpression.getStringExpression());
			result = literal.parse(tokens, line);
		} else if (valueExpression.getType() == IF_THEN_ELSE) {
			IfThenElseExpression ifThenElse = new IfThenElseExpression(valueExpression.getStringExpression());
			result = ifThenElse.parse(tokens, line);
		}
		return result;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public Expression getValue() {
		return valueExpression;
	}

	public void setValue(Expression value) {
		this.valueExpression = value;
	}

}
