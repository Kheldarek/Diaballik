package diaballik.logic.board.parser;

import diaballik.logic.board.Coordinate;

public class CoordinateParser {
	private static final int SMALL_A_LETTER = 97;
	public static Coordinate parse(String coordinate) throws IncorrectInputException {
		if(coordinate.length() != 2) {
			throw new IncorrectInputException();
		}
		return new Coordinate(parseRow(coordinate), parseColumn(coordinate));
	}
	
	private static int parseRow(String coordinate) throws IncorrectInputException {
		try {
			return Integer.parseInt("" + coordinate.charAt(1)) - 1;
		} 
		catch(NumberFormatException nfe) {
			throw new IncorrectInputException();
		}
	}
	
	private static int parseColumn(String coordinate) throws IncorrectInputException {
		int row = ((int)coordinate.charAt(0)) - SMALL_A_LETTER;
		if(row < 0 || row > 6) {
			throw new IncorrectInputException();
		}
		return row;
	}
}
