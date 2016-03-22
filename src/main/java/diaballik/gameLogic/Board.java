package diaballik.gameLogic;

public class Board {
	private Field[][] board;

	public Board(Field[][] board) {
		this.board = board;
		resetBoard();
	}
	
	public void resetBoard() {
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				if(i == 0) {
					board[i][j].setNewState(FieldContent.FIRST_PLAYER);
				}
				else if(i == 6) {
					board[i][j].setNewState(FieldContent.SECOND_PLAYER);
				}
				else {
					board[i][j].setNewState(FieldContent.EMPTY);
				}
			}
		}
		board[0][3].setNewState(FieldContent.FIRST_BALL);
		board[6][3].setNewState(FieldContent.SECOND_BALL);
	}
	
	public void actualizeBoard(Coordinate coordinates, FieldContent newState) {
		board[coordinates.getRowIndex()][coordinates.getColumnIndex()].setNewState(newState);
	}
	
	public Field getField(Coordinate coordinates) {
		return board[coordinates.getRowIndex()][coordinates.getColumnIndex()];
	}
}
