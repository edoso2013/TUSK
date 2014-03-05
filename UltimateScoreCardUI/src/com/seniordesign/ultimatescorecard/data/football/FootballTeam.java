package com.seniordesign.ultimatescorecard.data.football;

import java.util.ArrayList;

import com.seniordesign.ultimatescorecard.data.Team;

public class FootballTeam extends Team{
	private static final long serialVersionUID = -4547516998953968383L;
	private ArrayList<FootballPlayer> players = new ArrayList<FootballPlayer>();
	
	public FootballTeam (String teamName, boolean homeTeam){
		super(teamName, homeTeam);
		setTeamAbbr(teamName);
		setTeamPlayers(teamName);	
	}
	
	private void setTeamAbbr(String team){
		if(team.equals("New England Patriots")){
			_teamAbbr = "NE";
		}
		else if(team.equals("New York Jets")){
			_teamAbbr = "NYJ";
		}
		else if(team.equals("Chicago Bears")){
			_teamAbbr = "DEN";
		}
		else if(team.equals("Dallas Cowboys")){
			_teamAbbr = "DAL";
		}
		else if(team.equals("New Orlean Saints")){
			_teamAbbr = "NO";
		}
		else {
			if(_homeTeam){
				_teamAbbr = "Home";
			}
			else{
				_teamAbbr = "Away";
			}
		}
	}
	
	private void setTeamPlayers(String team){
		if(team.equals("New England Patriots")){
			players.add(new FootballPlayer("Tom Brady"));
			players.add(new FootballPlayer("Stevan Ridley"));
			players.add(new FootballPlayer("LaGarrette Blount"));
			players.add(new FootballPlayer("Shane Vereen"));
			players.add(new FootballPlayer("Julian Edelman"));
			players.add(new FootballPlayer("Aaron Dobson"));
			players.add(new FootballPlayer("Danny Amendola"));
			players.add(new FootballPlayer("Kenbrell Thompkins"));
			players.add(new FootballPlayer("Rob Gronkowski"));
			players.add(new FootballPlayer("Michael Hoomanawanui"));
		}
		else if(team.equals("New York Jets")){
			players.add(new FootballPlayer("Geno Smith"));
			players.add(new FootballPlayer("Pierre Thomas"));
			players.add(new FootballPlayer("Bilal Powell"));
			players.add(new FootballPlayer("Stephen Hill"));
			players.add(new FootballPlayer("Jeremy Kerley"));
			players.add(new FootballPlayer("Santonio Holmes"));
			players.add(new FootballPlayer("Joshua Cribbs"));
			players.add(new FootballPlayer("David Nelson"));
			players.add(new FootballPlayer("Greg Salas"));
			players.add(new FootballPlayer("Jeff Cumberland"));
		}
		else if(team.equals("Chicago Bears")){
			players.add(new FootballPlayer("Jay Cutler"));
			players.add(new FootballPlayer("Matt Forte"));
			players.add(new FootballPlayer("Michael Bush"));
			players.add(new FootballPlayer("Brandon Marshall"));
			players.add(new FootballPlayer("Alshon Jeffreys"));
			players.add(new FootballPlayer("Devin Hester"));
			players.add(new FootballPlayer("Earl Bennett"));
			players.add(new FootballPlayer("Dante Rosario"));
			players.add(new FootballPlayer("Martellus Bennett"));
			players.add(new FootballPlayer("Tony Fiametta"));
		}
		else if(team.equals("Dallas Cowboys")){
			players.add(new FootballPlayer("Tony Romo"));
			players.add(new FootballPlayer("DeMarco Murray"));
			players.add(new FootballPlayer("Joesph Randle"));
			players.add(new FootballPlayer("Dez Bryant"));
			players.add(new FootballPlayer("Miles Austin"));
			players.add(new FootballPlayer("Terrance Williams"));
			players.add(new FootballPlayer("Dwayne Harris"));
			players.add(new FootballPlayer("Cole Beasley"));
			players.add(new FootballPlayer("Jason Witten"));
			players.add(new FootballPlayer("James Hanna"));
			
		}
		else if(team.equals("New Orlean Saints")){
			players.add(new FootballPlayer("Drew Brees"));
			players.add(new FootballPlayer("Mark Ingram"));
			players.add(new FootballPlayer("Darren Sproles"));
			players.add(new FootballPlayer("Tielor Robinson"));
			players.add(new FootballPlayer("Marques Colston"));
			players.add(new FootballPlayer("Kenny Stills"));
			players.add(new FootballPlayer("Lance Moore"));
			players.add(new FootballPlayer("Robert Meachem"));
			players.add(new FootballPlayer("Jimmy Graham"));
			players.add(new FootballPlayer("Benjamin Watson"));
		}
		else {
			players.add(new FootballPlayer("Quarterback"));
			players.add(new FootballPlayer("Half-back 1"));
			players.add(new FootballPlayer("Half-back 2"));
			players.add(new FootballPlayer("Half-back 3"));
			players.add(new FootballPlayer("Wide Receiver 1"));
			players.add(new FootballPlayer("Wide Receiver 2"));
			players.add(new FootballPlayer("Wide Receiver 3"));
			players.add(new FootballPlayer("Wide Receiver 4"));
			players.add(new FootballPlayer("Tight-end 1"));
			players.add(new FootballPlayer("Tight-end 2"));
		}
	}
	
	public FootballPlayer getPlayer(int player){
		return players.get(player);
	}
	
	public ArrayList<FootballPlayer> getRoster(){
		return players;
	}
	
	public FootballPlayer getPlayer(String player){
		for(int i=0; i<players.size(); i++){
			if(player.equals(players.get(i).getName())){
				return players.get(i);
			}
		}
		return null;
	}
	
	public int getPlayerAsPositionInArray(String player){
		for(int i=0; i<players.size(); i++){
			if(player.equals(players.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	public void scoreChange(int point, String player1, String player2){
		_score += point;
		if(player2.equals(null)){
			getPlayer(player1).rushTD();
		}
		else{
			getPlayer(player1).passTD();
			getPlayer(player2).recTD();
		}
	}

	public int numberPlayers() {
		return players.size();
	}	
}