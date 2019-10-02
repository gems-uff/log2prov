package log2prov.language.expressions;

import java.util.Map;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class OrExpression extends Expression {

	public OrExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkOrExpression(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String[] slices = TokenUtil.getInstance().supressReserved(super.getStringExpression())
					.split("\\s*\\|\\|\\s*");
			if (slices.length > 0) {

				if (TokenUtil.getInstance().checkBooleanExpression(slices[0])) {
					result = new BooleanExpression(TokenUtil.getInstance().impressReserved(slices[0])).parse(tokens, line);
				} else {
					throw new InvalidExpression("Expressão AND inválida! Expressão não booleana entre &&. Expressão: "
							+ super.getStringExpression());
				}

				for (int i = 1; i < slices.length; i++) {
					if (TokenUtil.getInstance().checkBooleanExpression(slices[i])) {
						result = "" +(Boolean.parseBoolean(result) 
								|| Boolean.parseBoolean(new BooleanExpression(TokenUtil.getInstance().impressReserved(slices[i])).parse(tokens, line)));
					} else {
						throw new InvalidExpression(
								"Expressão OR inválida! Expressão não booleana entre ||. Expressão: "
										+ super.getStringExpression());
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
