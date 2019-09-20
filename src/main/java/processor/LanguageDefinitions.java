package processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import language.Expression;

public class LanguageDefinitions {
	
	private Map<String, Expression> tokens;
	private List<Expression> statements;
	private Expression lineTest;

	public LanguageDefinitions() {
		super();
		this.setTokens(new HashMap<>());
		this.setStatements(new ArrayList<>());
	}

	
	public Map<String, Expression> getTokens() {
		return tokens;
	}

	public void setTokens(Map<String, Expression> tokens) {
		this.tokens = tokens;
	}

	public List<Expression> getStatements() {
		return statements;
	}

	public void setStatements(List<Expression> statements) {
		this.statements = statements;
	}


	public Expression getLineTest() {
		return lineTest;
	}


	public void setLineTest(Expression lineTest) {
		this.lineTest = lineTest;
	}
}