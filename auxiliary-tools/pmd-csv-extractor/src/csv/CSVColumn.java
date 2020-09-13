package csv;

public class CSVColumn {

	private String name;
	private boolean hasQuotationMark;
	
	CSVColumn(String name, boolean hasQuotationMark) {
		this.name = name;
		this.hasQuotationMark = hasQuotationMark;
	}

	public String getName() {
		return name;
	}

	boolean hasQuotationMark() {
		return hasQuotationMark;
	}
	
	
}
