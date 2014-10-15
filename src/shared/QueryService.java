package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface QueryService extends Remote {

	/**
	 * Submits a new query to the server.
	 * 
	 * @param a
	 * @param b
	 * @return an ID which can be used to poll the state of the query or collect
	 *         the results.
	 */
	public int submitQuery(int a, int b) throws RemoteException;

	/**
	 * Polls for the state of a query.
	 * 
	 * @param queryId
	 * @return <i>COMPLETED</i>, <i>PENDING</i> or <i>QUERY_UNKNOWN</i>
	 * @throws RemoteException
	 */
	public QueryState getQueryState(int queryId) throws RemoteException;

	/**
	 * Polls for the result of a given query. Make sure, that the query has
	 * completed beforehand or check the result.
	 * 
	 * @param queryId
	 * @return the result or -1 if the result is not available
	 * @throws RemoteException
	 */
	public int getQueryResult(int queryId) throws RemoteException;
	// the return value thing is a hack… it is not supposed to be used in
	// production

}
