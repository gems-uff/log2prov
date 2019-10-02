package log2prov.language.expressions;

import java.util.Map;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class ConcatExpression extends Expression {

	public ConcatExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkConcatenation(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String[] slices = TokenUtil.getInstance().supressReserved(super.getStringExpression()).split("\\s*\\+\\s*");
			if (slices.length > 0) {
				for (int i = 0; i < slices.length; i++) {
					if (TokenUtil.getInstance().checkQuotedString(slices[i])) {
						result += slices[i].replace("\"", "");
					} else {
						result += new Expression(TokenUtil.getInstance().impressReserved(slices[i])).parse(tokens, line);
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
