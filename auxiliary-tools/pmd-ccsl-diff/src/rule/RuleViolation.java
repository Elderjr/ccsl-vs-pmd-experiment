package rule;


import java.util.ArrayList;
import java.util.List;

public class RuleViolation {

	private String ruleName;
	private List<String> violations;
	private int totalFiles;
	
	RuleViolation(String ruleName, List<String> violations, int totalFiles) {
		this.ruleName = ruleName;
		this.violations = violations;
		this.totalFiles = totalFiles;
	}
	
	public String getRuleName() {
		return this.ruleName;
	}
	
	public int getTotalFiles() {
		return this.totalFiles;
	}
	
	public List<String> getViolations(){
		return this.violations;
	}
	
	public List<String> diff(RuleViolation r){
		List<String> diff = new ArrayList<>();
		List<String> rViolations = new ArrayList<>(r.violations); //a copy of the 'r' violations
		for(String v : violations) {
			int index = rViolations.indexOf(v);
			if(index >= 0) {
				rViolations.remove(index);
			} else {
				diff.add(v);
			}			
		}
		return diff;
	}
	
	
}
