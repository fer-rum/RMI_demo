package server;

import java.util.concurrent.Callable;

/**
 * This is a worker thread that gets called to handle an incoming query. It
 * takes two arguments <i>a</i>, <i>b</i> and calculates the modified Ackermann
 * function <i>ack(a,b)</i>.
 * 
 * @author Fredo Erxleben
 *
 */
public class QueryThread implements Callable<Integer> {

	private int a, b;

	QueryThread(int a, int b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public Integer call() throws Exception {
		return ack(a, b);
	}

	private int ack(int a, int b) {
		if (a == 0)
			return b + 1;
		if (b == 0)
			return this.ack(a - 1, 1);
		return this.ack(a - 1, this.ack(a, b - 1));
	}

}
