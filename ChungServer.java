/**
 * @file ChungServer.java
 * @author Roland Teodorowitsch and Bernardo Scapini Consoli
 * @date October 2017
 * 
 * @section DESCRIPTION
 * 
 * Modification of an RMI server provided by Roland Teodorowitsch
 * 
 */

import java.rmi.Naming;
import java.rmi.RemoteException;

public class ChungServer {

	public static void main(String[] args) {
		if	(args.length != 1) {
			System.err.println("ChungServer <server host>\n  ERRO: Nome de dominio ou IP nao fornecido!");
			System.exit(1);
		}
		System.setProperty("java.rmi.server.hostname",args[0]);
		System.setProperty("java.security.policy","ChungServer.policy");
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");
		} catch (RemoteException e) {
			System.out.println("RMI registry already running.");
		}
		try {
			Naming.rebind ("ChungToi", new ChungImpl ());
			System.out.println ("ChungServer is ready.");
		} catch (Exception e) {
			System.out.println ("ChungServer failed:");
			e.printStackTrace();
		}
	}

}
