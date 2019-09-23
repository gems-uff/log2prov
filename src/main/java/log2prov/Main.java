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

import exception.InvalidExpression;
import language.expressions.Expression;
import language.expressions.StatementExpression;
import language.expressions.StringExpression;
import readers.ConfigurationFileReader;

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
		this.activities = new ArrayList<>();
		this.namespacePrefix = "default";
		this.namespace = "";
	}

	private void process(String definitionsFile, String inputFile, String outputFile) {
		try {
			ConfigurationFileReader config = new ConfigurationFileReader(definitionsFile);
			config.readFile();
			agents = config.getDefinitions().getAgents();
			activities = config.getDefinitions().getActivities();
			entities = config.getDefinitions().getActivities();
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
			try {
				String line = br.readLine();
				while (line != null) {
					config.getDefinitions().getTokens().put("line", new StringExpression(line));
					if (config.getDefinitions().getLineTest().parse(config.getDefinitions().getTokens(), line)
							.equals(TRUE)) {
						processAgents(config, fw, line);
						processActivities(config, fw, line);
						processEntities(config, fw, line);
						processStatements(config, fw, line);
					}
					lineNumber++;
				}
			} catch (Exception e) {
				System.err.println(
						e.getMessage() + "\n Linha do erro: " + lineNumber + "\nExpressão do erro: " + e.getMessage());
				e.printStackTrace();
			} finally {
				br.close();
			}
			fw.write("endDocument");
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
		for (String ag : agents) {
			if (!agents.contains(tokens.get(ag).parse(tokens, line))) {
				agents.add(ag);
				fw.write("agent(\"" + ag + "\")\n");
			}
		}
	}

	private void processActivities(ConfigurationFileReader config, FileWriter fw, String line)
			throws InvalidExpression, IOException {
		Map<String, Expression> tokens = config.getDefinitions().getTokens();
		for (String ac : activities) {
			if (!activities.contains(tokens.get(ac).parse(tokens, line))) {
				activities.add(ac);
				fw.write("activity(\"" + ac + "\")\n");
			}
		}
	}

	private void processEntities(ConfigurationFileReader config, FileWriter fw, String line)
			throws InvalidExpression, IOException {
		Map<String, Expression> tokens = config.getDefinitions().getTokens();
		for (String e : entities) {
			if (!entities.contains(tokens.get(e).parse(tokens, line))) {
				entities.add(e);
				fw.write("entity(\"" + e + "\")\n");
			}
		}
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
