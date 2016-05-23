package diaballik.AI;

/**
 * Created by psend on 09.05.2016.
 */
public class Tree
{
	public Node root;

	public Node FindBestMove(Node init)
	{
		Node max = new Node();
		max.grade =-990;
		for (Node x : init.children)
		{
			if(x.grade>=max.grade)
				max=x;
		}
		return max;
	}

	public Node FindWorstMove(Node init)
	{
		Node max = new Node();
		max.grade = 999999;
		for (Node x : init.children)
		{
			if(x.grade<max.grade)
				max=x;
		}
		return max;
	}


}
