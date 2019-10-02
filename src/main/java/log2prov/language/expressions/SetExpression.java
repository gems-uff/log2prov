package log2prov.language.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class SetExpression extends Expression {

	private List<Expression> expressions;
	private List<String> vars;

	public SetExpression(String expr) throws InvalidExpression {
		super(expr);
		this.expressions = new ArrayList<Expression>();
		this.vars = new ArrayList<>();
		if (TokenUtil.getInstance().checkSetExpression(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		} else {
			String[] slices = expr.split("\\s*,\\s*");
			for (int i = 0; i < slices.length; i++) {
				expressions.add(new Expression(slices[i]));
				vars.add(slices[i].substring(1,slices[i].length()));
			}
		}
	}

	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			for (Expression expression : expressions) {
				result = expression.parse(tokens, line) + ",";
			}
			if (result.endsWith(",")) {
				result = result.substring(0, result.length() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão " + super.getStringExpression()
					+ "! Detalhes: " + e.getMessage());
		}
		return result;
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
	}

	public List<String> getVars() {
		return vars;
	}

	public void setVars(List<String> vars) {
		this.vars = vars;
	}

}
