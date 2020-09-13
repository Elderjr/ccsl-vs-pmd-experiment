package csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CSVReader {

	private static CSVLine readCSVLine(int index, String line, List<CSVColumn> csvColumns) throws CSVParserException {
		Map<String, String> contentByColumn = new HashMap<>();
		Iterator<CSVColumn> csvColumnsIterator = csvColumns.iterator();
		CSVColumn currentColumn = null;
		char c;
		boolean newColumn = true; // flag que indica que uma nova coluna está sendo inspecinonada
		int startColumnValueIndex = 0;
		for (int i = 0; i < line.length(); i++) {
			c = line.charAt(i);
			if (newColumn) {
				if (!csvColumnsIterator.hasNext()) {
					throw new CSVParserException(
							"Erro na linha " + (index + 1) + " do arquivo csv: nao ha mais colunas");
				}
				currentColumn = csvColumnsIterator.next();
				startColumnValueIndex = i;
				if (currentColumn.hasQuotationMark()) {
					startColumnValueIndex++;
				}
				newColumn = false;
			} else if (c == ',' && currentColumn.hasQuotationMark() && line.charAt(i - 1) == '"') {
				contentByColumn.put(currentColumn.getName(), line.substring(startColumnValueIndex, i - 1));
				newColumn = true;
			} else if (c == ',' && !currentColumn.hasQuotationMark()) {
				contentByColumn.put(currentColumn.getName(), line.substring(startColumnValueIndex, i));
				newColumn = true;
			} else if (i == line.length() - 1 && currentColumn.hasQuotationMark()) {
				contentByColumn.put(currentColumn.getName(), line.substring(startColumnValueIndex, i));
			} else if (i == line.length() - 1 && !currentColumn.hasQuotationMark()) {
				contentByColumn.put(currentColumn.getName(), line.substring(startColumnValueIndex, i + 1));
			}
		}
		return new CSVLine(contentByColumn);
	}

	private static List<CSVColumn> readCSVColumns(String csvColumnsLine) {
		String csvColumnsStr[] = csvColumnsLine.split(",");
		List<CSVColumn> csvColumns = new ArrayList<>();
		for (int i = 0; i < csvColumnsStr.length; i++) {
			if (csvColumnsStr[i].startsWith("\"")) {
				csvColumns.add(new CSVColumn(csvColumnsStr[i].replaceAll("\"", ""), true));
			} else {
				csvColumns.add(new CSVColumn(csvColumnsStr[i], false));
			}
		}

		return csvColumns;
	}

	public static CSVObject readCSVFile(String filePath) throws IOException, CSVParserException {
		File file = new File(filePath);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		List<CSVColumn> csvColumns = readCSVColumns(reader.readLine());
		List<CSVLine> values = new ArrayList<>();
		int index = 0;
		while (reader.ready()) {
			values.add(readCSVLine(index, reader.readLine(), csvColumns));
			index++;
		}
		reader.close();
		return new CSVObject(csvColumns, values);
	}
}
