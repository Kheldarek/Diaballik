package diaballik.AI;

import diaballik.logic.Board;
import diaballik.logic.PlayerType;
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
	public boolean passed;
	public List<Node> children;
	public PlayerType player;
	public Node parent;
	public int moves;
	public Node(Coordinate x, Coordinate y)
	{
		move.from = x;
		move.to = y;
		children = new ArrayList<>();
	}
	public Node(Move mov,boolean pass,Node parent,int cnt, PlayerType playerType)
	{
		move=mov;
		passed = pass;
		this.parent = parent;
		children = new ArrayList<>();
		moves = cnt;
		player = playerType;
	}

	public Node(){children = new ArrayList<>();}
}
