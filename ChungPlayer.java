/**
 * @file ChungPlayer.java
 * @author Bernardo Scapini Consoli
 * @date October 2017
 * 
 * @section DESCRIPTION
 * 
 * A player class for the RMI implementation of the game Chung Toi
 * 
 */

public class ChungPlayer {

	private StringBuilder name; // Saves the name of the current client using this player
	private boolean free = true; // Whether this player is currently in a game or free to be used by a client
	private final int id; // Permanent ID provided upon object creation
	private final int opponentId; // Permanent ID of opponent provided during object creation
	private final int color; // Permanent player color provided during object creation; 0 is black, 1 is white
	private final ChungToi game; // Permanent game linked to this player; provided during object creation

	public ChungPlayer(int playerID, int opId, int playerColor, ChungToi playerGame){
		name = new StringBuilder();
		opponentId = opId;
		id = playerID;
		color = playerColor;
		game = playerGame;
	}

	public int registerPlayer(String playerName){
		name.append(playerName);
		free = false;
		return id;
	}
	
	public void clearPlayer(){
		name.setLength(0);
		free = true;
	}

	public boolean free(){
		return free;
	}

	public int getId(){
		return id;
	}

	public String getName(){
		return name.toString();
	}

	public int getColor(){
		return color;
	}

	public int getOpponent(){
		return opponentId;
	}

	public ChungToi getGame(){
		return game;
	}
}
