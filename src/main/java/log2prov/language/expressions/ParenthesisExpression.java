package log2prov.language.expressions;

import java.util.Map;
import java.util.Stack;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class ParenthesisExpression extends Expression {

	public ParenthesisExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkParenthesisExpression(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String token = TokenUtil.getInstance().supressReserved(super.getStringExpression());
			if (token != null && token.contains("(")) {
				Stack<Integer> s = new Stack<>();
				for (int i = 0; i < token.length(); i++) {
					if (token.charAt(i) == '(') {
						s.push(i);
					} else if (token.charAt(i) == ')') {
						int begin = s.pop();
						String t = token.substring(begin, i + 1);
						Expression e = new Expression(TokenUtil.getInstance()
								.impressReserved(token.substring(begin + 1, i).replace("(", "").replace(")", "")));
						result = token.replace(t, e.parse(tokens, line));
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
