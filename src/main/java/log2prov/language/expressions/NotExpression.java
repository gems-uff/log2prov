package log2prov.language.expressions;

import java.util.Map;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class NotExpression extends Expression {

	public NotExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkNotExpression(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String token = super.getStringExpression().replace(" ", "").substring(1);

			if (TokenUtil.getInstance().checkBooleanExpression(token)) {
				result = new BooleanExpression(TokenUtil.getInstance().impressReserved(token)).parse(tokens, line);
				if (result.equals(TRUE)) {
					return FALSE;
				} if (result.equals(FALSE)) {
					return TRUE;
				}
			} else {
				throw new InvalidExpression("Expressão NOT inválida! Expressão não booleana após !. Expressão: "
						+ super.getStringExpression());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão " + super.getStringExpression()
					+ "! Detalhes: " + e.getMessage());
		}
		return result;
	}

}
