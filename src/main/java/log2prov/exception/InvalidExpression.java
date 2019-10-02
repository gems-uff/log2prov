package exception;

public class InvalidExpression extends Exception {

	private static final long serialVersionUID = -443891194096324729L;
	
	private String expression;

	public InvalidExpression() {
		super("Expressão inválida!");
	}
	
	public InvalidExpression(String message) {
		super(message);
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
