/**
 * @file ChungServer.java
 * @author Bernardo Scapini Consoli
 * @date November 2017
 * 
 * @section DESCRIPTION
 * 
 * Interface of the WS implementation for a Chung Tói Server utilizing the methods specified in 
 * TRABALHO 2: JOGO CHUNG TOI AUTOMATIZADO EM JAVA WEB SERVICES by Roland Teodorowitsch
 * 
 */

package chungtoi;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface ChungServer {
	@WebMethod int preRegistro(String nome1, int id1, String nome2, int id2);
	@WebMethod int registraJogador(String nome);
	@WebMethod int encerraPartida(int userId);
	@WebMethod int temPartida(int userId);
	@WebMethod int ehMinhaVez(int userId);
	@WebMethod String obtemTabuleiro(int userId);
	@WebMethod int posicionaPeca(int userId, int posNova, int orPeca);
	@WebMethod int movePeca(int userId, int posInit, int sentDesloc, int distDesloc, int orNova);
	@WebMethod String obtemOponente(int userId);
}