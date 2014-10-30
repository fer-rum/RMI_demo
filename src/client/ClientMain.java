package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import shared.QueryService;
import shared.QueryState;

/**
 * The main class of the RMI-client.
 * Submit the target host as args[0] (e.g. hostname.domain.tld)
 * Only provide a port if you do not use the default 1099.
 * 
 * @author fredo
 *
 */
public class ClientMain {

	public static void main(String[] args) {

		HashMap<Integer, QueryProgress> submittedQueries = new HashMap<>();

		try {
			String uri = args[0];
			Registry registry = LocateRegistry.getRegistry(uri);
			
			System.out.println(registry.list());
			QueryService queryService = (QueryService) registry.lookup("query");
			System.out.println("Got server " + queryService);

			// submit a bunch of queries

			for (int a = 0; a < 4; a++) {
				for (int b = 0; b < 13; b++) {
					int id = queryService.submitQuery(a, b);
					submittedQueries.put((Integer) id, new QueryProgress(a, b));
				}
			}

			// check if queries completed
			while (!submittedQueries.isEmpty()) {
				Thread.sleep(1000);
				LinkedList<Integer> toDelete = new LinkedList<>();
				for (Entry<Integer, QueryProgress> entry : submittedQueries
						.entrySet()) {
					QueryState state = queryService.getQueryState(entry
							.getKey());
					switch (state) {
					case COMPLETED:
						entry.getValue().setDone(true);
						int result = queryService.getQueryResult(entry.getKey());
						System.out.println("Query " + entry.getKey() + " finished: ack(" + entry.getValue().getA() + ", " + entry.getValue().getB() + ") = " + result);
						toDelete.add(entry.getKey());
						break;
					case QUERY_UNKNOWN:
						toDelete.add((Integer) entry.getKey());
						System.err.println("Query " + entry.getKey()
								+ " is unknown!");
						break;
					default:
						break;
					}
				}
				// cleanup
				for(Integer i: toDelete) {
					submittedQueries.remove(i);
				}
				
			}

		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
