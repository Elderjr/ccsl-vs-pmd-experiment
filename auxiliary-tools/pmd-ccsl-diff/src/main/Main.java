package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import output.DiffWriter;
import rule.RuleReader;
import rule.RuleViolation;
import static utils.RegexContants.CCSL_FILE_NAME_PATTERN;
import static utils.RegexContants.PMD_FILE_NAME_PATTERN;

public class Main {

	private static void populatePMDAndCCSLReports(String dir, Map<String, RuleViolation> pmdReports,
			Map<String, RuleViolation> ccslReports) throws IOException {
		System.out.println("*** Getting pmd and ccsl reports ***");
		Files.walk(Paths.get(dir)).forEach(path -> {
			try {
				RuleViolation ruleViolation;
				if (path.getFileName().toString().matches(PMD_FILE_NAME_PATTERN)) {
					ruleViolation = RuleReader.extractRuleViolationsFromFile(path.toFile());
					System.out.println("PMD Rule report detected: " + ruleViolation.getRuleName());
					pmdReports.put(ruleViolation.getRuleName(), ruleViolation);
				} else if (path.getFileName().toString().matches(CCSL_FILE_NAME_PATTERN)) {
					ruleViolation = RuleReader.extractRuleViolationsFromFile(path.toFile());
					System.out.println("CCSL Rule report detected: " + ruleViolation.getRuleName());
					ccslReports.put(ruleViolation.getRuleName(), ruleViolation);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static void main(String[] args) throws IOException {
		args = new String[]{"D:\\testComparator"};
		if (args.length > 0) {
			String strDir = args[0];
			File fileDir = new File(strDir);
			if (fileDir.isDirectory()) {
				Map<String, RuleViolation> pmdReports = new HashMap<>();
				Map<String, RuleViolation> ccslReports = new HashMap<>();
				populatePMDAndCCSLReports(strDir, pmdReports, ccslReports);
				System.out.println("*** Writting diff files ***");
				DiffWriter.writeDiffFiles(strDir, ccslReports, pmdReports);
				System.out.println("*** Writting CSV File ***");
				DiffWriter.writeCSVDiffFile(strDir, ccslReports, pmdReports);
			} else {
				System.out.println(strDir + "is not a directoy");
			}
		} else {
			System.out.println("First argument should be the directory where there are CCSL and PMD reports");
		}
	}
}
