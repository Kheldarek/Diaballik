package diaballik.gameLogic;

public class Coordinate {
	private int rowIndex, columnIndex;

	public Coordinate(int rowIndex, int columnIndex) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (columnIndex != other.columnIndex || rowIndex != other.rowIndex)
			return false;
		return true;
	}
}
