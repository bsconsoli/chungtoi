/**
 * @file ChungImpl.java
 * @author Bernardo Scapini Consoli
 * @date October 2017
 * 
 * @section DESCRIPTION
 * 
 * Implementation of ChungInterface.java.
 * 
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class ChungImpl extends UnicastRemoteObject implements ChungInterface {

	private static final long serialVersionUID = 1234L;
	private static ChungPlayer[] jogadores; // Array containing all players
	private static LinkedList<ChungPlayer> listaDeEspera = new LinkedList(); // List of players wherein their opponents have been associated with a client
	private static int contadores[] = new int[500]; // Array of timers for all games
	private static int modosTimer[] = new int[500]; // Array of timer modes for all games
	private static final int MAXTIMER = 60; // Seconds in which a player must make a move
	private static final Object lock = new Object(); // Synchronization lock

	protected ChungImpl() throws RemoteException {
		for (int i=0; i<500;i++){
				contadores[i] = 0;
				modosTimer[i] = 0;
		}
		CounterThread counters = new CounterThread();
		counters.start();

    	/**
	     * All 500 games and 1000 players are initialized here. Clients are assigned 
         * players that already exist, and when they leave, the players are wiped
         * and become ready for reuse. Games are similarly reused once no clients are 
         * attached to their players.
	     */
		jogadores = new ChungPlayer[1000];
		for(int i = 0; i < 500; i++){
			ChungToi game = new ChungToi();
			jogadores[i] = new ChungPlayer(i, 999-i, 1, game);
			jogadores[999-i] = new ChungPlayer(999-i, i, 0, game);
		}
	}

	/**
	 * @brief Class for the thread that keeps track of the timers
	 * 
	 * The thread pulses every second to update the timers. A timer has four modes: -1, reset mode; 
	 * 0, inactive mode; 1, waiting for player mode; 2, waiting for move mode.
	 *
	 * The modes work as following:
	 *	- -1: resets the players and game so that they may be reused. Also resets the associated timer;
	 *  -  0: Does nothing;
	 *  -  1: Keeps time, but if timer reaches the maximum waiting for other player time, the timer is reset;
     *  -  2: Keeps time, but if timer reaches the maximum waiting for next move time, the timer is reset;
	 *	
	 */

	public class CounterThread extends Thread {

		@Override
		public void run() {
			while(true){
				for (int i=0; i<500;i++){
					synchronized (lock) {
						if (modosTimer[i] == 0) continue;
						else if (modosTimer[i] == -1){
							ChungPlayer jogador = jogadores[i];
							ChungToi jogo = jogador.getGame();
							jogadores[jogador.getOpponent()].clearPlayer();
							jogo.wipeBoard();
							jogador.clearPlayer();
							contadores[i] = 0;
							modosTimer[i] = 0;
						} else contadores[i]++;
                        if (modosTimer[i] == 1){
                            if (contadores[i] > MAXTIMER * 2) modosTimer[i] = -1;
                        }
                        if (modosTimer[i] == 2) {
                            if (contadores[i] > MAXTIMER) modosTimer[i] = -1;
                        }
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @brief Assigns a free player to a client
	 * 
	 * First, the listaDeEspera is checked, so that games waiting for one player are prioritized.
	 * Then, each white player is checked for if they are free. Black players are not checked because
	 * if all white players are filled, and no players are waiting for a client, then all black players
	 * are also filled.
	 * Finally, timer of the player assigned to the client is set to waiting for player mode.
	 *	
	 */

	@Override
	public int registraJogador(String nome) throws RemoteException{
		ChungPlayer novoJogador;
		int novoId = -2;

		if(!listaDeEspera.isEmpty()){
			novoJogador = listaDeEspera.remove();
			novoJogador.registerPlayer(nome);
			novoId = novoJogador.getId();
		}
		else {
			for(int i = 0; i < 500; i++){
				if(jogadores[i].free()){
					novoJogador = jogadores[i];
					novoJogador.registerPlayer(nome);
					listaDeEspera.add(jogadores[999-i]);
					synchronized (lock) {
						modosTimer[i] = 1;
					}
					novoId = novoJogador.getId();
					break;
				}
			}
		}

		if (novoId >= 0){
			int novoTimer;
			if (novoId >= 500) novoTimer = 999 - novoId;
			else novoTimer = novoId;
            synchronized (lock) {
			    contadores[novoTimer] = 0;
            }
		}

		return novoId;
	}

   /**
	 * @brief Reset game and players
	 * 
	 * Ends the match by resetting the game state and both players so that all may be used
	 * immediately by other clients.
	 *	
	 */

	@Override
	public int encerraPartida(int idUsuario) throws RemoteException{
		if (idUsuario < 500) {
			synchronized (lock) {
				modosTimer[idUsuario] = -1;
			}
		} else {
			synchronized (lock) {
				modosTimer[999 - idUsuario] = -1;
			}
		}
		return 0;
	}

   /**
	 * @brief Check if game has two players
	 * 
	 * If there has been no timeout, and there are two players, the clients learn the color 
	 * of their players. 1 for white, 2 for black.
	 *	
	 */

	@Override
	public int temPartida(int idUsuario) throws RemoteException{
		ChungPlayer jogador = jogadores[idUsuario];
		if (idUsuario < 500){
			synchronized (lock) {
				if(contadores[idUsuario] >= MAXTIMER * 2) {
					modosTimer[idUsuario] = -1;
					listaDeEspera.remove();
					return -2;
				}
			}
		} else{
			synchronized (lock) { 
				if (contadores[999 - idUsuario] >= MAXTIMER * 2){
					modosTimer[999 - idUsuario] = -1;
					return -2;
				} 
			}
		}
		if (jogadores[jogador.getOpponent()].getName().length() == 0) return 0;
		if (jogador.getColor() == 1) {
            modosTimer[idUsuario] = 2;
            return 1;
        }
		else {
            modosTimer[999 - idUsuario] = 2;
            return 2;
        }
	}

   /**
	 * @brief Check if it is this player's turn or if a game ending condition has been reached
	 * 
	 * Firstly it is checked if either player has acheved a winning board configuration. Secondly,
	 * there is a timeout check. Finally, there is a check for if it is the turn of the asking client.
	 *	
	 */

	@Override
	public int ehMinhaVez(int idUsuario) throws RemoteException{
		ChungPlayer jogador = jogadores[idUsuario];
		if (jogador.getGame().checkVictory() == jogador.getColor()) return 2;
		if (jogador.getGame().checkVictory() == jogadores[jogador.getOpponent()].getColor()) return 3;
		if (idUsuario < 500){
			synchronized (lock) {
				if(contadores[idUsuario] >= MAXTIMER) {
					modosTimer[idUsuario] = -1;
					return 5;
				}
			}
		} else {
			synchronized (lock) {
				if (contadores[999-idUsuario] >= MAXTIMER) {
					modosTimer[999 - idUsuario] = -1;
					return 5;
				}
			}
		}
		if (jogadores[jogador.getOpponent()].getName().length() == 0) return -1;
		if (jogador.getColor() == jogador.getGame().getPlayerTurn()){
			if (idUsuario < 500){
				synchronized (lock) {
					contadores[idUsuario] = 0;
				}
			} else {
				synchronized (lock) {
					contadores[999 - idUsuario] = 0;
				}
			}
			return 1;
		}
		else return 0;
	}

	@Override
	public String obtemTabuleiro(int idUsuario) throws RemoteException{
		return jogadores[idUsuario].getGame().getBoard();
	}

	@Override
	public int posicionaPeca(int idUsuario, int posNova, int orPeca) throws RemoteException{
		return jogadores[idUsuario].getGame().setPiece(posNova, orPeca);
	}

	@Override
	public int movePeca(int idUsuario, int posInit, int sentDesloc, int distDesloc, int orNova) throws RemoteException{
		return jogadores[idUsuario].getGame().movePiece(posInit, sentDesloc, distDesloc, orNova);
	}

	@Override
	public String obtemOponente(int idUsuario) throws RemoteException{
		return jogadores[jogadores[idUsuario].getOpponent()].getName();
	}
}
