package processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import exception.InvalidExpression;

import static language.ExpressionInterface.*;

import language.AttributionExpression;
import language.Expression;

public class ConfigurationLogReader {

	private File logFile;
	private LanguageDefinitions definitions;
	private boolean isHeaderLine;
	private boolean isHeaderToken;
	private boolean isHeaderAgents;
	private boolean isHeaderEntities;
	private boolean isHeaderActivities;
	private boolean isHeaderStatements;

	public ConfigurationLogReader() {
		super();
		this.definitions = new LanguageDefinitions();
	}

	public ConfigurationLogReader(String f) throws URISyntaxException, IOException {
		this();
		this.setLogFile(new File(f));
	}

	public void readFile() throws URISyntaxException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(getLogFile()));
		int lineNumber = 1;
		try {
			String line = br.readLine();
			line = line.replace(" ", "");
			line = line.replace("\"", "");
			while (line != null) {
				processLine(line);
				line = br.readLine();
				if (isHeaderLine) {
					processHeaderLineContent(line);
				} else if (isHeaderToken) {
					processHeaderTokenContent(line);
				} else if (isHeaderAgents) {
					processHeaderAgentContent(line);
				} else if (isHeaderEntities) {
					processHeaderEntitiesContent(line);
				} else if (isHeaderActivities) {
					processHeaderActivitiesContent(line);
				} else if (isHeaderStatements) {
					processHeaderStatementsContent(line);
				}
				lineNumber++;
			}
		} catch (InvalidExpression e) {
			System.err.println(
					e.getMessage() + "\nLinha do erro: " + lineNumber + "\nExpressão do erro: " + e.getExpression());
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

	private void processHeaderAgentContent(String line) {
		// TODO Auto-generated method stub

	}

	private void processHeaderStatementsContent(String line) {
		// TODO Auto-generated method stub
		
	}

	private void processHeaderActivitiesContent(String line) {
		// TODO Auto-generated method stub
		
	}

	private void processHeaderEntitiesContent(String line) {
		// TODO Auto-generated method stub
		
	}


	private void processHeaderLineContent(String line) {
		Expression expr = new Expression(line);
		if (expr.getType() == BOOLEAN) {
			this.definitions.setLineTest(expr);
		}
		isHeaderLine = false;

	}

	private void processHeaderTokenContent(String line) throws InvalidExpression {
		Expression expr = new Expression(line);
		if (expr.getType() == ATTRIBUTION) {
			AttributionExpression att = new AttributionExpression(line);
			this.definitions.getTokens().put(att.getVar(), att.getValue());
		}
		isHeaderToken = expr.isEndHeaderToken(line);

	}

	public void processLine(String line) {
		Expression expr = new Expression(line);
		int type = expr.getType();
		switch (type) {
		case HEADER_LINE: {
			isHeaderLine = true;
			break;
		}
		case HEADER_TOKENS: {
			isHeaderToken = true;
			break;
		}
		case HEADER_AGENTS: {
			isHeaderAgents = true;
			break;
		}
		case HEADER_ENTITIES: {
			isHeaderEntities = true;
			break;
		}
		case HEADER_ACTIVITIES: {
			isHeaderActivities = true;
			break;
		}
		case HEADER_STATEMENTS: {
			isHeaderStatements = true;
			break;
		}


		default:
			break;
		}
	}

	public File getLogFile() {
		return logFile;
	}

	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}

	public LanguageDefinitions getDefinitions() {
		return definitions;
	}

	public void setDefinitions(LanguageDefinitions definitions) {
		this.definitions = definitions;
	}

}
