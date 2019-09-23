package language.expressions;

import java.util.Map;

import exception.InvalidExpression;
import util.TokenChecker;

public class IfThenElseExpression extends Expression {

	private BooleanExpression condition;
	private Expression trueConsequence;
	private Expression falseConsequence;

	public IfThenElseExpression(String expression) throws InvalidExpression {
		super(expression);
		if (!TokenChecker.getInstance().checkIfThenElse(expression)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		} else {
			String[] slices = getStringExpression().split("\\s*\\"+CONDITION_SEPARATOR+"\\s*");
			condition = new BooleanExpression(slices[0]);
			String[] thenElse = slices[1].split("\\s*"+ELSE_SEPARATOR+"\\s*");
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
			result = trueConsequence.parse(tokens, line );
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

	public BooleanExpression getCondition() {
		return condition;
	}

	public void setCondition(BooleanExpression condition) {
		this.condition = condition;
	}

}
