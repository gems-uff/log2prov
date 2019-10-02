package log2prov.language.expressions;

import java.util.Map;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class ReplaceExpression extends Expression {

	public ReplaceExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkReplace(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String[] slices = super.getStringExpression().split("\\.replace\\(");
			String left = slices[0];
			if (left != null && slices.length > 1) {
				Expression leftLiteral = new Expression(left);
				result = leftLiteral.parse(tokens, line);
				for (int i = 1; i < slices.length; i++) {
					String leftParam = parseParam(slices[i], 0, tokens, line);
					String rightParam = parseParam(slices[i], 1, tokens, line);
					result = result.replace(leftParam, rightParam);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão " + super.getStringExpression()
					+ "! Detalhes: " + e.getMessage());
		}
		return result;
	}

	private String parseParam(String paramsExpression, int i, Map<String, Expression> tokens, String line)
			throws InvalidExpression {
		String result = ""; 
		if (paramsExpression != null) {
			String[] slices = paramsExpression.split("\\s*,\\s*");
			if (i == 0 || (i == 1 && slices.length == 2)) {
				if (i==0) {
					result = new Expression(slices[i]).parse(tokens, line);
				} else {
					result = new Expression(slices[i].substring(0,slices[i].lastIndexOf(")"))).parse(tokens, line);
				}
			} else {
				result = "";
			}
		}
		return result;
	}

}
