package language;

import java.util.Map;

import exception.InvalidExpression;

public class ReplaceExpression extends Expression {

	public ReplaceExpression(String expr) throws InvalidExpression {
		if (getType() != REPLACE) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		String[] slices = super.getStringExpression().split("\\.replace\\(");
		String left = slices[0];
		if (left != null && slices.length > 1) {
			LiteralExpression leftLiteral = new LiteralExpression(left);
			result = leftLiteral.parse(tokens, line);
			for (int i = 1; i < slices.length; i++) {
				String leftParam = parseParam(slices[i], 0, tokens, line);
				String rightParam = parseParam(slices[i], 1, tokens, line);
				result = result.replace(leftParam, rightParam);
			}
		}
		return result;
	}

	private String parseParam(String paramsExpression, int i, Map<String, Expression> tokens, String line)
			throws InvalidExpression {
		String result = "";
		if (paramsExpression != null) {
			paramsExpression = paramsExpression.replace(")", "");
			String[] slices = paramsExpression.split(",");
			result = new LiteralExpression(slices[i]).parse(tokens, line);
		}
		return result;
	}

}
