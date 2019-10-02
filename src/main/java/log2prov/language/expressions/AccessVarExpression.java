package language.expressions;

import java.util.Map;

import exception.InvalidExpression;
import util.TokenUtil;

public class AccessVarExpression extends Expression {

	public AccessVarExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkAccessVar(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			result = tokens.get(super.getStringExpression().substring(1)).parse(tokens, line);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão " + super.getStringExpression()
					+ "! Detalhes: " + e.getMessage());
		}
		return result;
	}

}
