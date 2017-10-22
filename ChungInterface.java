/**
 * @file ChungInterface.java
 * @author Bernardo Scapini Consoli
 * @date October 2017
 * 
 * @section DESCRIPTION
 * 
 * Interface of the RMI implementation for a Chung Tói Server utilizing the methods specified in 
 * TRABALHO DA AREA 1: JOGO CHUNG TOI DISTRIBUIDO EM JAVA RMI by Roland Teodorowitsch
 * 
 */


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChungInterface extends Remote {
	public int registraJogador(String nome) throws RemoteException;
	public int encerraPartida(int userId) throws RemoteException;
	public int temPartida(int userId) throws RemoteException;
	public int ehMinhaVez(int userId) throws RemoteException;
	public String obtemTabuleiro(int userId) throws RemoteException;
	public int posicionaPeca(int userId, int posNova, int orPeca) throws RemoteException;
	public int movePeca(int userId, int posInit, int sentDesloc, int distDesloc, int orNova) throws RemoteException;
	public String obtemOponente(int userId) throws RemoteException;
}
