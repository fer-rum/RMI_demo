package server;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import shared.QueryService;
import shared.QueryState;

/**
 * This is the QueryService that handles the incoming queries.
 * Each query is assigned to its own thread and the threads are handed to an executor
 * who manages them in a CachedThreadPool.
 * 
 * @author Fredo Erxleben
 *
 */
public class ThreadedQueryService implements QueryService {
	
	private ExecutorService executor;
	private Map<Integer, Future<Integer>> futures;
	private Integer lastQueryId;
	
	ThreadedQueryService() throws RemoteException{
		super();
		this.executor = Executors.newCachedThreadPool(); // modify, if needed
		this.lastQueryId = 0;
		this.futures = new HashMap<>();
	}

	@Override
	public int submitQuery(int a, int b) throws RemoteException {
		// create a new thread for the query
		Future<Integer> future = this.executor.submit(new QueryThread(a, b));
		Integer queryId = requestQueryId();
		this.futures.put(queryId, future);
		return queryId;
	}

	@Override
	public QueryState getQueryState(int queryId) throws RemoteException {
		// poll the state of the thread
		Future<Integer> future = this.futures.get((Integer)queryId);
		if(future == null) {
			return QueryState.QUERY_UNKNOWN;
		}
		if(future.isDone()) {
			return QueryState.COMPLETED;
		}
		return QueryState.PENDING;
	}

	@Override
	public int getQueryResult(int queryId) throws RemoteException {
		// transfer the result, if the query is done
		Future<Integer> future = this.futures.get((Integer)queryId);
		if(future != null && future.isDone()) {
			try {
				return future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	private Integer requestQueryId() {
		this.lastQueryId++;
		return lastQueryId;
	}

}
