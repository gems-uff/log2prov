package log2prov.language.expressions;

import java.util.Map;
import java.util.regex.Pattern;

import log2prov.exception.InvalidExpression;

public interface ExpressionInterface {

	public static final Pattern VAR_PATTERN = Pattern.compile("^\\w+$");
	public static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+");
	public static final Pattern ATTRIBUTION_PATTERN = Pattern.compile("^\\w+(\\s)*=(\\s)*");
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String NULL = "-";
	public static final String ELSE_SEPARATOR = ":";
	public static final String CONCAT_SEPARATOR = "+";
	public static final String VAR_SEPARATOR = ",";
	public static final String AND_SEPARATOR = "&&";
	public static final String OR_SEPARATOR = "||";
	public static final String CONDITION_SEPARATOR = "?";
	public static final int FALSE_TYPE = -2;
	public static final int TRUE_TYPE = -1;
	public static final int EMPTY = -1;
	public static final int HEADER_LINE = 0;
	public static final int HEADER_TOKENS = 1;
	public static final int HEADER_AGENTS = 2;
	public static final int HEADER_ENTITIES = 3;
	public static final int HEADER_ACTIVITIES = 4;
	public static final int HEADER_STATEMENTS = 5;
	public static final int STATEMENT = 6;
	public static final int LITERAL = 7;
	public static final int VAR = 8;
	public static final int ACCESS_VAR = 9;
	public static final int VAR_SET = 10;
	public static final int ATTRIBUTION = 11;
	public static final int BOOLEAN = 12;
	public static final int IF_THEN_ELSE = 13;
	public static final int TEST_REGEXP = 14;
	public static final int MATCH = 15;
	public static final int CONTAINS = 16;
	public static final int SUBSTRING = 17;
	public static final int REPLACE = 18;
	public static final int QUOTED_STRING = 19;
	public static final int NUMBER = 20;
	public static final int CONCATENATION = 21;

	public String getStringExpression();

	public String parse(Map<String, Expression> tokens, String line) throws InvalidExpression;

}
