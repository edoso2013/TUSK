package com.seniordesign.ultimatescorecard;


import java.util.ArrayList;

import com.seniordesign.ultimatescorecard.data.basketball.BasketballActivity;
import com.seniordesign.ultimatescorecard.data.basketball.BasketballGameTime;
import com.seniordesign.ultimatescorecard.data.football.FootballActivity;
import com.seniordesign.ultimatescorecard.data.football.FootballGameTime;
import com.seniordesign.ultimatescorecard.sqlite.DatabaseHelper;
import com.seniordesign.ultimatescorecard.sqlite.basketball.BasketballDatabaseHelper;
import com.seniordesign.ultimatescorecard.sqlite.helper.Teams;
import com.seniordesign.ultimatescorecard.view.StaticFinalVars;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//in sync with activity_choose_team.xml
public class ChooseTeamActivity extends Activity{
	LinearLayout _listOfTeams;																			//refers to the linear layout where we add the team names
	Button _deleteButton;																				//obviously refers to the delete button
	private SharedPreferences _teamsEntered;															//this is where we can store some information
	private Editor _prefEditor;																			//use the preference editor to edit the shared preference
	private boolean _selectAwayTeam = false;															//false = select Home team, true = select Away team
	private boolean _setDelete = false;																	//false = we can delete a team, true = we can't delete a team
	private Teams[] _teams = new Teams[2];																//Array of two Teams, used for passing teams to next activity
	private ArrayList<Teams> teams = null;
	private TextView _teamSelectTitle;
	private String _sportType;
	//databases
	public DatabaseHelper _db;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_team);
		_sportType = getIntent().getExtras().getString(StaticFinalVars.SPORT_TYPE);
		
		//databases
		if(_sportType.equals("basketball")){
			_db = new BasketballDatabaseHelper(getApplicationContext());
		}
		_teamSelectTitle = (TextView) findViewById(R.id.team_selection_title);							//getting the view of some features implemented in xml
		_listOfTeams = (LinearLayout) findViewById (R.id.teamListLayout);
		_deleteButton = (Button) findViewById (R.id.deleteTeamButton);
		
		//load teams from database (maybe put it catch for empty database?
		loadTeams();

	}	
	
	//when back button is hit to return to this page (restart it)
	@Override
	protected void onRestart() {
		super.onRestart();
		_selectAwayTeam = false;																		//time to select home team
		_teamSelectTitle.setText(getResources().getString(R.string.home_team_select_title));			//change the text of title
		_teams[0] = null;																				//empty array of team names to be sent along
		_teams[1] = null;
		_deleteButton.setEnabled(true);																	//reset the delete button
		for(int i=0; i < _listOfTeams.getChildCount(); i++){											//refresh all items in the linear layout
			TextView newItem = ((TextView)_listOfTeams.getChildAt(i));	
			newItem.setBackgroundColor(getResources().getColor(R.color.white));
			newItem.setTextColor(getResources().getColor(R.color.black));
		}
	}
	
	//getting string from shared preference, parsing it, and add to linear layout
	private void loadTeams(){
		//databases
		teams = (ArrayList<Teams>) _db.getAllTeams();
		for(Teams t: teams){
			this.addNewTeam(t.gettname());
		}
	}

	//saving team name in linear layout to the shared preference in phone
	@SuppressLint("CommitPrefEdits")
	private void saveTeams(){
		StringBuilder sb = new StringBuilder();															//building the string to save
		for(int i=0; i < _listOfTeams.getChildCount(); i++){
			sb.append(((TextView)_listOfTeams.getChildAt(i)).getText().toString()+",");					//put all the items in the list in one string separated by commas
		}
		if(sb.toString().length() > 0){																	//if our list is NOT empty
			_prefEditor.putString(_sportType+"TeamList", sb.toString());									//put the string in shared preferences
			_prefEditor.commit();																		//commit the change
		}
		else{																							//we no longer have anything in our list
			_prefEditor.clear();																		//clear saved material in shared preference
			_prefEditor.commit();																		//commit the change
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		_listOfTeams.removeAllViews();
		loadTeams();
	}

	//when the create a team button is pressed, this method is executed
	public void addViews(View v){
		Intent intent = new Intent(getApplicationContext(), CreateTeamActivity.class);
		if(!_selectAwayTeam){
			intent.putExtra(StaticFinalVars.CREATE_EDIT, "");
			intent.putExtra(StaticFinalVars.SPORT_TYPE, _sportType);
			startActivityForResult(intent, StaticFinalVars.CREATE_TEAM_CODE);	
		}
		else{
			intent.putExtra(StaticFinalVars.CREATE_EDIT, _teams[0].gettname());
			intent.putExtra(StaticFinalVars.SPORT_TYPE, _sportType);
			startActivityForResult(intent, StaticFinalVars.EDIT_TEAM_CODE);	
		}
	}
	
	//actually add a new team (list item) to our view
	private void addNewTeam(String teamName){
		final TextView textView = newTextView(teamName);														//creating a new text view giving the string we want it to display (Team Name)
		textView.setOnClickListener(new OnClickListener(){														//making this text view clickable
			@Override
			public void onClick(View view) {
				textView.setBackgroundColor(getResources().getColor(R.color.black));							//change color of background and text when view is clicked
				textView.setTextColor(getResources().getColor(R.color.white));
				if(_setDelete){																					//removing the item, we're in delete mode
					_listOfTeams.removeView(textView);
					
					Teams curTeam = null;
					for(Teams t: teams){
						if(t.gettname().equals(textView.getText())){
							curTeam = t;
							break;
						}
					}
					_db.deleteTeam(curTeam.gettid());
					
					_setDelete = false;
					_teamSelectTitle.setText(getResources().getString(R.string.home_team_select_title));
				}
				else if(!_selectAwayTeam){																		//we are selecting home team, now change interface to prompt for away team selection
					for(Teams t: teams){
						if(t.gettname().equals(textView.getText().toString())){
							_teams[0] = t;
						}
					}
					_teamSelectTitle.setText(getResources().getString(R.string.away_team_select_title));
					_selectAwayTeam = true;
					_deleteButton.setEnabled(false);															//when one team is selected, disable the delete feature
				}
				else{																							//not in delete mode, so one team was selected
					if(!_teams[0].gettname().equals(textView.getText().toString())){	
						for(Teams t: teams){
							if(t.gettname().equals(textView.getText().toString())){
								_teams[1] = t;
							}
						}												//add the second team to the string array
						confirmTeams(textView);																	//call confirmTeam method to got to next activity
					}
					else{																						//basically, here, we're cancelling our selection
						textView.setBackgroundColor(getResources().getColor(R.color.white));
						textView.setTextColor(getResources().getColor(R.color.black));
						_teamSelectTitle.setText(getResources().getString(R.string.home_team_select_title)); 	//back to selecting home team
						_teams[0] = null;																		//set first team (home team) to null
						_selectAwayTeam = false;																//we're not selecting the away team anymore
						_deleteButton.setEnabled(true);															//enable the delete button
					}
				}
			}
		});
		_listOfTeams.addView(textView);																			//add the textView to the linear layout
	} 
	
	//confirm that those are the teams that we want to keep scores for
	public void confirmTeams(final TextView tv){
		Builder confirmDialog = new Builder(this);																//building a dialog box for this
		confirmDialog.setTitle("Team Selection Confirmation");
		confirmDialog.setMessage("Keeping scores for "+ _teams[1].gettname() + " vs. " + _teams[0].gettname() + "?");
				
		confirmDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){							//the positive yes button
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intent;
				if(_sportType.equals("soccer")){
					intent = new Intent(getApplicationContext(), BasketballActivity.class);						//create new intent (basketball activity)
				}
				else if (_sportType.equals("football")){
					intent = new Intent(getApplicationContext(), FootballActivity.class);
					intent.putExtra(StaticFinalVars.GAME_INFO, new FootballGameTime(_teams[0], _teams[1]));
				}
				else if (_sportType.equals("baseball")){
					intent = new Intent(getApplicationContext(), BasketballActivity.class);
				}
				else{
					intent = new Intent(getApplicationContext(), BasketballActivity.class);	
					intent.putExtra(StaticFinalVars.GAME_TIME, new BasketballGameTime(_teams[0], _teams[1]));
				}
				
				startActivity(intent);																			//let's go
			}
		});
		confirmDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){							//the negative no button		
			@Override
			public void onClick(DialogInterface arg0, int arg1) {							
				tv.setBackgroundColor(getResources().getColor(R.color.white));									//undo the selection of the second (away) team
				tv.setTextColor(getResources().getColor(R.color.black));
				_teams[1] = null;
			}
		});
		confirmDialog.show();																					//make it happen
	}
	
	//creating the new text view and returning it
	private TextView newTextView(String teamName){
		final TextView textView = new TextView(this);														//these are all the stuff that you can do statically in xml
		textView.setText(teamName);																			//here, we're dynamically programming them in Java
		textView.setPadding(5,5,5,5);
		textView.setTextSize(24);
		return textView;
	}

	//deleting a team from the linear layout we called "listOfTeams"
	public void deleteATeam(View view){
		if(_listOfTeams.getChildCount() > 0){																		//as long as we have items to delete, then we can use this
			
			_teamSelectTitle.setText(getResources().getString(R.string.delete_team_title));
			_setDelete = true;											
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	    } 
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	        Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
	    }
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//saveTeams();
		finish();
	}
}
