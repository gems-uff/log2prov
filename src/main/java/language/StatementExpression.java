package language;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidExpression;
import util.RegexpUtil;

public class StatementExpression extends Expression {

	public StatementExpression(String stringExpression) throws InvalidExpression {
		super(stringExpression);
		if (getType() != STATEMENT) {
			InvalidExpression e = new InvalidExpression();
			e.setExpression(super.getStringExpression());
			throw e;
		}
	}

	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression {
		String result = "";
		try {
			String[] slices = super.getStringExpression().split(getStatementName().replace("(", "\\("));
			String right = slices[1];
			if (right != null) {
				String[] innerSlice = right.split("\\s*,\\s*");
				String secondParam = innerSlice[1].substring(0,innerSlice[1].indexOf(")"));
				Expression leftLiteral = new Expression(innerSlice[0]);
				Expression rightLiteral = new Expression(secondParam);
				result = super.getStringExpression().replace(innerSlice[0], leftLiteral.parse(tokens, line));
				result = result.replace(secondParam, rightLiteral.parse(tokens, line));
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
