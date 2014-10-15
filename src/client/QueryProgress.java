package client;

/**
 * A simple representation of the progress of a query with the parameters A and
 * B.
 * 
 * @author fredo
 *
 */
public class QueryProgress {

	QueryProgress(int a, int b) {
		this.setA(a);
		this.setB(b);
		this.setDone(false);
	}

	private int a;
	private int b;
	private boolean done;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
}
