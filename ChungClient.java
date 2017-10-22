/**
 * @file ChungClient.java
 * @author Bernardo Scapini Consoli
 * @date October 2017
 * 
 * @section DESCRIPTION
 * 
 * Implementation of the client-side operations for an RMI Chung Toi Server
 * 
 */

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ChungClient {

	public static void main(String[] args) {
		if	(args.length != 2) {
			System.err.println("ERRO! Argumentos entrados incorretamente!\nChungClient <server host> <player name>");
			System.exit(1);
		}
		System.setProperty("java.security.policy","ChungClient.policy");
		try {
			ChungInterface chungToi = (ChungInterface) Naming.lookup ("//"+args[0]+"/ChungToi");

			// Registering client with server
			int id = chungToi.registraJogador(args[1]);
			if (id == -2){
				System.err.println("ERRO! Numero maximo de partidas alcancado!\n Tente novamente mais tarde!");
				System.exit(1);
			}

			// Opponent search loop
			int temPartida = chungToi.temPartida(id);
			System.out.println("Procurando Oponente...");
			while(temPartida <= 0){
				if(temPartida == -2){
					System.err.println("Tempo de espera por partida esgotado!\nTente novamente mais tarde!");
					chungToi.encerraPartida(id);
					System.exit(1);
				} else if(temPartida == -1){
					System.err.println("Erro inesperado!\nTente novamente!");
					chungToi.encerraPartida(id);
					System.exit(1);
				}
				temPartida = chungToi.temPartida(id);
			}
			if(temPartida == 1){
				System.out.println("Oponente encontrado!\nVoce e o jogador claro!\nJogo ID: " + id);
			} else {
				System.out.println("Oponente encontrado!\nVoce e o jogador escuro!");
			}

			// Obtains opponent name
			String oponente = chungToi.obtemOponente(id);

			// Main game loop
			Scanner sc = new Scanner(System.in);
			int minhaVez = chungToi.ehMinhaVez(id);
			int pecas = 0;
			boolean esperando = true;
			while(true){
				if(minhaVez == -1){ // Error has occured
					System.err.println("Erro inesperado!\nEncerrando partida!");
					chungToi.encerraPartida(id);
					System.exit(1);
				} else if (minhaVez == 0){ // Not your turn
					if (esperando){
						System.out.println("Esperando Oponente...");
						esperando = false;
					} 
				} else if (minhaVez == 1){ // Your turn
					esperando = true;

					// Print board
					String tabuleiro = chungToi.obtemTabuleiro(id);
					System.out.println(tabuleiro.charAt(0) + " | " + tabuleiro.charAt(1) + " | " + tabuleiro.charAt(2));
					System.out.println("- + - + -");
					System.out.println(tabuleiro.charAt(3) + " | " + tabuleiro.charAt(4) + " | " + tabuleiro.charAt(5));
					System.out.println("- + - + -");
					System.out.println(tabuleiro.charAt(6) + " | " + tabuleiro.charAt(7) + " | " + tabuleiro.charAt(8));

					int pos;
					int or;
					int mov;
					if (pecas < 3){ // If client can still set pieces
						System.out.println("Indique a posicao da peca: (0 a 8)\n");
						pos = sc.nextInt();
						System.out.println("Indique a orientacao da peca: (0 a 1)\n");
						or = sc.nextInt();

						if(!oponente.equals(chungToi.obtemOponente(id))) {
							System.err.println("Erro fatal!\nEncerrando partida!");
							System.exit(1);
						}
						mov = chungToi.posicionaPeca(id, pos, or);
						if (mov == 1){
							pecas++;
						} else if (mov == 0){
							System.out.println("Posicao ocupada! Tente novamente!");
						} else {
							System.out.println("Posicao invalida! Tente novamente!");
						}
					} else{ // If all pieces have been set and client must move them
						System.out.println("Indique a posicao da peca que deseja deslocar: (0 a 8)\n");
						int posInit = sc.nextInt();
						System.out.println("Indique o sentido em que deseja delocar a peca: (0 a 8)\n");
						int sent = sc.nextInt();
						System.out.println("Indique a distancia que deseja deslocar a peca: (0 a 2)\n");
						int dist = sc.nextInt();
						System.out.println("Indique a orientacao da peca apos movimento: (0 a 1)\n");
						int orNov = sc.nextInt();

						if(!oponente.equals(chungToi.obtemOponente(id))) {
							System.err.println("Erro fatal!\nEncerrando partida!");
							System.exit(1);
						}

						mov = chungToi.movePeca(id, posInit, sent, dist, orNov);
						if (mov == 1){
								pecas++;
						} else if (mov == 0){
								System.out.println("Movimento invalido! Tente novamente!");
						} else {
								System.out.println("Parametros invalidos! Tente novamente!");
						}
					}
				} else if (minhaVez == 2){ // If client has won
					System.out.println("Voce venceu! Parabens!");
					break;
				} else if (minhaVez == 3){ // If client has lost
					System.out.println("Voce perdeu! Melhor sorte da proxima vez!");
					break;
				} else if (minhaVez == 5){ // If client has won by WO
					System.out.println("Voce venceu por walkover!");
					break;
				} else {
					System.err.println("Erro inesperado!\nEncerrando partida!");
					chungToi.encerraPartida(id);
					System.exit(1);
				}
				minhaVez = chungToi.ehMinhaVez(id);
			}

			// End game
			chungToi.encerraPartida(id);

		} catch (Exception e) {
			System.out.println ("ChungClient failed:");
			e.printStackTrace();
		}
	}
}
