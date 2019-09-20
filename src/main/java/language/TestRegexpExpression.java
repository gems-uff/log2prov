package language;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidExpression;

public class TestRegexpExpression extends Expression {

	public TestRegexpExpression(String stringExpression) throws InvalidExpression {
		super(stringExpression);
		if (getType() != TEST_REGEXP) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String[] slices = super.getStringExpression().split("testRegexp\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split(",");
				LiteralExpression leftLiteral = new LiteralExpression(innerSlice[0]);
				LiteralExpression rightLiteral = new LiteralExpression(innerSlice[1]);
				Pattern p = Pattern.compile(rightLiteral.parse(tokens, line));
				Matcher m = p.matcher(leftLiteral.parse(tokens, line));
				if (m.find()) {
					result = TRUE;
				} else {
					result = FALSE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão testRegexp! Detalhes: " + e.getMessage());
		}
		return result;
	}

}
