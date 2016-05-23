package diaballik.AI;

import diaballik.logic.board.Coordinate;

/**
 * Created by psend on 23.05.2016.
 */
public class Move
{
	public Coordinate from;
	public Coordinate to;

	public Move(Coordinate x, Coordinate y)
	{
		from = x;
		to = y;

	}

	public  Move(){};
}
