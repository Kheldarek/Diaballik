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
	int depth;


	public AiPlayer(PlayerType p)
	{
		player = p;
		moves = new ArrayList<>();
		rulebook = new Rulebook();
		piecesPositions = new ArrayList<>();
		allPosibleCoord = new ArrayList<>();
		generator = new Random();
		depth = 6;
		final int WIN = 2000;
		final int LOSS = -2000;

		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 7; j++)
			{
				allPosibleCoord.add(new Coordinate(i, j));
			}

		}
	}

	private Coordinate[] getPlayerPiecesPositions(Board board, PlayerType player)
	{
		Coordinate[] tmpPieceList = new Coordinate[7];
		int i = 0;
		if (player == PlayerType.PLAYER_2)
		{
			for (Coordinate x : allPosibleCoord)
			{
				if (board.getField(x) == Field.PLAYER_2_PIECE)
				{
					tmpPieceList[i] = x;
					i++;
				}
				if (board.getField(x) == Field.PLAYER_2_BALL)
					tmpPieceList[6] = x;
			}
		} else
		{
			for (Coordinate x : allPosibleCoord)
			{
				if (board.getField(x) == Field.PLAYER_1_PIECE)
				{
					tmpPieceList[i] = x;
					i++;
				}
				if (board.getField(x) == Field.PLAYER_1_BALL)
					tmpPieceList[6] = x;
			}
		}
		return tmpPieceList;
	}

	public List<Move> Play()
	{

		gameTree = new Tree();
		gameTree.root = new Node();
		GenerateGameTree(gameTree.root, new Board(currentBoard), getPlayerPiecesPositions(currentBoard, player), depth, player);
		gameTree.root.grade = MinMax(gameTree.root,new Board(currentBoard), depth * 3, false);
		return Move();
	}

	public void GenerateGameTree(Node node, Board board, Coordinate[] pieces, int depth, PlayerType currentPlayer)
	{
		if (depth <= 0) return;

		for (Move move : generatePosibleMoves(board, pieces))
		{
			node.children.add(new Node(move));
		}
		depth--;
		if (depth % 3 == 0)
			currentPlayer = ChangePlayer(currentPlayer);
		for (Node next : node.children)
		{
			Board newBoard = new Board(board);
			if (node.move != null)
				newBoard.actualize(node.move.from, node.move.to);
			GenerateGameTree(next, new Board(newBoard), getPlayerPiecesPositions(newBoard, currentPlayer), depth, currentPlayer);

		}
	}

	PlayerType ChangePlayer(PlayerType p)
	{
		if (p == PlayerType.PLAYER_1)
			return PlayerType.PLAYER_2;
		else
			return PlayerType.PLAYER_1;

	}

	public List<Move> generatePosibleMoves(Board board, Coordinate[] pieces)
	{
		List<Move> pMoves = new ArrayList<>();
		for (int i = 0; i < 6; i++)
		{
			for (int j = -1; j < 2; j = j + 2)
			{
				if (pieces[i].changeColumn(j).getColumn() > 0 && pieces[i].changeColumn(j).getColumn() < 7)
				{
					if (rulebook.getMovementType(board, pieces[i], pieces[i].changeColumn(j), player) == MovementType.PIECE_MOVEMENT)
					{
						pMoves.add(new Move(pieces[i], pieces[i].changeColumn(j)));
					}
				}

				if (pieces[i].changeRow(j).getRow() > 0 && pieces[i].changeRow(j).getRow() < 7)
				{
					if (rulebook.getMovementType(board, pieces[i], pieces[i].changeRow(j), player) == MovementType.PIECE_MOVEMENT)
					{
						pMoves.add(new Move(pieces[i], pieces[i].changeRow(j)));
					}
				}


			}

			if (rulebook.getMovementType(board, pieces[6], pieces[i], player) == MovementType.BALL_THROW)
			{
				pMoves.add(new Move(pieces[6], pieces[i]));
			}
		}

		return pMoves;

	}

	public void AlphaBeta()
	{

	}


	public int MinMax(Node node, Board board, int depth, boolean maximizing)
	{

		if (depth == 0 || node.children.isEmpty())
		{
			Board nextBoard = new Board(board);
			nextBoard.actualize(node.move.from, node.move.to);
			return EvaluateMove(nextBoard,node.move, maximizing);
		}
		if (depth % 3 == 0)
		{
			maximizing = !maximizing;
		}

		if (maximizing)
		{
			int bestValue = -999999;
			for (Node tmpNode : node.children)
			{
				Board nextBoard = new Board(board);
				nextBoard.actualize(tmpNode.move.from, tmpNode.move.to);
				tmpNode.grade = MinMax(tmpNode, nextBoard, --depth, maximizing);
				if (tmpNode.grade > bestValue) bestValue = tmpNode.grade;

			}
			return bestValue;
		} else
		{
			int bestValue = 999999;
			for (Node tmpNode : node.children)
			{
				Board nextBoard = new Board(board);
				nextBoard.actualize(tmpNode.move.from, tmpNode.move.to);
				tmpNode.grade = MinMax(tmpNode, nextBoard, --depth, maximizing);
				//bestValue = gameTree.FindWorstMove(tmpNode).grade;
				if(tmpNode.grade<bestValue) bestValue = tmpNode.grade;
				return bestValue;
			}
			return bestValue;
		}
		//return 0;
	}

	public List<Move> Move()
	{
		moves.clear();
		Node move1, move2, move3;
		move1 = gameTree.FindBestMove(gameTree.root);
		move2 = gameTree.FindBestMove(move1);
		move3 = gameTree.FindBestMove(move2);
		moves.add(move1.move);
		moves.add(move2.move);
		moves.add(move3.move);
		return moves;
	}

	public int EvaluateMove(Board board, Move move, boolean maximizing)
	{


		return 7 - move.to.getRow();


	}


}
