package log2prov.language.expressions;

import java.util.Map;

import log2prov.exception.InvalidExpression;
import log2prov.util.TokenUtil;

public class AttributionExpression extends Expression {

	private String var;
	private Expression valueExpression;

	public AttributionExpression() {
		super();
	}

	public AttributionExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkAttribution(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		} else {
			String[] slices = expr.split("\\s*=\\s*",2);
			String left = slices[0];
			String right = slices[1];
			if (left != null && right != null) {
				var = left;
				valueExpression = new Expression(right);
			}
		}
	}

	@Override
	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			result = new Expression(super.getStringExpression()).parse(tokens, line);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão " + super.getStringExpression()
					+ "! Detalhes: " + e.getMessage());
		}
		return result;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public Expression getValue() {
		return valueExpression;
	}

	public void setValue(Expression value) {
		this.valueExpression = value;
	}

}
