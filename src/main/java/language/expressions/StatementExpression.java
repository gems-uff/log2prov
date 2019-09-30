package language.expressions;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidExpression;
import util.RegexpUtil;
import util.TokenUtil;

public class StatementExpression extends Expression {

	public StatementExpression(String expr) throws InvalidExpression {
		super(expr);
		if (!TokenUtil.getInstance().checkStatement(expr)) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	public boolean isValid(Map<String, Expression> tokens, String line) {
		boolean isValid = false;
		try {
			String[] slices = super.getStringExpression().split(getStatementName().replace("(", "\\("));
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				String secondParam = innerSlice[1].contains(")")
						? innerSlice[1].substring(0, innerSlice[1].indexOf(")"))
						: innerSlice[1];
				Expression leftLiteral = new Expression(innerSlice[0]);
				Expression rightLiteral = new Expression(secondParam);
				String param1 = leftLiteral.parse(tokens, line);
				String param2 = rightLiteral.parse(tokens, line);
				isValid = innerSlice[0] != null && innerSlice[0].length() > 0 && param1 != null && param1.length() > 0
						&& param2 != null && param2.length() > 0;
			}
		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}

	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String[] slices = super.getStringExpression().split(getStatementName().replace("(", "\\("));
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				String secondParam = innerSlice[1].contains(")")
						? innerSlice[1].substring(0, innerSlice[1].indexOf(")"))
						: innerSlice[1];
				Expression leftLiteral = new Expression(innerSlice[0]);
				Expression rightLiteral = new Expression(secondParam);
				result = super.getStringExpression().replace(innerSlice[0],
						"\"" + leftLiteral.parse(tokens, line) + "\"");
				result = result.replace(secondParam, "\"" + rightLiteral.parse(tokens, line) + "\"");
				result = RegexpUtil.getInstance().replace(result, "\\s*,\\s*", ", ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidExpression("Erro ao tentar processar expressão " + super.getStringExpression()
					+ "! Detalhes: " + e.getMessage());
		}
		return result;
	}

	private String getStatementName() {
		Pattern p = Pattern.compile("^\\w+\\(");
		Matcher m = p.matcher(super.getStringExpression());
		if (m.find()) {
			return super.getStringExpression().substring(m.start(), m.end());
		}
		return null;
	}

}
