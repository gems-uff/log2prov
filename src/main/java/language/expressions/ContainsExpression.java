package language.expressions;

import java.util.Map;

import exception.InvalidExpression;
import util.TokenUtil;

public class ContainsExpression extends Expression {

	public ContainsExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkContains(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		} 
	}

	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String[] slices = super.getStringExpression().split("\\.");
			String left = slices[0];
			String right = slices[1];
			if (left != null && right != null) {
				String[] innerSlice = right.split("contains\\(");
				Expression leftLiteral = new Expression(left);
				Expression rightLiteral = new Expression(innerSlice[1].replace(")", ""));
				if (leftLiteral.parse(tokens, line).contains(rightLiteral.parse(tokens, line))) {
					result = TRUE;
				} else {
					result = FALSE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão " + super.getStringExpression()
					+ "! Detalhes: " + e.getMessage());
		}
		return result;
	}

}
