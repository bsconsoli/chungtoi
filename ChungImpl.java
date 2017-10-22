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
	private static ChungPlayer[] jogadoresReservados // Array containing preregistered players

	protected ChungImpl() throws RemoteException {
    	/**
	     * All 500 games and 1000 players are initialized here. Clients are assigned 
         * players that already exist, and when they leave, the players are wiped
         * and become ready for reuse. Games are similarly reused once no clients are 
         * attached to their players.
	     */
		jogadores = new ChungPlayer[1000];
		jogadoresReservados = new ChungPlayer[1000];
		for(int i = 0; i < 500; i++){
			ChungToi game = new ChungToi();
			jogadores[i] = new ChungPlayer(i, 999-i, 1, game);
			jogadores[999-i] = new ChungPlayer(999-i, i, 0, game);
		}
	}

	/**
	 * @brief Assigns a free player to a client
	 * 
	 * First, listaDeEspera is checked, so that games waiting for one player are prioritized.
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
					novoId = novoJogador.getId();
					break;
				}
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
			listaDeEspera.remove();
		} else{
			return -2;
		}
		if (jogadores[jogador.getOpponent()].getName().length() == 0) return 0;
		if (jogador.getColor() == 1) {
            return 1;
        }
		else {
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
			return 5;
		} else {
			return 5;
		}
		if (jogadores[jogador.getOpponent()].getName().length() == 0) return -1;
		if (jogador.getColor() == jogador.getGame().getPlayerTurn()){
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
