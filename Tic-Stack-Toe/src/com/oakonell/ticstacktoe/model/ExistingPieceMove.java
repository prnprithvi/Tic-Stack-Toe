package com.oakonell.ticstacktoe.model;

import java.nio.ByteBuffer;
import java.util.List;

import com.oakonell.ticstacktoe.model.Board.PieceStack;

public class ExistingPieceMove extends AbstractMove {
	private final Piece exposedSourcePiece;
	private final Cell source;

	public ExistingPieceMove(Player player, Piece playedPiece,
			Piece exposedSourcePiece, Cell from, Cell target,
			Piece existingTargetPiece) {
		super(player, playedPiece, target, existingTargetPiece);
		this.source = from;
		this.exposedSourcePiece = exposedSourcePiece;
	}

	public Piece getExposedSourcePiece() {
		return exposedSourcePiece;
	}

	public Cell getSource() {
		return source;
	}

	@Override
	public State applyToGame(Game game) {
		return game.movePiece(source, getTargetCell());
	}

	@Override
	public State applyTo(GameType type, Board board,
			List<PieceStack> blackPlayerPieces,
			List<PieceStack> whitePlayerPieces) {
		return board.moveFrom(getPlayer(), getSource(), getTargetCell());
	}

	@Override
	public void undo(Board board, State originalState,
			List<PieceStack> blackPlayerPieces,
			List<PieceStack> whitePlayerPieces) {
		board.undoBoardMove(this, originalState);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder("Board Move ");
		builder.append(getPlayedPiece());
		builder.append(" from ");
		builder.append(getSource());
		if (exposedSourcePiece != null) {
			builder.append(" (exposing ");
			builder.append(exposedSourcePiece);
			builder.append(")");
		}
		appendTargetToString(builder);
		return builder.toString();
	}

	public static AbstractMove fromBytes(ByteBuffer buffer, Game game) {
		int sourceX = buffer.get();
		int sourceY = buffer.get();
		Cell source = new Cell(sourceX, sourceY);
		int exposedSourceVal = buffer.getInt();

		Piece playedPiece = game.getBoard().getVisiblePiece(sourceX, sourceY);
		Piece exposedSourcePiece = game.getBoard().peekNextPiece(source);
		int myExposedSourcePieceVal = exposedSourcePiece != null ? exposedSourcePiece
				.getVal() : 0;
		if (exposedSourceVal != myExposedSourcePieceVal) {
			throw new RuntimeException(
					"Invalid board move message, wrong exposed piece value. Received " + exposedSourceVal + ", but is " + myExposedSourcePieceVal);
		}

		CommonMoveInfo commonInfo = commonFromBytes(buffer, game, playedPiece);

		return new ExistingPieceMove(game.getCurrentPlayer(),
				commonInfo.playedPiece, exposedSourcePiece, source,
				commonInfo.target, commonInfo.existingTargetPiece);
	}

	@Override
	protected void privateAppendBytesToMessage(ByteBuffer buffer) {
		int x = source.getX();
		int y = source.getY();

		buffer.put((byte) x);
		buffer.put((byte) y);

		// checksum
		int sourceVal = exposedSourcePiece != null ? exposedSourcePiece
				.getVal() : 0;
		buffer.putInt(sourceVal);

		appendCommonToMessage(buffer);
	}

	@Override
	protected byte getMoveType() {
		return BOARD_MOVE;
	}

}
