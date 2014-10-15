package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import shared.QueryService;

/**
 * This is a server that offers a query interface via RMI.
 * @author Fredo Erxleben
 *
 */
public class ServerMain {
	
	// TODO shutdown gracefully

	public static void main(String[] args) {

		// preparations
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
						.exportObject(queryService, 0);

				registry.rebind("query", stub);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}

		// the remote queryService will keep the thing alive from here on

	}

}
