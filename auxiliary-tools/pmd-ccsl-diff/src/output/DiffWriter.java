package output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

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
	
	private static String getCSVDiffLine(Optional<RuleViolation> ccslViolation, Optional<RuleViolation> pmdViolation) {
		String exclusiveCcslViolations = "";
		String exclusivePmdViolations = "";
		if(ccslViolation.isPresent() && pmdViolation.isPresent()) {
			exclusiveCcslViolations = String.join("\n", ccslViolation.get().diff(pmdViolation.get()));
			exclusivePmdViolations = String.join("\n", pmdViolation.get().diff(ccslViolation.get()));	
		} else if(ccslViolation.isPresent() && !pmdViolation.isPresent()) {
			exclusiveCcslViolations = String.join("\n", ccslViolation.get().getViolations());
			exclusivePmdViolations = "";
		} else if(!ccslViolation.isPresent() && pmdViolation.isPresent()) {
			exclusiveCcslViolations = "";
			exclusivePmdViolations = String.join("\n", pmdViolation.get().getViolations());;
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(pmdViolation.map(RuleViolation::getRuleName).orElseGet(() -> ccslViolation.get().getRuleName()) + ",")
			.append(pmdViolation.map(v -> v.getViolations().size()).orElse(0) + ",")
			.append(pmdViolation.map(RuleViolation::getTotalFiles).orElse(0) + ",")
			.append("\"" + exclusivePmdViolations + "\",")
			.append(ccslViolation.map(v -> v.getViolations().size()).orElse(0) + ",")
			.append(ccslViolation.map(RuleViolation::getTotalFiles).orElse(0) + ",")
			.append("\"" + exclusiveCcslViolations + "\"");
		return strBuilder.toString();
	}
	public static void writeCSVDiffFile(String dir, Map<String, RuleViolation> ccsl, Map<String, RuleViolation> pmd) throws IOException {
		StringBuilder strBuilder = new StringBuilder("Rule,"
				+ "#PMD Violations,"
				+ "#PMD Files,"
				+ "#PMD - CCSL,"
				+ "#CCSL Violations,"
				+ "#CCSL Files,"
				+ "#CCSL - PMD\n");
		Set<String> rules = new HashSet<>();
		rules.addAll(ccsl.keySet());
		rules.addAll(pmd.keySet());
		for(String ruleName : rules) {
			Optional<RuleViolation> ccslViolation = Optional.ofNullable(ccsl.get(ruleName));
			Optional<RuleViolation> pmdViolation = Optional.ofNullable(pmd.get(ruleName));
			String csvLine = getCSVDiffLine(ccslViolation, pmdViolation);
			strBuilder.append(csvLine + "\n");
		}
		File csvFile = new File(dir.replace("\\", "/") + "/" + "ccsl-vs-pmd" + ".csv");
		BufferedWriter writter = new BufferedWriter(new FileWriter(csvFile));
		writter.append(strBuilder.toString());
		writter.close();
	}
}
