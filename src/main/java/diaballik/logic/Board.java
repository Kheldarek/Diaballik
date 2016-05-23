package diaballik.logic;

import java.util.Arrays;

import diaballik.logic.board.Coordinate;
import diaballik.logic.board.Field;

public class Board {
	private Field[][] board;
	
	public Board() {
		createEmptyBoard();
		setPiecesOnBoard();
		setBallsOnBoard();
	}

	public Board(Board x)
	{
		this.board = new Field[7][7];

		for (int i = 0;i<7;i++)
		{
			for(int j=0; j<7; j++)
			{
				this.board[i][j] = x.board[i][j];
			}
		}
	}

	private void createEmptyBoard() {
		board = new Field[7][7];
		for(int i = 0; i < 7; i++) {
			Arrays.fill(board[i], Field.EMPTY);
		}
		
	}

	private void setPiecesOnBoard() {
		for(int column = 0; column < 7; column++) {
			board[0][column] = Field.PLAYER_1_PIECE;
			board[6][column] = Field.PLAYER_2_PIECE;
		}
	}
	
	private void setBallsOnBoard() {
		board[0][3] = Field.PLAYER_1_BALL;
		board[6][3] = Field.PLAYER_2_BALL;
	}
	
	public void actualize(Coordinate from, Coordinate to) {
		Field newToValue = board[from.getRow()][from.getColumn()];
		Field newFromValue = board[to.getRow()][to.getColumn()];
		board[from.getRow()][from.getColumn()] = newFromValue;
		board[to.getRow()][to.getColumn()] = newToValue;
	}
	
	public Field getField(Coordinate position) {
		return board[position.getRow()][position.getColumn()];
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				if(board[i][j] == Field.EMPTY) {
					result.append("     ");
				}
				result.append(board[i][j]);
				result.append(" ");
				if(board[i][j] == Field.EMPTY) {
					result.append("    ");
				}
			}
			result.append("\n");
		}
		return result.toString();
	}
}
