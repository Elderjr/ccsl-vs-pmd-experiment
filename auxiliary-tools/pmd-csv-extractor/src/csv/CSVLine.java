package csv;


import java.util.Map;

public class CSVLine {

	private Map<String, String> contentByColumn;
	
	
	CSVLine(Map<String, String> contentByColumn) {
		this.contentByColumn = contentByColumn;
	}
	
	public String getContentOfColumn(String columnName) {
		return contentByColumn.get(columnName);
	}
}
