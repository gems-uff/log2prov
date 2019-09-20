package language;

import java.util.Map;

import exception.InvalidExpression;

public class ContainsExpression extends Expression {

	public ContainsExpression(String stringExpression) throws InvalidExpression {
		super(stringExpression);
		if (getType() != CONTAINS) {
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
				LiteralExpression leftLiteral = new LiteralExpression(left);
				LiteralExpression rightLiteral = new LiteralExpression(innerSlice[1].replace(")", ""));
				if (leftLiteral.parse(tokens, line).contains(rightLiteral.parse(tokens, line))) {
					result = TRUE;
				} else {
					result = FALSE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão contains! Detalhes: " + e.getMessage());
		}
		return result;
	}

}
