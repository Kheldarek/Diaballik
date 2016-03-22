package diaballik.gameLogic;

import java.util.ArrayList;

public class Field {
	private Coordinate position;
	private ArrayList<Coordinate> neighbours;
	private FieldContent state;
	
	public Field(Coordinate position, ArrayList<Coordinate> neighbours, FieldContent state) {
		this.position = position;
		this.neighbours = neighbours;
		this.state = state;
	}

	public FieldContent getState() {
		return state;
	}

	public void setNewState(FieldContent state) {
		this.state = state;
	}

	public Coordinate getPosition() {
		return position;
	}

	public ArrayList<Coordinate> getNeighbours() {
		return neighbours;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Field))
			return false;
		Field other = (Field) obj;
		if (!position.equals(other.position) || state != other.state)
			return false;
		return true;
	}
	
}
