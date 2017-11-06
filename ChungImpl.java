/**
 * @file ChungImpl.java
 * @author Bernardo Scapini Consoli
 * @date October 2017
 * @modification November 2017
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
	private static Dictionary<Integer, ChungPlayer> jogadores = new Dictionary<Integer, ChungPlayer>(); // Array containing all players
	private static Dictionary<String, Integer> jogadoresReservados = new Dictinary<String, Integer>(); // Array containing preregistered players

	protected ChungImpl() throws RemoteException {

	}

	@Override
	public int preRegistro(String nome1, int id1, String nome2, int id2){
		ChungToi game = new ChungToi();
		jogadoresReservados.put(nome1, id1);
		jogadoresReservados.put(nome2, id2);
		jogadores.put(id1, new ChungPlayer(id1, id2, 1, game));
		jogadores.put(id2, new ChungPlayer(id2, id1, 0, game));
	}

	@Override
	public int registraJogador(String nome) throws RemoteException{
		int playerId = jogadoresReservados.get(nome);
		ChungPlayer newPlayer = jogadores.get(playerId);
		if (!newplayer.getName().equals("")) return -1;
		newPlayer.registerPlayer(nome);
		jogadoresReservados.remove(nome);

		return playerId;
	}

	@Override
	public int encerraPartida(int idUsuario) throws RemoteException{
		jogadores.remove(idUsuario); 
		return 0;
	}

	@Override
	public int temPartida(int idUsuario) throws RemoteException{
		ChungPlayer jogador = jogadores.get(idUsuario);
		if (jogadores.(jogador.getOpponent()).getName().length() == 0) return 0;
		if (jogador.getColor() == 1) return 1;
		if (jogador.getColor() == 0) return 2;
		return -1;
	}

	@Override
	public int ehMinhaVez(int idUsuario) throws RemoteException{
		ChungPlayer jogador = jogadores.get(idUsuario);
		if (jogadores.get(jogador.getOpponent()).getName().length() == 0) return -2;
		if (jogador.getGame().checkVictory() == jogadores.get(jogador.getOpponent()).getColor()) return 3;
		if (jogador.getGame().checkVictory() == jogador.getColor()) return 2;		
		if (jogador.getColor() == jogador.getGame().getPlayerTurn()) return 1;
		if (jogador.getColor() != jogador.getGame().getPlayerTurn()) return 0;
		else return -1;
	}

	@Override
	public String obtemTabuleiro(int idUsuario) throws RemoteException{
		return jogadores.get(idUsuario).getGame().getBoard();
	}

	@Override
	public int posicionaPeca(int idUsuario, int posNova, int orPeca) throws RemoteException{
		return jogadores.get(idUsuario).getGame().setPiece(posNova, orPeca);
	}

	@Override
	public int movePeca(int idUsuario, int posInit, int sentDesloc, int distDesloc, int orNova) throws RemoteException{
		return jogadores.get(idUsuario).getGame().movePiece(posInit, sentDesloc, distDesloc, orNova);
	}

	@Override
	public String obtemOponente(int idUsuario) throws RemoteException{
		return jogadores.get(jogadores.get(idUsuario).getOpponent()).getName();
	}
}
