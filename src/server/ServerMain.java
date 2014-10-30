package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

import shared.QueryService;

/**
 * This is a server that offers a query interface via RMI.
 * 
 * Launch rmiregistry with
 * rmiregistry -J-Djava.rmi.server.codebase="http://someserver.tld/somepath/shared.jar"&
 * 
 * @author Fredo Erxleben
 *
 */
public class ServerMain {
	
	// TODO shutdown gracefully

	public static void main(String[] args) {
		RemoteServer.setLog(System.out);
		
		// preparations
		if(System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		// set up registry
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			try {
				// none already running, create one
				LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				registry = LocateRegistry.getRegistry();

				// set up query service
				ThreadedQueryService queryService = new ThreadedQueryService();
				QueryService stub = (QueryService) UnicastRemoteObject
						.exportObject(queryService, 1098); 
						// explicitly offering service over port 1098
				registry.rebind("query", stub);
				System.out.println("Server Info: " + queryService);
				System.out.println("Registry list: " + registry.list());
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}

		// the remote queryService will keep the thing alive from here on

	}

}
