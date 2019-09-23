package processor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import readers.ConfigurationFileReader;

class ConfigurationLogReaderTest {

	@Test
	void testReadFile() {
		try {
			ConfigurationFileReader logReader = new ConfigurationFileReader("etc/sample1.conf");
			logReader.readFile();
			assertNotNull(logReader.getDefinitions());
			assertEquals("testRegexp($line, \"^[0-9][0-9]\")",
					logReader.getDefinitions().getLineTest().getStringExpression());
			assertEquals("$line.match(\"\\[(\\w*\\.)+\\w*\\]\")",
					logReader.getDefinitions().getTokens().get("t1").getStringExpression());
			assertEquals("$t1.match(\"(.\\w*])$\") ",
					logReader.getDefinitions().getTokens().get("t2").getStringExpression());
			assertEquals("$t2.replace(\".\",\"\").replace(\"]\",\"\")",
					logReader.getDefinitions().getTokens().get("classe").getStringExpression());
			assertEquals("$t1.replace(\"[\",\"\").replace($classe,\"\")",
					logReader.getDefinitions().getTokens().get("pacote").getStringExpression());
			assertEquals("$t1.replace(\"[\",\"\").replace($classe,\"\")",
					logReader.getDefinitions().getTokens().get("pacote").getStringExpression());

			assertEquals("\"io\"", logReader.getDefinitions().getTokens().get("io").getStringExpression());
			assertEquals("\"comm\"", logReader.getDefinitions().getTokens().get("comm").getStringExpression());
			assertEquals("\"cpu\"", logReader.getDefinitions().getTokens().get("cpu").getStringExpression());

			assertEquals(6, logReader.getDefinitions().getStatements().size());
			assertEquals(5, logReader.getDefinitions().getAgents().size());
			assertEquals(2, logReader.getDefinitions().getActivities().size());
			assertEquals(1, logReader.getDefinitions().getEntities().size());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
