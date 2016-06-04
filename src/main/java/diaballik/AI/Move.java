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
	public Move(Coordinate x, Coordinate y,MovementType type)
	{
		from = x;
		to = y;
		this.type = type;

	}

	public  Move(){};
}
