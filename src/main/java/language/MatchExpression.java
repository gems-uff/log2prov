package language;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidExpression;

public class MatchExpression extends Expression {

	public MatchExpression(String expr) throws InvalidExpression {
		if (getType() != MATCH) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		String[] slices = super.getStringExpression().split("\\.match\\(");
		String left = slices[0];
		if (left != null && slices.length > 1) {
			LiteralExpression leftLiteral = new LiteralExpression(left);
			result = leftLiteral.parse(tokens, line);
			for (int i = 1; i < slices.length; i++) {
				String regexp = slices[i].replace(")", "");
				Pattern p = Pattern.compile(regexp);
				Matcher m = p.matcher(result);
				if (m.find()) {
					result = result.substring(m.start(), m.end());
				}

			}
		}
		return result;
	}

}
