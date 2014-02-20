package com.oakonell.ticstacktoe.ui.local;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.oakonell.ticstacktoe.MainActivity;
import com.oakonell.ticstacktoe.R;
import com.oakonell.ticstacktoe.model.Game;
import com.oakonell.ticstacktoe.model.GameMode;
import com.oakonell.ticstacktoe.model.GameType;
import com.oakonell.ticstacktoe.model.Player;
import com.oakonell.ticstacktoe.model.ScoreCard;
import com.oakonell.ticstacktoe.model.db.DatabaseHandler;
import com.oakonell.ticstacktoe.model.db.DatabaseHandler.OnLocalMatchUpdateListener;
import com.oakonell.ticstacktoe.model.solver.MinMaxAI;
import com.oakonell.ticstacktoe.model.solver.RandomAI;
import com.oakonell.ticstacktoe.ui.game.GameFragment;
import com.oakonell.ticstacktoe.ui.game.HumanStrategy;
import com.oakonell.ticstacktoe.ui.game.SoundManager;

public class AiGameStrategy extends AbstractLocalStrategy {

	private int aiDepth;

	public AiGameStrategy(MainActivity mainActivity, SoundManager soundManager) {
		super(mainActivity, soundManager);
	}

	public AiGameStrategy(MainActivity mainActivity, SoundManager soundManager,
			LocalMatchInfo localMatchInfo) {
		super(mainActivity, localMatchInfo, soundManager);
	}

	public void startGame(String blackName, String whiteName, GameType type,
			int aiDepth) {
		this.aiDepth = aiDepth;
		Player blackPlayer = HumanStrategy.createPlayer(blackName, true);

		Player whitePlayer;
		if (aiDepth < 0) {
			whitePlayer = RandomAI.createPlayer(whiteName, false);
		} else {
			whitePlayer = MinMaxAI.createPlayer(whiteName, false, aiDepth);
		}

		Tracker myTracker = EasyTracker.getTracker();
		myTracker.sendEvent(getMainActivity().getString(R.string.an_start_game_cat),
				getMainActivity().getString(R.string.an_start_ai_game_action), type
						+ "", 0L);

		final Game game = new Game(type, GameMode.AI, blackPlayer, whitePlayer,
				blackPlayer);

		final ScoreCard score = new ScoreCard(0, 0, 0);

		DatabaseHandler db = new DatabaseHandler(getMainActivity());
		matchInfo = new LocalMatchInfo(TurnBasedMatch.MATCH_STATUS_ACTIVE,
				TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN, blackName, whiteName,
				aiDepth, System.currentTimeMillis(), game);
		db.insertMatch(matchInfo, new OnLocalMatchUpdateListener() {
			@Override
			public void onUpdateSuccess(LocalMatchInfo matchInfo) {
				GameFragment gameFragment = getMainActivity().getGameFragment();
				if (gameFragment == null) {
					gameFragment = new GameFragment();

					FragmentManager manager = getMainActivity()
							.getSupportFragmentManager();
					FragmentTransaction transaction = manager
							.beginTransaction();
					transaction.replace(R.id.main_frame, gameFragment,
							MainActivity.FRAG_TAG_GAME);
					transaction.addToBackStack(null);
					transaction.commit();
				}
				gameFragment.startGame(game, score, null, false);

			}

			@Override
			public void onUpdateFailure() {
				// TODO Auto-generated method stub

			}
		});
	}

	public void playAgain() {
		Game game = matchInfo.readGame(getMainActivity());

		startGame(game.getBlackPlayer().getName(), game.getWhitePlayer()
				.getName(), game.getType(), aiDepth);
		// TODO, keep track of score, and switch first player...
	}

}