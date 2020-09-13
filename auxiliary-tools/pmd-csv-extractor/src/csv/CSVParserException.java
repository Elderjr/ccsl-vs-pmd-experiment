package csv;

public class CSVParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public CSVParserException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return this.message;
	}
}
