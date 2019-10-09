package log2prov.language.expressions;

import java.util.Map;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class AndExpression extends Expression {

	public AndExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkAndExpression(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String[] slices = TokenUtil.getInstance().supressReserved(super.getStringExpression()).split("\\s*&&\\s*",
					2);
			if (slices.length == 2) {

				if (TokenUtil.getInstance().checkBooleanExpression(slices[0])) {
					result = new BooleanExpression(TokenUtil.getInstance().impressReserved(slices[0])).parse(tokens,
							line);
				} else {
					throw new InvalidExpression("Expressão AND inválida! Expressão não booleana entre &&. Expressão: "
							+ super.getStringExpression());
				}

				if (TokenUtil.getInstance().checkBooleanExpression(slices[1])) {
					result = "" + (Boolean.parseBoolean(result) && Boolean
							.parseBoolean(new BooleanExpression(TokenUtil.getInstance().impressReserved(slices[1]))
									.parse(tokens, line)));
				} else {
					throw new InvalidExpression("Expressão AND inválida! Expressão não booleana entre &&. Expressão: "
							+ super.getStringExpression());
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
