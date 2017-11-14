/**
 * @file ChungServerImpl.java
 * @author Bernardo Scapini Consoli
 * @date November 2017
 * 
 * @section DESCRIPTION
 * 
 * Implementation of ChungServer.java.
 * 
 */

//temporização, pode tirar? -- numero máximo de jogadores, necessário? -- usuario ja cadastrado, é problema mesmo com preregistro?

package chungtoi;

import java.util.Date;
import javax.jws.WebService;
import java.util.LinkedList;
import java.util.Hashtable;

@WebService(endpointInterface =  "chungtoi.ChungServer")
public class ChungServerImpl implements ChungServer {

	//private static final long serialVersionUID = 1234L;
	private static Hashtable<Integer, ChungPlayer> jogadores; // Array containing all players
	private static Hashtable<String, Integer> jogadoresReservados; // Array containing preregistered players

	protected ChungServerImpl() {
        jogadores = new Hashtable<Integer, ChungPlayer>();
        jogadoresReservados = new Hashtable<String, Integer>();
	}

	public int preRegistro(String nome1, int id1, String nome2, int id2){
		ChungToi game = new ChungToi();
		jogadoresReservados.put(nome1, id1);
		jogadoresReservados.put(nome2, id2);
		jogadores.put(id1, new ChungPlayer(id1, id2, 1, game));
		jogadores.put(id2, new ChungPlayer(id2, id1, 0, game));
        return 0;
	}

	public int registraJogador(String nome){
		int playerId = jogadoresReservados.get(nome);
		ChungPlayer newPlayer = jogadores.get(playerId);
		newPlayer.registerPlayer(nome);
		jogadoresReservados.remove(nome);
		return playerId;
	}

	public int encerraPartida(int idUsuario){
        if (jogadores.get(idUsuario) == null) return -1;
		jogadores.remove(idUsuario); 
		return 0;
	}

	public int temPartida(int idUsuario){
		ChungPlayer jogador = jogadores.get(idUsuario);
        if (jogador == null) return -1;
		if (jogadores.get(jogador.getOpponent()).getName().length() == 0) return 0;
		if (jogador.getColor() == 1) return 1;
		if (jogador.getColor() == 0) return 2;
		return -1;
	}

	public int ehMinhaVez(int idUsuario){
		ChungPlayer jogador = jogadores.get(idUsuario);
        if (jogador == null) return -1;
		if (jogadores.get(jogador.getOpponent()).getName().length() == 0) return -2;
		if (jogador.getGame().checkVictory() == jogadores.get(jogador.getOpponent()).getColor()) return 3;
		if (jogador.getGame().checkVictory() == jogador.getColor()) return 2;		
		if (jogador.getColor() == jogador.getGame().getPlayerTurn()) return 1;
		if (jogador.getColor() != jogador.getGame().getPlayerTurn()) return 0;
		else return -1;
	}

	public String obtemTabuleiro(int idUsuario){
        if (jogadores.get(idUsuario) == null) return "";
        if (obtemOponente(idUsuario).length() == 0) return "";
		return jogadores.get(idUsuario).getGame().getBoard();
	}

	public int posicionaPeca(int idUsuario, int posNova, int orPeca) {
        ChungPlayer jogador = jogadores.get(idUsuario);
        if (jogador == null) return -1;
        if (obtemOponente(idUsuario).length() == 0) return -2;
        if (jogador.getColor() != jogador.getGame().getPlayerTurn()) return -4;
		return jogador.getGame().setPiece(posNova, orPeca);
	}

	public int movePeca(int idUsuario, int posInit, int sentDesloc, int distDesloc, int orNova){
        ChungPlayer jogador = jogadores.get(idUsuario);
        if (jogador == null) return -1;
        if (obtemOponente(idUsuario).length() == 0) return -2;
        if (jogador.getColor() != jogador.getGame().getPlayerTurn()) return -4;
		return jogador.getGame().movePiece(posInit, sentDesloc, distDesloc, orNova);
	}

	public String obtemOponente(int idUsuario){
        if (jogadores.get(idUsuario) == null) return "";
		return jogadores.get(jogadores.get(idUsuario).getOpponent()).getName();
	}
}
