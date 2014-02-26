package com.oakonell.ticstacktoe.ui.local;

import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.oakonell.ticstacktoe.MainActivity;
import com.oakonell.ticstacktoe.model.Game;
import com.oakonell.ticstacktoe.model.GameMode;
import com.oakonell.ticstacktoe.model.Player;
import com.oakonell.ticstacktoe.model.ScoreCard;
import com.oakonell.ticstacktoe.model.solver.AiPlayerStrategy;
import com.oakonell.ticstacktoe.ui.game.SoundManager;

public class AiGameStrategy extends AbstractLocalStrategy {

	private final int aiDepth;

	public AiGameStrategy(MainActivity mainActivity, SoundManager soundManager,
			int aiDepth) {
		super(mainActivity, soundManager);
		this.aiDepth = aiDepth;
	}

	public AiGameStrategy(MainActivity mainActivity, SoundManager soundManager,
			int aiDepth, AiMatchInfo matchInfo) {
		super(mainActivity, matchInfo, soundManager);
		this.aiDepth = aiDepth;
	}

	public void playAgain() {
		Game game = getMatchInfo().readGame(getContext());

		startGame(game.getBlackPlayer().getName(), game.getWhitePlayer()
				.getName(), game.getType(), getMatchInfo().getScoreCard());
		// TODO, keep track of score, and switch first player...
	}

	protected AiMatchInfo createMatchInfo(String blackName, String whiteName,
			final Game game, ScoreCard score) {
		return new AiMatchInfo(TurnBasedMatch.MATCH_STATUS_ACTIVE,
				TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN, blackName, whiteName,
				aiDepth, System.currentTimeMillis(), game, score, 0, 0);
	}

	protected GameMode getGameMode() {
		return GameMode.AI;
	}

	@Override
	protected Player createWhitePlayer(String whiteName) {
		return AiPlayerStrategy.createWhitePlayer(whiteName, false, aiDepth);
	}

}
