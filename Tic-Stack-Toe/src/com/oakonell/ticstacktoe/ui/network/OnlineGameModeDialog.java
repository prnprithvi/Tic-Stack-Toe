package com.oakonell.ticstacktoe.ui.network;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.oakonell.ticstacktoe.R;
import com.oakonell.ticstacktoe.model.GameType;
import com.oakonell.ticstacktoe.ui.menu.GameTypeSpinnerHelper;
import com.oakonell.ticstacktoe.ui.menu.TypeDropDownItem;

public class OnlineGameModeDialog extends SherlockDialogFragment {
	public static final String SELECT_PLAYER_INTENT_KEY = "select_player";

	public interface OnlineGameModeListener {
		void chosenMode(GameType type, boolean useTurnBased, boolean isRanked);
	}

	private OnlineGameModeListener listener;
	private boolean isQuick;

	public void initialize(boolean isQuick, OnlineGameModeListener listener) {
		this.listener = listener;
		this.isQuick = isQuick;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		View view = inflater.inflate(R.layout.dialog_online_type, container,
				false);

		TextView titleView = (TextView) view.findViewById(R.id.title);
		if (isQuick) {
			titleView.setText(R.string.choose_quick_game_mode_title);
		} else {
			titleView.setText(R.string.choose_online_game_mode_title);
		}

		Button start = (Button) view.findViewById(R.id.start);
		if (isQuick) {
			start.setText(R.string.choose_online_opponent);
		}

		final Spinner typeSpinner = (Spinner) view.findViewById(R.id.game_type);
		GameTypeSpinnerHelper.populateSpinner(getActivity(), typeSpinner);
		final TextView typeDescr = (TextView) view
				.findViewById(R.id.game_type_descr);
		GameTypeSpinnerHelper
				.setOnChange(getActivity(), typeSpinner, typeDescr);

		final CheckBox realtime = (CheckBox) view.findViewById(R.id.realtime);

		final CheckBox isRanked = (CheckBox) view
				.findViewById(R.id.ranked_game);

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

				TypeDropDownItem typeItem = (TypeDropDownItem) typeSpinner
						.getSelectedItem();
				boolean isTurnBased = !realtime.isChecked();

				listener.chosenMode(typeItem.type, isTurnBased,
						isRanked.isChecked());
			}
		});
		return view;

	}

}
