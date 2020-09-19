package output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import rule.RuleViolation;

public class DiffWriter {

	
	public static void writeDiffFile(String dir, RuleViolation ccslViolation, RuleViolation pmdViolation) throws IOException {
		File diffFile = new File(dir.replace("\\", "/") + "/" + ccslViolation.getRuleName() + "-diff.txt");
		List<String> exclusiveCcslViolations = ccslViolation.diff(pmdViolation);
		List<String> exclusivePmdViolations = pmdViolation.diff(ccslViolation);
		BufferedWriter writter = new BufferedWriter(new FileWriter(diffFile));
		writter.append("CCSL Violations count: " + ccslViolation.getViolations().size()+ "\n");
		writter.append("CCSL File count: " + ccslViolation.getTotalFiles() + "\n");
		writter.append("CCSL Violations that PMD does not have reported:\n");
		for(String v : exclusiveCcslViolations) {
			writter.append(v + "\n");
		}
		writter.append("Total: " + exclusiveCcslViolations.size() +"\n");
		writter.append("PMD Violations count: " + pmdViolation.getViolations().size()+ "\n");
		writter.append("PMD File count: " + pmdViolation.getTotalFiles() + "\n");
		writter.append("PMD Violations that CCSL does not have reported:\n");
		for(String v : exclusivePmdViolations) {
			writter.append(v + "\n");
		}
		writter.append("Total: " + exclusivePmdViolations.size());
		writter.close();
	}

	public static void writeDiffFiles(String dir, Map<String, RuleViolation> ccsl, Map<String, RuleViolation> pmd) throws IOException {
		String ruleName;
		RuleViolation ccslRuleViolation;
		RuleViolation pmdRuleViolation;
		for(Entry<String, RuleViolation> ccslEntry : ccsl.entrySet()) {
			ruleName = ccslEntry.getKey();
			ccslRuleViolation = ccslEntry.getValue();
			pmdRuleViolation = pmd.get(ruleName);
			if(pmdRuleViolation != null) {
				System.out.println("Writting diff file: " + ccslRuleViolation.getRuleName());
				writeDiffFile(dir, ccslRuleViolation, pmdRuleViolation);
			}
		}
	}
}
