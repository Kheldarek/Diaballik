package diaballik.AI;

import diaballik.logic.Board;
import diaballik.logic.PlayerType;
import diaballik.logic.Rulebook;
import diaballik.logic.board.Coordinate;
import diaballik.logic.board.Field;
import diaballik.logic.board.MovementType;

import java.util.ArrayList;
import java.util.Collections;
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
	public int depth;
	final int WIN = 10000;
	final int LOSS = -10000;
	List<Move> last3;


	public AiPlayer(PlayerType p)
	{
		player = p;
		moves = new ArrayList<>();
		last3 = new ArrayList<>();
		rulebook = new Rulebook();
		piecesPositions = new ArrayList<>();
		allPosibleCoord = new ArrayList<>();
		generator = new Random();
		depth = 1;


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
		gameTree.root.parent = gameTree.root;
		gameTree.root.player = player;
		GenerateGameTree(gameTree.root, new Board(currentBoard), getPlayerPiecesPositions(currentBoard, player), depth * 3, player);
		gameTree.root.grade = MinMax(gameTree.root, new Board(currentBoard), depth * 3, false);
		return Move();
	}

	public void GenerateGameTree(Node node, Board board, Coordinate[] pieces, int depth, PlayerType currentPlayer)
	{
		if (depth <= 0) return;
		Node tmp = new Node();
		if (node.player != currentPlayer)
		{
			tmp.moves = 0;
			tmp.passed = false;
		} else
		{
			tmp.moves = node.moves;
			tmp.passed = node.passed;
		}
		for (Move move : generatePossibleMoves(tmp, board, pieces, currentPlayer))
		{

			if (node.parent.player == currentPlayer)
			{
				if (move.type == MovementType.BALL_THROW || node.passed)
					node.children.add(new Node(move, true, node, node.moves, currentPlayer));
				else
					node.children.add(new Node(move, false, node, node.moves + 1, currentPlayer));
			} else if (move.type == MovementType.BALL_THROW)
				node.children.add(new Node(move, true, node, node.moves, currentPlayer));
			else
				node.children.add(new Node(move, false, node, node.moves + 1, currentPlayer));

		}
		Collections.shuffle(node.children, new Random(System.currentTimeMillis()));
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

	public List<Move> generatePossibleMoves(Node node, Board board, Coordinate[] pieces, PlayerType p)
	{
		List<Move> pMoves = new ArrayList<>();
		for (int i = 0; i < 7; i++)
		{
			if (node.moves < 2)
				for (int j = -1; j < 2; j = j + 2)
				{
					if (pieces[i].changeColumn(j).getColumn() > 0 && pieces[i].changeColumn(j).getColumn() < 7)
					{
						if (rulebook.getMovementType(board, pieces[i], pieces[i].changeColumn(j), p) == MovementType.PIECE_MOVEMENT)
						{
							Move tmp = new Move(pieces[i], pieces[i].changeColumn(j), MovementType.PIECE_MOVEMENT);
							pMoves.add(tmp);
						}
					}

					if (pieces[i].changeRow(j).getRow() >= 0 && pieces[i].changeRow(j).getRow() < 7)
					{
						if (rulebook.getMovementType(board, pieces[i], pieces[i].changeRow(j), p) == MovementType.PIECE_MOVEMENT)
						{
							Move tmp = new Move(pieces[i], pieces[i].changeRow(j), MovementType.PIECE_MOVEMENT);
							pMoves.add(tmp);

						}
					}


				}
			if (!node.passed)
				if (rulebook.getMovementType(board, pieces[6], pieces[i], p) == MovementType.BALL_THROW)
				{
					Move tmp = new Move(pieces[6], pieces[i], MovementType.BALL_THROW);
					pMoves.add(tmp);
				}
		}
		if (node.parent != null && node.parent.player == p)
		{
			pMoves.remove(node.parent.move);
			pMoves.remove(new Move(node.parent.move.to, node.parent.move.from, node.parent.move.type));
			if (node.parent.parent != null && node.parent.parent.player == p)
			{
				pMoves.remove(node.parent.parent.move);
				pMoves.remove(new Move(node.parent.parent.move.to,
						node.parent.parent.move.from, node.parent.parent.move.type));
			}
		}
		pMoves.removeAll(last3);

		return pMoves;

	}

	PlayerType ChangePlayer(PlayerType p)
	{
		if (p == PlayerType.PLAYER_1)
			return PlayerType.PLAYER_2;
		else
			return PlayerType.PLAYER_1;

	}


	public int MinMax(Node node, Board board, int depth, boolean maximizing)
	{

		if (depth == 0 || node.children.isEmpty())
		{
			return maximizing ? EvaluateMove(board, node, maximizing) : (-EvaluateMove(board, node, maximizing));
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
				tmpNode.grade = MinMax(tmpNode, nextBoard, depth - 1, maximizing);
				if (tmpNode.grade > bestValue) bestValue = tmpNode.grade;

			}
			return bestValue;

		}
		else
		{
			int bestValue = 999999;
			for (Node tmpNode : node.children)
			{
				Board nextBoard = new Board(board);
				nextBoard.actualize(tmpNode.move.from, tmpNode.move.to);
				tmpNode.grade = MinMax(tmpNode, nextBoard, --depth, maximizing);
				//bestValue = gameTree.FindWorstMove(tmpNode).grade;
				if (tmpNode.grade < bestValue) bestValue = tmpNode.grade;

			}
			return bestValue;
		}
		//return bestValue;

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
		last3 = new ArrayList<>(moves);
		try
		{
			last3.forEach(Move::revert);
		}
		catch(Exception e)
		{

		}
		return moves;
	}

	public int EvaluateMove(Board board, Node move, boolean maximizing)
	{
		int tmpGrade = 0;
		int p1Start = 0;
		int p2Start = 6;
		PlayerType currentPlayer = move.player;
		board.actualize(move.move.from, move.move.to);
		if (rulebook.checkGameIsOver(currentPlayer, board) && currentPlayer == player)
			return WIN;
		else if (rulebook.checkGameIsOver(currentPlayer, board) && currentPlayer != player)
			return LOSS;
		board.actualize(move.move.to, move.move.from);

		if (move.player == player)
		{
			if (move.move.to.getRow() == p1Start)
				tmpGrade += 200;
			else
				tmpGrade += ((7 - move.move.to.getRow()) * 10);
		} else
		{
			if (move.move.to.getRow() == p2Start)
				tmpGrade += 200;
			else
				tmpGrade += ((move.move.to.getRow()) * 10);
		}

		return tmpGrade;
	}


}
