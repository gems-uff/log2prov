package log2prov;

import static language.expressions.ExpressionInterface.TRUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static language.expressions.ExpressionInterface.*;
import exception.InvalidExpression;
import language.expressions.Expression;
import language.expressions.IfThenElseExpression;
import language.expressions.StatementExpression;
import language.expressions.StringExpression;
import readers.ConfigurationFileReader;
import util.FileUtil;
import util.TokenUtil;

public class Main {

	private List<String> agents;
	private List<String> activities;
	private List<String> entities;
	private String namespacePrefix;
	private String namespace;

	public Main() {
		super();
		this.agents = new ArrayList<>();
		this.activities = new ArrayList<>();
		this.entities = new ArrayList<>();
		this.namespacePrefix = "default";
		this.namespace = "";
	}

	private void process(String definitionsFile, String inputFile, String outputFile) {
		try {
			ConfigurationFileReader config = new ConfigurationFileReader(definitionsFile);
			config.readFile();
			FileWriter fw = new FileWriter(outputFile);
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			int lineNumber = 1;
			fw.write("document\n");
			fw.write(this.namespacePrefix);
			if (this.namespace.length() > 0) {
				fw.write(" <" + this.namespace + ">\n");
			} else {
				fw.write("\n");
			}
			int totalLines = FileUtil.getInstance().countLines(inputFile) + 1;
			try {
				String line = br.readLine();
				while (line != null) {
					System.out.print("\rProcessing line " + lineNumber + " of " + totalLines + "...");
					line = line.trim();
					config.getDefinitions().getTokens().put("line", new StringExpression(line));
					if (config.getDefinitions().getLineTest().parse(config.getDefinitions().getTokens(), line)
							.equals(TRUE)) {
						processAgents(config, fw, line);
						processActivities(config, fw, line);
						processEntities(config, fw, line);
						processStatements(config, fw, line);
					}
					lineNumber++;
					line = br.readLine();
				}
				lineNumber--;
				System.out.print("\rProcessing line " + lineNumber + " of " + totalLines + ". Finished!");
			} catch (Exception e) {
				System.err.println(
						e.getMessage() + "\n Linha do erro: " + lineNumber + "\nExpressão do erro: " + e.getMessage());
				e.printStackTrace();
			} finally {
				br.close();
			}
			fw.write("endDocument");
			fw.close();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processAgents(ConfigurationFileReader config, FileWriter fw, String line)
			throws InvalidExpression, IOException {
		Map<String, Expression> tokens = config.getDefinitions().getTokens();
		for (String ag : config.getDefinitions().getAgents()) {
			Expression expr = tokens.get(ag);
			if (expr != null) {
				String agent = expr.parse(tokens, line);
				if (!agents.contains(agent) && !agent.equals(NULL)) {
					agents.add(agent);
					fw.write("agent(\"" + agent + "\")\n");
				}
			}
		}
	}

	private void processActivities(ConfigurationFileReader config, FileWriter fw, String line)
			throws InvalidExpression, IOException {
		Map<String, Expression> tokens = config.getDefinitions().getTokens();
		List<String> newActivities = new ArrayList<>();
		for (String ac : config.getDefinitions().getActivities()) {
			Expression expr = tokens.get(ac);
			if (expr != null) {
				String activity = expr.parse(tokens, line);
				if (!activities.contains(activity) && !activity.equals(NULL)) {
					activities.add(activity);
					fw.write("activity(\"" + activity + "\", -, -)\n");
				}
			}
		}
		activities.addAll(newActivities);
	}

	private void processEntities(ConfigurationFileReader config, FileWriter fw, String line)
			throws InvalidExpression, IOException {
		Map<String, Expression> tokens = config.getDefinitions().getTokens();
		List<String> newEntities = new ArrayList<>();
		for (String e : config.getDefinitions().getEntities()) {
			if (line.startsWith("07:14:25,610 ERROR")) {
				System.out.println("Interessa!");
			}
			Expression expr = tokens.get(e);
			if (expr != null) {
				String entity = expr.parse(tokens, line);
				if (!entities.contains(entity) && !entity.equals(NULL)) {
					newEntities.add(entity);
					fw.write("entity(\"" + entity + "\")\n");
				}
			}
		}
		entities.addAll(newEntities);
	}

	private void processStatements(ConfigurationFileReader config, FileWriter fw, String line)
			throws IOException, InvalidExpression {
		List<Expression> statements = config.getDefinitions().getStatements();
		for (Expression expr : statements) {
			if (expr instanceof StatementExpression) {
				StatementExpression statement = (StatementExpression) expr;
				if (statement.isValid(config.getDefinitions().getTokens(), line)) {
					fw.write(statement.parse(config.getDefinitions().getTokens(), line) + "\n");
				}
			} else if (expr instanceof IfThenElseExpression) {
				IfThenElseExpression thenElse = (IfThenElseExpression) expr;
				if (thenElse.getCondition().parse(config.getDefinitions().getTokens(), line).equals(TRUE)) {
					Expression trueConsequence = thenElse.getTrueConsequence();
					if (TokenUtil.getInstance().checkStatement(trueConsequence.getStringExpression())) {
						StatementExpression stmt = new StatementExpression(trueConsequence.getStringExpression());
						if (stmt.isValid(config.getDefinitions().getTokens(), line)) {
							fw.write(stmt.parse(config.getDefinitions().getTokens(), line) + "\n");
						}
					} else {
						throw new InvalidExpression(
								"Statement not specified in the consequences of If-Then-Else expression inside [statements]");
					}
				} else {
					Expression falseConsequence = thenElse.getFalseConsequence();
					if (falseConsequence != null) {
						if (TokenUtil.getInstance().checkStatement(falseConsequence.getStringExpression())) {
							StatementExpression stmt = new StatementExpression(falseConsequence.getStringExpression());
							if (stmt.isValid(config.getDefinitions().getTokens(), line)) {
								fw.write(stmt.parse(config.getDefinitions().getTokens(), line) + "\n");
							}
						} else {
							throw new InvalidExpression(
									"Statement not specified in the consequences of If-Then-Else expression inside [statements]");
						}
					}
				}
			} else {
				throw new InvalidExpression(
						"Statement not specified in the consequences of If-Then-Else expression inside [statements]");
			}
		}
	}

	public static void main(String[] args) {
		Main log2Prov = new Main();
		String definitionsFile = null;
		String inputFile = null;
		String outputFile = null;
		System.out.println("Log2Prov v1.0");
		System.out.println();
		if (args.length == 0 || args.length > 10) {
			System.out.println("Invalid option! Try -h or --help for help!");
		} else if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help"))) {
			System.out.println("Options:");
			System.out.println();
			System.out.println("-d <configuration_file>");
			System.out.println("-i <input_log_file>");
			System.out.println("-o <output_provn_file>");
			System.out.println("-p namespace prefix [optional]");
			System.out.println("-n namespace [optional]");
		} else if (args.length >= 6 && args.length <= 10) {
			if (args.length == 8) {
				if (args[6].equals("-p")) {
					log2Prov.setNamespacePrefix(args[7]);
				}
				if (args[8].equals("-n")) {
					log2Prov.setNamespace(args[9]);
				}
			} else if (args.length == 7) {

			}
			if (args[0].equals("-d")) {
				definitionsFile = args[1];
			}
			if (args[2].equals("-i")) {
				inputFile = args[3];
			}
			if (args[4].equals("-o")) {
				outputFile = args[5];
			}
			if (definitionsFile == null) {
				System.out.println("Configuration file not informed! Try -h or --help for help!");
			} else if (inputFile == null) {
				System.out.println("Input log file not informed! Try -h or --help for help!");
			} else if (outputFile == null) {
				System.out.println("Output prov-n file not informed! Try -h or --help for help!");
			} else {
				log2Prov.process(definitionsFile, inputFile, outputFile);
			}
		}

	}

	public String getNamespacePrefix() {
		return namespacePrefix;
	}

	public void setNamespacePrefix(String namespacePrefix) {
		this.namespacePrefix = namespacePrefix;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
