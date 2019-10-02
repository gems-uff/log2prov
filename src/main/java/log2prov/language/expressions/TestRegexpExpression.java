package log2prov.language.expressions;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class TestRegexpExpression extends Expression {

	public TestRegexpExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkTestRegexp(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String[] slices = TokenUtil.getInstance().supressReserved(super.getStringExpression()).split("testRegexp\\(");
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				String firstParam = innerSlice[0];
				String secondParam = innerSlice[1].substring(0, innerSlice[1].lastIndexOf(")"));
				Expression leftLiteral = new Expression(TokenUtil.getInstance().impressReserved(firstParam));
				Expression rightLiteral = new Expression(TokenUtil.getInstance().impressReserved(secondParam));
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
			throw new InvalidExpression("Erro ao tentar processar expressão " + super.getStringExpression()
					+ "! Detalhes: " + e.getMessage());
		}
		return result;
	}

}
