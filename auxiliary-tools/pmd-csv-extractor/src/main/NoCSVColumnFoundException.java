package main;

public class NoCSVColumnFoundException extends Exception {


	private static final long serialVersionUID = 1L;
	private String column;
	
	public NoCSVColumnFoundException(String column) {
		this.column = column;
	}
	
	@Override
	public String getMessage(){
		return "A coluna " + column + "vnao foi encontrado no arquivo csv";
	}
}
