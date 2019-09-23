package language.expressions;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidExpression;
import util.TokenChecker;

public class MatchExpression extends Expression {

	public MatchExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenChecker.getInstance().checkMatch(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String[] slices = super.getStringExpression().split("\\.match\\(");
			String left = slices[0];
			if (left != null && slices.length > 1) {
				Expression leftLiteral = new Expression(left);
				result = leftLiteral.parse(tokens, line);
				for (int i = 1; i < slices.length; i++) {
					String regexp = slices[i].substring(0, slices[i].length() - 1).replace("\"", "");
					Pattern p = Pattern.compile(regexp);
					Matcher m = p.matcher(result);
					if (m.find()) {
						result = result.substring(m.start(), m.end());
					}

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
