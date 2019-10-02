package readers;

import static language.expressions.ExpressionInterface.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import exception.InvalidExpression;
import language.expressions.Expression;
import language.expressions.StatementExpression;

public class LogFileReader {

	private ConfigurationFileReader config;
	private File logFile;
	private FileWriter outputFile;

	public LogFileReader() {
		super();
		this.config = new ConfigurationFileReader();
	}

	public LogFileReader(ConfigurationFileReader config, String input, String output)
			throws URISyntaxException, IOException {
		this();
		this.logFile = new File(input);
		this.outputFile = new FileWriter(output);
		this.config = config;
	}

	public void process() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(getLogFile()));
		int lineNumber = 1;
		try {
			String line = br.readLine();
			while (line != null) {
				processLine(line);
				line = br.readLine();
				lineNumber++;
			}
		} catch (Exception e) {
			System.err.println(
					e.getMessage() + "\nLinha do erro: " + lineNumber + "\nExpressão do erro: " + e.getMessage());
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

	private void processLine(String line) {
		if (line != null && line.length() > 0) {
			try {
				List<Expression> statements = config.getDefinitions().getStatements();
				Map<String, Expression> tokens = config.getDefinitions().getTokens();
				if (isLineValid(config.getDefinitions().getLineTest(), tokens, line)) {
					for (Expression expression : statements) {
						if (expression instanceof StatementExpression) {
							StatementExpression statement = (StatementExpression) expression;
							if (statement.isValid(tokens, line)) {
								outputFile.write(statement.parse(tokens, line));
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidExpression e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean isLineValid(Expression lineTest, Map<String, Expression> tokens, String line)
			throws InvalidExpression {
		return lineTest != null && lineTest.parse(tokens, line).equals(TRUE);
	}

	public ConfigurationFileReader getConfig() {
		return config;
	}

	public void setConfig(ConfigurationFileReader config) {
		this.config = config;
	}

	public File getLogFile() {
		return logFile;
	}

	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}

	public FileWriter getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(FileWriter outputFile) {
		this.outputFile = outputFile;
	}

}
