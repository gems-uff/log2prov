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
			ConfigurationFileReader logReader = new ConfigurationFileReader("etc/sample.conf");
			logReader.readFile();
			assertNotNull(logReader.getDefinitions());
			
			assertEquals("testRegexp($line, \"^([0][1-9]|[1][0-2])-([0][1-9]|[1][0-9]|[2][0-9]|[3][0-1])-([1][9][0-9]{2}|[2][0-9]{3})( ([0-1][0-9]|[2][0-3]):[0-5][0-9]:[0-5][0-9])\")",
					logReader.getDefinitions().getLineTest().getStringExpression());
			
			assertEquals("testRegexp($line, \"\\[AGENT\\]:\\s*\\w*[!\\.]\") ? $line.match(\"\\[AGENT\\]:\\s*\\w*[!\\.]\")",
					logReader.getDefinitions().getTokens().get("preAgent").getStringExpression());
			
			assertEquals("testRegexp($line, \"\\[AGENT\\]:\\s*\\w*[!\\.]\") ? $preAgent.replace(\"[AGENT]: \", \"\").replace(\"!\", \"\").replace(\".\", \"\")",
					logReader.getDefinitions().getTokens().get("ag").getStringExpression());
			
			assertEquals("testRegexp($line, \"\\[ACTIVITY\\]:\\s*\\w*[!\\.;]\") ? $line.match(\"\\[ACTIVITY\\]:\\s*\\w*[!\\.;]\")",
					logReader.getDefinitions().getTokens().get("preActivity").getStringExpression());
			
			assertEquals("testRegexp($line, \"\\[ACTIVITY\\]:\\s*\\w*[!\\.;]\") ? $preActivity.replace(\"[ACTIVITY]: \", \"\").replace(\"!\", \"\").replace(\".\", \"\").replace(\";\", \"\")",
					logReader.getDefinitions().getTokens().get("ac").getStringExpression());
			
			assertEquals("testRegexp($line, \"\\[ACTIVITY\\]:\\s*\\w*[!\\.;]$\") ? $line.match(\"\\[ACTIVITY\\]:\\s*\\w*[!\\.;]$\")",
					logReader.getDefinitions().getTokens().get("preInformer").getStringExpression());
			assertEquals("testRegexp($line, \"\\[ACTIVITY\\]:\\s*\\w*[!\\.;]$\") ? $preInformer.replace(\"[ACTIVITY]: \", \"\").replace(\"!\", \"\").replace(\".\", \"\").replace(\";\", \"\")",
					logReader.getDefinitions().getTokens().get("informer").getStringExpression());

			assertEquals(5, logReader.getDefinitions().getStatements().size());
			assertEquals(1, logReader.getDefinitions().getAgents().size());
			assertEquals(2, logReader.getDefinitions().getActivities().size());
			assertEquals(1, logReader.getDefinitions().getEntities().size());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
