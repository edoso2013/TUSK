package com.seniordesign.ultimatescorecard.stats.hockey;

import com.seniordesign.ultimatescorecard.R;
import com.seniordesign.ultimatescorecard.data.GameInfo;
import com.seniordesign.ultimatescorecard.data.hockey.HockeyTeam;
import com.seniordesign.ultimatescorecard.sqlite.helper.Players;
import com.seniordesign.ultimatescorecard.sqlite.helper.Teams;
import com.seniordesign.ultimatescorecard.stats.soccer.SoccerStatsActivity;
import com.seniordesign.ultimatescorecard.view.StaticFinalVars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HockeyBoxscoreFragment extends Fragment{ 
	private boolean _lookingAtHome = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = (View) inflater.inflate(R.layout.fragment_boxscore, container, false);
        return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		GameInfo info = ((HockeyStatsActivity) getActivity()).getGameInfo();
		
		((TextView)getView().findViewById(R.id.homeTeamStatText)).setText(((HockeyStatsActivity) getActivity()).getGameInfo().getHomeTeam().gettname());
		((TextView)getView().findViewById(R.id.awayTeamStatText)).setText(((HockeyStatsActivity) getActivity()).getGameInfo().getAwayTeam().gettname());
		
		if(_lookingAtHome){
			getView().findViewById(R.id.homeTeamStatText).setBackgroundColor(getResources().getColor(R.color.robin_egg_blue));
			getView().findViewById(R.id.awayTeamStatText).setBackgroundColor(getResources().getColor(R.color.white));
		}
		else{
			getView().findViewById(R.id.homeTeamStatText).setBackgroundColor(getResources().getColor(R.color.white));
			getView().findViewById(R.id.awayTeamStatText).setBackgroundColor(getResources().getColor(R.color.robin_egg_blue));
		}
		
		getView().findViewById(R.id.homeTeamStatText).setOnClickListener(homeTeamListener);
		getView().findViewById(R.id.awayTeamStatText).setOnClickListener(awayTeamListener);
		
		removeAllViews();
		addTextViews();
	}
	
	private void addTextViews(){
		LinearLayout layout = ((LinearLayout) getView().findViewById(R.id.playerListLayout));
		GameInfo _gameInfo = ((HockeyStatsActivity) getActivity()).getGameInfo();
		HockeyTeam team = null;
		if(_lookingAtHome){
			for(Players p: _gameInfo.getHomePlayers()){
				layout.addView(newTextView(p.getpname()));	
			}
			layout.addView(newTextView(_gameInfo.getHomeTeam().getabbv() +  " Stats"));		}
		else{
			for(Players p: _gameInfo.getAwayPlayers()){
				layout.addView(newTextView(p.getpname()));	
			}		
			layout.addView(newTextView(_gameInfo.getAwayTeam().getabbv() +  " Stats"));
		}
	}
	
	private void removeAllViews(){
		LinearLayout layout = ((LinearLayout) getView().findViewById(R.id.playerListLayout));
		layout.removeAllViews();
	}
	
	private TextView newTextView(String teamName){
		final TextView textView = new TextView(getActivity());
		textView.setText(teamName);
		textView.setPadding(5,5,5,5);
		textView.setTextSize(20);
		textView.setOnClickListener(selectPlayerListener);
		return textView;
	}
	
	private OnClickListener homeTeamListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			getView().findViewById(R.id.homeTeamStatText).setBackgroundColor(getResources().getColor(R.color.robin_egg_blue));
			getView().findViewById(R.id.awayTeamStatText).setBackgroundColor(getResources().getColor(R.color.white));
			if(!_lookingAtHome){
				_lookingAtHome = true;
				removeAllViews();
				addTextViews();
				
			}
		}
	};
	
	private OnClickListener awayTeamListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			getView().findViewById(R.id.homeTeamStatText).setBackgroundColor(getResources().getColor(R.color.white));
			getView().findViewById(R.id.awayTeamStatText).setBackgroundColor(getResources().getColor(R.color.robin_egg_blue));
			if(_lookingAtHome){
				_lookingAtHome = false;
				removeAllViews();
				addTextViews();			
			}
		}
	};
	
	private OnClickListener selectPlayerListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity().getApplicationContext(), HockeyIndividualStatActivity.class);
			if(_lookingAtHome){
				intent.putExtra(StaticFinalVars.TEAM_INFO, ((HockeyStatsActivity) getActivity()).getGameInfo().getHomeTeam());
				intent.putExtra(StaticFinalVars.PLAYERS_INFO, ((HockeyStatsActivity) getActivity()).getGameInfo().getHomePlayers());
				intent.putExtra(StaticFinalVars.GAME_ID, ((HockeyStatsActivity) getActivity()).getGameInfo().getgid());
				intent.putExtra(StaticFinalVars.HOME_OR_AWAY, _lookingAtHome);
				intent.putExtra(StaticFinalVars.SHOT_CHART, ((HockeyStatsActivity) getActivity()).getHomeShotChart());
			}
			else{
				intent.putExtra(StaticFinalVars.TEAM_INFO, ((HockeyStatsActivity) getActivity()).getGameInfo().getAwayTeam());
				intent.putExtra(StaticFinalVars.PLAYERS_INFO, ((HockeyStatsActivity) getActivity()).getGameInfo().getAwayPlayers());
				intent.putExtra(StaticFinalVars.GAME_ID, ((HockeyStatsActivity) getActivity()).getGameInfo().getgid());
				intent.putExtra(StaticFinalVars.HOME_OR_AWAY, _lookingAtHome);
				intent.putExtra(StaticFinalVars.SHOT_CHART, ((HockeyStatsActivity) getActivity()).getAwayShotChart());
			}
			intent.putExtra(StaticFinalVars.PLAYER_NAME, ((TextView)v).getText().toString());
			intent.putExtra(StaticFinalVars.GAME_INFO, ((HockeyStatsActivity) getActivity()).getGameInfo());
			intent.putExtra(StaticFinalVars.DISPLAY_TYPE, 0);
			startActivity(intent);
		}
	};
}
