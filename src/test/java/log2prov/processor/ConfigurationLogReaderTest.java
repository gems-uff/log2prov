package log2prov.processor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import log2prov.readers.ConfigurationFileReader;

class ConfigurationLogReaderTest {

	@Test
	void testReadFile() {
		try {
			ConfigurationFileReader logReader = new ConfigurationFileReader("etc/analysis1.conf");
			logReader.readFile();
			assertNotNull(logReader.getDefinitions());
			
			assertEquals("testRegexp($line, \"^[0-9][0-9]\")",
					logReader.getDefinitions().getLineTest().getStringExpression());
			
			assertEquals("$line.match(\"\\[(\\w*\\.)+\\w*\\]\")",
					logReader.getDefinitions().getTokens().get("t1").getStringExpression());
			
			assertEquals("$t1.match(\"(.\\w*])$\")",
					logReader.getDefinitions().getTokens().get("t2").getStringExpression());
			
			assertEquals("$t2.replace(\".\",\"\").replace(\"]\",\"\")",
					logReader.getDefinitions().getTokens().get("classe").getStringExpression());
			
			assertEquals("$t1.replace(\"[\",\"\").replace($t2,\"\")",
					logReader.getDefinitions().getTokens().get("pacote").getStringExpression());
			

			assertEquals("\"io\"", logReader.getDefinitions().getTokens().get("io").getStringExpression());
			assertEquals("\"commIn\"", logReader.getDefinitions().getTokens().get("commIn").getStringExpression());
			assertEquals("\"commOut\"", logReader.getDefinitions().getTokens().get("commOut").getStringExpression());
			assertEquals("\"cpu\"", logReader.getDefinitions().getTokens().get("cpu").getStringExpression());

			assertEquals(8, logReader.getDefinitions().getStatements().size());
			assertEquals(3, logReader.getDefinitions().getAgents().size());
			assertEquals(6, logReader.getDefinitions().getActivities().size());
			assertEquals(2, logReader.getDefinitions().getEntities().size());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
