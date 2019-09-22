package language;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import exception.InvalidExpression;
import processor.LanguageDefinitions;

class ExpressionTest {
	/*
	
	
	
	 */
	
	@Test
	void testParse1() throws InvalidExpression {
		String lineTest = "testRegexp($line, \"^[0-9][0-9]\")";
		List<Expression> statements = new ArrayList<Expression>();
		statements.add(new Expression("hadMember($classe  ,  $pacote)"));
		

		Map<String, Expression> tokens = new HashMap<>();
		tokens.put("t1", new Expression("$line.match(\"\\[(\\w*\\.)+\\w*\\]\")"));
		tokens.put("t2", new Expression("$t1.match(\"(.\\w*])$\")"));
		tokens.put("classe", new Expression("$t2.replace(\".\"  ,  \"\").replace(\"]\"  ,  \"\")"));
		tokens.put("pacote", new Expression("$t1.replace(\"[\"  ,  \"\").replace($classe  ,  \"\").replace(\".]\" , \"\")"));
		
		LanguageDefinitions ld = new LanguageDefinitions();
		ld.setLineTest(new BooleanExpression(lineTest));
		ld.setTokens(tokens);
		ld.setStatements(statements);
		
		String line = "04:03:25,723 INFO  [br.mil.mar.dsm.sinais.controlador.InspecaoAction] (default task-36) - [Aberta e não iniciada, área de Exames, Juntas de Saúde Restituídas, Juntas de Saúde Encaminhadas, Auditoria, AuditoriaNova, Revisão Ex officio, Em Revisão Ex officio, Declínio de Competência, Concluídas pela Auditoria, Concluídas pelo AMP, Impressos, Revisadas, NIP: 17130492]";
		ld.getTokens().put("line", new StringExpression(line));
		
		assertEquals("true", new BooleanExpression(lineTest).parse(tokens, line));
		for (Expression expression : statements) {
			assertEquals("hadMember(InspecaoAction, br.mil.mar.dsm.sinais.controlador)",expression.parse(tokens, line));
		}
	}
	
	@Test
	void testParse2() throws InvalidExpression {
		String lineTest = "testRegexp($line,\"^[0-9][0-9]\")";
		List<Expression> statements = new ArrayList<Expression>();
		statements.add(new Expression("testRegexp($line  , \"gravado\") ? wasAssociatedWith($classe, \"io\")"));
		

		Map<String, Expression> tokens = new HashMap<>();
		tokens.put("t1", new Expression("$line.match(\"\\[(\\w*\\.)+\\w*\\]\")"));
		tokens.put("t2", new Expression("$t1.match(\"(.\\w*])$\")"));
		tokens.put("classe", new Expression("$t2.replace(\".\",\"\").replace(\"]\",\"\")"));
		tokens.put("pacote", new Expression("$t1.replace(\"[\",\"\").replace($classe,\"\").replace(\".]\",\"\")"));
		
		LanguageDefinitions ld = new LanguageDefinitions();
		ld.setLineTest(new BooleanExpression(lineTest));
		ld.setTokens(tokens);
		ld.setStatements(statements);
		
		String line = "06:48:44,134 INFO  [br.mil.mar.dsm.sinais.dao.LogEventoDAOImpl] (default task-49) - [CONEXAO-ID]: 5112995. [EVENTO]: 052544. [IS]: 616621. [OPERAÇÃO]: Exame Hematologia gravado com sucesso.";
		ld.getTokens().put("line", new StringExpression(line));
		
		assertEquals("true", new BooleanExpression(lineTest).parse(tokens, line));
		for (Expression expression : statements) {
			assertEquals("wasAssociatedWith(LogEventoDAOImpl, io)",expression.parse(tokens, line));
		}
	}
	
	@Test
	void testParse3() throws InvalidExpression {
		String lineTest = "testRegexp($line,\"^[0-9][0-9]\")";
		List<Expression> statements = new ArrayList<Expression>();
		statements.add(new Expression("testRegexp($line,\"InspecaoAction(.*)\\[Aberta e não iniciada\")? wasAssociatedWith($classe,\"io\")"));
		

		Map<String, Expression> tokens = new HashMap<>();
		tokens.put("t1", new Expression("$line.match(\"\\[(\\w*\\.)+\\w*\\]\")"));
		tokens.put("t2", new Expression("$t1.match(\"(.\\w*])$\")"));
		tokens.put("classe", new Expression("$t2.replace(\".\",\"\").replace(\"]\",\"\")"));
		tokens.put("pacote", new Expression("$t1.replace(\"[\",\"\").replace($classe,\"\").replace(\".]\",\"\")"));
		
		LanguageDefinitions ld = new LanguageDefinitions();
		ld.setLineTest(new BooleanExpression(lineTest));
		ld.setTokens(tokens);
		ld.setStatements(statements);
		
		String line = "09:17:46,487 INFO  [br.mil.mar.dsm.sinais.controlador.InspecaoAction] (default task-77) - [Aberta e não iniciada, área de Exames, Juntas de Saúde Restituídas, Juntas de Saúde Encaminhadas, Auditoria, AuditoriaNova, Revisão Ex officio, Em Revisão Ex officio, Declínio de Competência, Concluídas pela Auditoria, Concluídas pelo AMP, Impressos, Revisadas, Inspeção nº: 592321]";
		ld.getTokens().put("line", new StringExpression(line));
		
		assertEquals("true", new BooleanExpression(lineTest).parse(tokens, line));
		for (Expression expression : statements) {
			assertEquals("wasAssociatedWith(InspecaoAction, io)",expression.parse(tokens, line));
		}
	}
	
	@Test
	void testParse4() throws InvalidExpression {
		String lineTest = "testRegexp($line,\"^[0-9][0-9]\")";
		List<Expression> statements = new ArrayList<Expression>();
		statements.add(new Expression("$line.contains(\"/rs/\")? wasAssociatedWith($classe, \"comm\")"));
		

		Map<String, Expression> tokens = new HashMap<>();
		tokens.put("t1", new Expression("$line.match(\"\\[(\\w*\\.)+\\w*\\]\")"));
		tokens.put("t2", new Expression("$t1.match(\"(.\\w*])$\")"));
		tokens.put("classe", new Expression("$t2.replace(\".\",\"\").replace(\"]\",\"\")"));
		tokens.put("pacote", new Expression("$t1.replace(\"[\",\"\").replace($classe,\"\").replace(\".]\",\"\")"));
		
		LanguageDefinitions ld = new LanguageDefinitions();
		ld.setLineTest(new BooleanExpression(lineTest));
		ld.setTokens(tokens);
		ld.setStatements(statements);
		
		String line = "09:17:54,219 INFO  [br.mil.mar.dsm.sigsaude.clients.samp.SampConsumerRS] (default task-91) - GET http://10.205.145.211/samp/rs/ciclo/existeCriEmAberto/86492799";
		ld.getTokens().put("line", new StringExpression(line));
		
		assertEquals("true", new BooleanExpression(lineTest).parse(tokens, line));
		for (Expression expression : statements) {
			assertEquals("wasAssociatedWith(SampConsumerRS, comm)",expression.parse(tokens, line));
		}
	}
	
	@Test
	void testParse5() throws InvalidExpression {
		String lineTest = "testRegexp($line,\"^[0-9][0-9]\")";
		List<Expression> statements = new ArrayList<Expression>();
		statements.add(new Expression("$line.contains(\"ReportHandlerAction\")?wasAssociatedWith($classe, \"cpu\")"));
		

		Map<String, Expression> tokens = new HashMap<>();
		tokens.put("t1", new Expression("$line.match(\"\\[(\\w*\\.)+\\w*\\]\")"));
		tokens.put("t2", new Expression("$t1.match(\"(.\\w*])$\")"));
		tokens.put("classe", new Expression("$t2.replace(\".\",\"\").replace(\"]\",\"\")"));
		tokens.put("pacote", new Expression("$t1.replace(\"[\",\"\").replace($classe,\"\").replace(\".]\",\"\")"));
		
		LanguageDefinitions ld = new LanguageDefinitions();
		ld.setLineTest(new BooleanExpression(lineTest));
		ld.setTokens(tokens);
		ld.setStatements(statements);
		
		String line = "06:31:29,958 INFO  [br.mil.mar.dsm.sinais.controlador.ReportHandlerAction] (default task-109) - Inicio Impressao TIS da IS:513648";
		ld.getTokens().put("line", new StringExpression(line));
		
		assertEquals("true", new BooleanExpression(lineTest).parse(tokens, line));
		for (Expression expression : statements) {
			assertEquals("wasAssociatedWith(ReportHandlerAction, cpu)",expression.parse(tokens, line));
		}
	}
	
	@Test
	void testParse6() throws InvalidExpression {
		String lineTest = "testRegexp($line,\"^[0-9][0-9]\")";
		List<Expression> statements = new ArrayList<Expression>();
		statements.add(new Expression("$line.contains(\"[CONEXAO-ID]\")? actedOnBehalfOf($classe, \"usuario\")"));
		

		Map<String, Expression> tokens = new HashMap<>();
		tokens.put("t1", new Expression("$line.match(\"\\[(\\w*\\.)+\\w*\\]\")"));
		tokens.put("t2", new Expression("$t1.match(\"(.\\w*])$\")"));
		tokens.put("classe", new Expression("$t2.replace(\".\",\"\").replace(\"]\",\"\")"));
		tokens.put("pacote", new Expression("$t1.replace(\"[\",\"\").replace($classe,\"\").replace(\".]\",\"\")"));
		
		LanguageDefinitions ld = new LanguageDefinitions();
		ld.setLineTest(new BooleanExpression(lineTest));
		ld.setTokens(tokens);
		ld.setStatements(statements);
		
		String line = "06:32:17,262 INFO  [br.mil.mar.dsm.sinais.dao.LogEventoDAOImpl] (default task-104) - [CONEXAO-ID]: 5112990. [EVENTO]: 051700. [IS]: 513648. [OPERAÇÃO]: Registro da impressão do TIS para a inspecao - 513648 efetuado.";
		ld.getTokens().put("line", new StringExpression(line));
		
		assertEquals("true", new BooleanExpression(lineTest).parse(tokens, line));
		for (Expression expression : statements) {
			assertEquals("actedOnBehalfOf(LogEventoDAOImpl, usuario)",expression.parse(tokens, line));
		}
	}

}
