package language;

import java.util.Map;

import exception.InvalidExpression;

public class IfThenElseExpression extends Expression {

	private BooleanExpression condition;
	private Expression trueConsequence;
	private Expression falseConsequence;

	public IfThenElseExpression(String expression) throws InvalidExpression {
		super(expression);
		if (getType() != IF_THEN_ELSE) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		} else {
			String[] slices = getStringExpression().split(CONDITION_SEPARATOR);
			condition = new BooleanExpression(slices[0]);
			String[] thenElse = slices[1].split(ELSE_SEPARATOR);
			String then = thenElse[0];
			trueConsequence = new Expression(then);
			String elze = null;
			if (thenElse.length == 2) {
				elze = thenElse[1];
				falseConsequence = new Expression(elze);
			}
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		if (condition.parse(tokens, line).equals(TRUE)) {
			result = trueConsequence.parse(tokens, line);
		} else if (condition.parse(tokens, line).equals(FALSE)) {
			result = falseConsequence.parse(tokens, line);
		} else {
			throw new InvalidExpression("Expressão booleana retornando valor diferente de true ou false!");
		}
		return result;
	}

	public Expression getTrueConsequence() {
		return trueConsequence;
	}

	public void setTrueConsequence(Expression trueConsequence) {
		this.trueConsequence = trueConsequence;
	}

	public Expression getFalseConsequence() {
		return falseConsequence;
	}

	public void setFalseConsequence(Expression falseConsequence) {
		this.falseConsequence = falseConsequence;
	}

	public Expression getCondition() {
		return condition;
	}

	public void setCondition(BooleanExpression condition) {
		this.condition = condition;
	}

}
