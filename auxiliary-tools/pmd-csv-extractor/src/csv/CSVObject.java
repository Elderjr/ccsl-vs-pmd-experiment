package csv;

import java.util.List;


public class CSVObject {

	private List<CSVColumn> columns;
	private List<CSVLine> values;
	
	public CSVObject(List<CSVColumn> columns, List<CSVLine> values) {
		this.columns = columns;
		this.values = values;
	}

	public List<CSVColumn> getColumns() {
		return columns;
	}

	public List<CSVLine> getValues() {
		return values;
	}

}
