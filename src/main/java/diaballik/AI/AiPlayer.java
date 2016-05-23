package diaballik.AI;

import diaballik.logic.Board;
import diaballik.logic.PlayerType;
import diaballik.logic.Rulebook;
import diaballik.logic.board.Coordinate;
import diaballik.logic.board.Field;
import diaballik.logic.board.MovementType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by psend on 08.05.2016.
 */
public class AiPlayer
{
    PlayerType player;
    public Board currentBoard;
    Tree gameTree;
    Rulebook rulebook;
    List<Move> moves;
	List<Coordinate> piecesPositions;
	Coordinate ballPosition;
	List<Coordinate> allPosibleCoord;
	Random generator;


    public AiPlayer(PlayerType p)
    {
        player = p;
        moves = new ArrayList<>();
		rulebook = new Rulebook();
		piecesPositions = new ArrayList<>();
		allPosibleCoord = new ArrayList<>();
		generator = new Random();
		for (int i =0; i<7;i++)
		{
			for (int j=0; j<7; j++)
			{
				allPosibleCoord.add(new Coordinate(i,j));
			}

		}
    }
	private Coordinate[] getPiecesPositions(Board board)
	{
		Coordinate[] tmpPieceList = new Coordinate[7];
		int i =0;
		for(Coordinate x : allPosibleCoord)
		{
			if(board.getField(x)==Field.PLAYER_2_PIECE)
			{
				tmpPieceList[i] = x;
				i++;
			}
			if(board.getField(x)==Field.PLAYER_2_BALL)
				tmpPieceList[6] = x;
		}
		return  tmpPieceList;
	}

    public List<Move> Play()
    {
        GenerateGameTree(new Board(currentBoard),getPiecesPositions(currentBoard));
       	gameTree.root.grade = MinMax(gameTree.root,3,true);
        return Move();
    }

	public List<Move> generatePosibleMoves(Board board,Coordinate[] pieces)
	{
		List<Move> pMoves = new ArrayList<>();
		for (int i = 0;i<6; i++)
		{
			for(int j = -1; j<2; j=j+2)
			{
				if(pieces[i].changeColumn(j).getColumn()>0&&pieces[i].changeColumn(j).getColumn()<7)
				{
					if (rulebook.getMovementType(board, pieces[i], pieces[i].changeColumn(j), player) == MovementType.PIECE_MOVEMENT)
					{
						pMoves.add(new Move(pieces[i], pieces[i].changeColumn(j)));
					}
				}
				if(pieces[i].changeRow(j).getRow()>0&&pieces[i].changeRow(j).getRow()<7)
				{
					if (rulebook.getMovementType(board, pieces[i], pieces[i].changeRow(j), player) == MovementType.PIECE_MOVEMENT)
					{
						pMoves.add(new Move(pieces[i], pieces[i].changeRow(j)));
					}
				}


			}
			if(rulebook.getMovementType(board,pieces[6],pieces[i],player)==MovementType.BALL_THROW)
			{
				pMoves.add(new Move(pieces[6],pieces[i]));
			}
		}

		return pMoves;

	}

    public void GenerateGameTree(Board board,Coordinate[] pieces)
    {
        Board b2,b3;
		gameTree = new Tree();
		gameTree.root = new Node();
		for (Move move: generatePosibleMoves(board,pieces))
		{
			gameTree.root.children.add(new Node(move));
		}
		for(Node child :gameTree.root.children)
		{
			b2=new Board(board);
			b2.actualize(child.move.from,child.move.to);
			Coordinate[] piecesAfterMove1 = getPiecesPositions(b2);
			for (Move move: generatePosibleMoves(b2,piecesAfterMove1))
			{
				child.children.add(new Node(move));
			}
			for (Node child2 : child.children)
			{
				b3 =new Board(b2);
				b3.actualize(child2.move.from,child2.move.to);
				Coordinate[] piecesAfterMove2 = getPiecesPositions(b3);
				for (Move move: generatePosibleMoves(b3,piecesAfterMove2))
				{
					child2.children.add(new Node(move));
				}
			}
		}


    }

    public void AlphaBeta()
    {

    }

    public int MinMax(Node node, int depth, boolean maximizing)
	{

		if (depth==0 || node.children.isEmpty())
			return EvaluateMove();

		if(maximizing)
		{
			int bestValue = -999999;
			for (Node tmpNode:node.children)
			{
				tmpNode.grade = MinMax(tmpNode,depth-1,true);
				if(tmpNode.grade> bestValue) bestValue=tmpNode.grade;

			}
			return bestValue;
		}
		else
		{
			int bestValue = 999999;
			for (Node tmpNode:node.children)
			{
				tmpNode.grade = MinMax(tmpNode,depth-1,true);
				bestValue = gameTree.FindWorstMove(tmpNode).grade;
				return bestValue;
			}
			return bestValue;
		}
		//return 0;
    }

    public List<Move> Move()
    {
		moves.clear();
		Node move1,move2,move3;
		move1 = gameTree.FindBestMove(gameTree.root);
		move2 = gameTree.FindBestMove(move1);
		move3 = gameTree.FindBestMove(move2);
		moves.add(move1.move);
		moves.add(move2.move);
		moves.add(move3.move);
		return moves;
    }

    public int EvaluateMove()
    {
        return generator.nextInt(1000)+1;
    }


}
