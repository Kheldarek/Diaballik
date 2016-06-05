package diaballik.AI;

import diaballik.logic.board.Coordinate;
import diaballik.logic.board.MovementType;

/**
 * Created by psend on 23.05.2016.
 */
public class Move
{
	public Coordinate from;
	public Coordinate to;
	public MovementType type;

	public Move(Coordinate x, Coordinate y)
	{
		from = x;
		to = y;

	}

	public Move(Coordinate x, Coordinate y, MovementType type)
	{
		from = x;
		to = y;
		this.type = type;

	}

	public void revert()
	{
		Coordinate tmp = new Coordinate(from.getRow(), from.getColumn());
		from = to;
		to = tmp;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Move move = (Move) o;

		if (from != null ? !from.equals(move.from) : move.from != null) return false;
		return to != null ? to.equals(move.to) : move.to == null;

	}

	@Override
	public int hashCode()
	{
		int result = from != null ? from.hashCode() : 0;
		result = 31 * result + (to != null ? to.hashCode() : 0);
		return result;
	}

	public Move()
	{
	}

	;
}
