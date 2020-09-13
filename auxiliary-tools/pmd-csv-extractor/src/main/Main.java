package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import csv.CSVColumn;
import csv.CSVLine;
import csv.CSVObject;
import csv.CSVParserException;
import csv.CSVReader;


public class Main {

	private static void writeRulesViolations(String outputDir, Map<String, List<String>> violationsPerRule)
			throws IOException {
		String ruleName;
		List<String> violations;
		for (Entry<String, List<String>> ruleEntry : violationsPerRule.entrySet()) {
			ruleName = ruleEntry.getKey();
			violations = ruleEntry.getValue();
			File violationFile = new File(outputDir + "\\" + ruleName + "-pmd-violations.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(violationFile));
			System.out.println("Writting file " + violationFile.getName());
			for (String v : violations) {
				writer.append(v);
				writer.newLine();
			}
			writer.close();
		}
	}

	private static Map<String, List<String>> buildViolationsPerRule(CSVObject csvObject) {
		Map<String, List<String>> violationsPerRule = new HashMap<>();
		for(CSVLine value : csvObject.getValues()) {
			String ruleName = value.getContentOfColumn("Rule");
			List<String> violations;
			if(!violationsPerRule.containsKey(ruleName)) {
				violations = new ArrayList<>();
				violationsPerRule.put(ruleName, violations);
			} else {
				violations = violationsPerRule.get(ruleName);
			}
			String file = value.getContentOfColumn("File");
			String line = value.getContentOfColumn("Line");
			String desc = value.getContentOfColumn("Description");
			String fullyViolation = file + ":" + line + ":" + desc;
			violations.add(fullyViolation);
		}
		return violationsPerRule;
	}
	
	private static void validateCSVObject(CSVObject csv) throws NoCSVColumnFoundException {
		List<String> csvColumns = csv.getColumns().stream()
				.map(column -> column.getName())
				.collect(Collectors.toList());
		if(!csvColumns.contains("File")) {
			throw new NoCSVColumnFoundException("File");
		} else if(!csvColumns.contains("Line")) {
			throw new NoCSVColumnFoundException("Line");
		} else if(!csvColumns.contains("Description")) {
			throw new NoCSVColumnFoundException("Description");
		} else if(!csvColumns.contains("Rule")) {
			throw new NoCSVColumnFoundException("Rule");
		}
	}
	public static void main(String[] args) throws IOException, CSVParserException, NoCSVColumnFoundException {
		if(args.length >= 2) {
			String csvFile = args[0]; 
			String outputDir = args[1];
			CSVObject csvObject = CSVReader.readCSVFile(csvFile);
			validateCSVObject(csvObject);
			Map<String, List<String>> violationsPerRule = buildViolationsPerRule(csvObject);
			writeRulesViolations(outputDir, violationsPerRule);	
		} else {
			System.out.println("First paremeter should be the pmd csv report file");
			System.out.println("Second paremeter should be the output folder");
		}
	}
	 
}
