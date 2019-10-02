package log2prov.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import log2prov.language.expressions.Expression;

public class LanguageDefinitions {
	
	private Map<String, Expression> tokens;
	private List<String> agents;
	private List<String> entities;
	private List<String> activities;
	private List<Expression> statements;
	private Expression lineTest;

	public LanguageDefinitions() {
		super();
		this.setTokens(new HashMap<>());
		this.setStatements(new ArrayList<>());
		this.setAgents(new ArrayList<>());
		this.setActivities(new ArrayList<>());
		this.setEntities(new ArrayList<>());
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


	public List<String> getAgents() {
		return agents;
	}


	public void setAgents(List<String> agents) {
		this.agents = agents;
	}


	public List<String> getEntities() {
		return entities;
	}


	public void setEntities(List<String> entities) {
		this.entities = entities;
	}


	public List<String> getActivities() {
		return activities;
	}


	public void setActivities(List<String> activities) {
		this.activities = activities;
	}
}