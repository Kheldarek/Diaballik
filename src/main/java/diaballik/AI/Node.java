package diaballik.AI;

import diaballik.logic.board.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psend on 23.05.2016.
 */
public class Node
{
	public Move move;
	public int grade;
	public List<Node> children;
	public Node(Coordinate x, Coordinate y)
	{
		move.from = x;
		move.to = y;
		children = new ArrayList<>();
	}
	public Node(Move mov)
	{
		move=mov;
		children = new ArrayList<>();
	}

	public Node(){children = new ArrayList<>();}
}
