package rule;

import static utils.RegexContants.FILE_PATH_PATTERN;
import static utils.RegexContants.REPORT_FILE_NAME_PATTERN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleReader {

	public static RuleViolation extractRuleViolationsFromFile(File file) throws Exception{
		if(!file.getName().matches(REPORT_FILE_NAME_PATTERN))
			throw new Exception("File name should be in format <RULE_Name>-<PMD or CCSL>-<*>.txt");
		String ruleName = file.getName().split("-")[0];
		List<String> violations = new ArrayList<>();
		Set<String> files = new HashSet<>();
		Pattern violationPattern = Pattern.compile(FILE_PATH_PATTERN + ":(\\d)+");
		Pattern filePattern = Pattern.compile(FILE_PATH_PATTERN);
		Matcher matcher = null;
		try (FileReader fr = new FileReader(file); BufferedReader buffer = new BufferedReader(fr)) {
			while (buffer.ready()) {
				String line = buffer.readLine();
				matcher = violationPattern.matcher(line);
				if (matcher.find()) {
					violations.add(matcher.group(0));
					matcher = filePattern.matcher(line);
					if(matcher.find()) {
						files.add(matcher.group(0));
					}
				}
			}
		}
		return new RuleViolation(ruleName, violations, files.size());
	}
}
