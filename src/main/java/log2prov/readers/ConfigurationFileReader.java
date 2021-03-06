package log2prov.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import log2prov.exception.InvalidExpression;
import log2prov.language.LanguageDefinitions;
import log2prov.language.expressions.AttributionExpression;
import log2prov.language.expressions.BooleanExpression;
import log2prov.language.expressions.Expression;
import log2prov.language.expressions.IfThenElseExpression;
import log2prov.language.expressions.SetExpression;
import log2prov.language.expressions.StatementExpression;
import log2prov.util.TokenUtil;

public class ConfigurationFileReader {

	private File configFile;
	private LanguageDefinitions definitions;
	private boolean isHeaderLine;
	private boolean isHeaderToken;
	private boolean isHeaderStatements;
	private boolean isHeaderActivities;
	private boolean isHeaderAgents;
	private boolean isHeaderEntities;

	public ConfigurationFileReader() {
		super();
		this.definitions = new LanguageDefinitions();
	}

	public ConfigurationFileReader(String f) throws URISyntaxException, IOException {
		this();
		this.setConfigFile(new File(f));
	}

	public void readFile() throws URISyntaxException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(getConfigFile()));
		int lineNumber = 1;
		try {
			String line = br.readLine();
			if (line != null) {
				line = line.trim();
			}
			while (line != null) {
				processLine(line);
				line = br.readLine();
				if (line != null) {
					line = line.trim();
				}
				if (isHeaderLine) {
					processHeaderLineContent(line);
				} else if (isHeaderToken) {
					processHeaderTokenContent(line);
				} else if (isHeaderAgents) {
					processHeaderAgentsContent(line);
				} else if (isHeaderActivities) {
					processHeaderActivitiesContent(line);
				} else if (isHeaderEntities) {
					processHeaderEntitiesContent(line);
				} else if (isHeaderStatements) {
					processHeaderStatementsContent(line);
				}
				lineNumber++;
			}
		} catch (InvalidExpression e) {
			System.err.println(
					e.getMessage() + "\n Linha do erro: " + lineNumber + "\nExpressão do erro: " + e.getExpression());
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

	private void processHeaderStatementsContent(String line) throws InvalidExpression {
		if (line != null && line.length() > 0) {
			Expression expr = null;
			if (TokenUtil.getInstance().checkIfThenElse(line)) {
				expr = new IfThenElseExpression(line);
			} else if (TokenUtil.getInstance().checkStatement(line)) {
				expr = new StatementExpression(line);
			}
			if (expr == null) {
				throw new InvalidExpression("Statement definition not recognized inside [statements]");
			}
			this.definitions.getStatements().add(expr);
			isHeaderStatements = expr.isEndHeaderToken(line);
		}
	}

	private void processHeaderAgentsContent(String line) throws InvalidExpression {
		if (line != null && line.length() > 0 && isHeaderAgents) {
			SetExpression expr = new SetExpression(line);
			this.definitions.setAgents(expr.getVars());
			isHeaderAgents = false;
		}
	}

	private void processHeaderActivitiesContent(String line) throws InvalidExpression {
		if (line != null && line.length() > 0 && isHeaderActivities) {
			SetExpression expr = new SetExpression(line);
			this.definitions.setActivities(expr.getVars());
			isHeaderActivities = false;
		}
	}

	private void processHeaderEntitiesContent(String line) throws InvalidExpression {
		if (line != null && line.length() > 0 && isHeaderEntities) {
			SetExpression expr = new SetExpression(line);
			this.definitions.setEntities(expr.getVars());
			isHeaderEntities = false;
		}
	}

	private void processHeaderLineContent(String line) throws InvalidExpression {
		if (TokenUtil.getInstance().checkBooleanExpression(line)) {
			this.definitions.setLineTest(new BooleanExpression(line));
		}
		isHeaderLine = false;

	}

	private void processHeaderTokenContent(String line) throws InvalidExpression {
		if (line != null && line.length() > 0) {
			Expression expr = new Expression(line);
			if (TokenUtil.getInstance().checkAttribution(line)) {
				AttributionExpression att = new AttributionExpression(line);
				this.definitions.getTokens().put(att.getVar(), att.getValue());
			}
			isHeaderToken = expr.isEndHeaderToken(line);
		}

	}

	public void processLine(String line) {
		if (TokenUtil.getInstance().checkLineHeader(line)) {
			isHeaderLine = true;
		} else if (TokenUtil.getInstance().checkTokensHeader(line)) {
			isHeaderToken = true;
		} else if (TokenUtil.getInstance().checkStatementsHeader(line)) {
			isHeaderStatements = true;
		} else if (TokenUtil.getInstance().checkAgentsHeader(line)) {
			isHeaderAgents = true;
		} else if (TokenUtil.getInstance().checkActivitiesHeader(line)) {
			isHeaderActivities = true;
		} else if (TokenUtil.getInstance().checkEntitiesHeader(line)) {
			isHeaderEntities = true;
		}
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public LanguageDefinitions getDefinitions() {
		return definitions;
	}

	public void setDefinitions(LanguageDefinitions definitions) {
		this.definitions = definitions;
	}

	public boolean isHeaderLine() {
		return isHeaderLine;
	}

	public void setHeaderLine(boolean isHeaderLine) {
		this.isHeaderLine = isHeaderLine;
	}

	public boolean isHeaderToken() {
		return isHeaderToken;
	}

	public void setHeaderToken(boolean isHeaderToken) {
		this.isHeaderToken = isHeaderToken;
	}

	public boolean isHeaderStatements() {
		return isHeaderStatements;
	}

	public void setHeaderStatements(boolean isHeaderStatements) {
		this.isHeaderStatements = isHeaderStatements;
	}

	public boolean isHeaderActivities() {
		return isHeaderActivities;
	}

	public void setHeaderActivities(boolean isHeaderActivities) {
		this.isHeaderActivities = isHeaderActivities;
	}

	public boolean isHeaderAgents() {
		return isHeaderAgents;
	}

	public void setHeaderAgents(boolean isHeaderAgents) {
		this.isHeaderAgents = isHeaderAgents;
	}

	public boolean isHeaderEntities() {
		return isHeaderEntities;
	}

	public void setHeaderEntities(boolean isHeaderEntities) {
		this.isHeaderEntities = isHeaderEntities;
	}

}
