package diaballik.logic.board;

import java.util.function.Function;

public class Coordinate {
	private int row, column;
	
	public Coordinate(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getRow() {
		return row;
	}
	
	public Coordinate changeRow(int rowMovement) {
		return new Coordinate(row + rowMovement, column);
	}
	
	public Coordinate changeColumn(int columnMovement) {
		return new Coordinate(row, columnMovement + column);
	}
	
	public boolean isNeighbourOf(Coordinate otherPosition) {
		if(this.row == otherPosition.row && (this.column + 1 == otherPosition.column || this.column - 1 == otherPosition.column)) {
			return true;
		}
		if(this.column == otherPosition.column && (this.row + 1 == otherPosition.row || this.row - 1 == otherPosition.row)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Coordinate)) {
			return false;
		}
		Coordinate other = (Coordinate) object;
		if(this.row == other.row && this.column == other.column) {
			return true;
		}
		return false;		
	}

	public boolean isOnDiagonal(Coordinate to) {
		if(this.row > to.row && this.column > to.column) {
			return checkDiagonal((from) -> {return from.column >= 0;}, to, this, (from) -> {return from.changeRow(-1).changeColumn(-1);});
		}
		if(this.row > to.row && this.column < to.column) {
			return checkDiagonal((from) -> {return from.column <= 6;}, to, this, (from) -> {return from.changeRow(-1).changeColumn(1);});
		}
		if(this.row < to.row && this.column > to.column) {
			return checkDiagonal((from) -> {return from.column >= 0;}, to, this, (from) -> {return from.changeRow(1).changeColumn(-1);});
		}
		if(this.row < to.row && this.column < to.column) {
			return checkDiagonal((from) -> {return from.column <= 6;}, to, this, (from) -> {return from.changeRow(1).changeColumn(1);});
		}
		return false;
	}

	private boolean checkDiagonal(Function<Coordinate, Boolean> limiter, Coordinate to, Coordinate from, Function<Coordinate, Coordinate> incrementer) {
		while(limiter.apply(from)) {
			if(from.equals(to)) {
				return true;
			}
			from = incrementer.apply(from);
		}
		return false;
	}
}
