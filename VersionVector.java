public class VersionVector{
	private int idNode;
	private int counter;
	
	public VersionVector (int mId, int mCounter) {
		idNode = mId;
		counter = mCounter;
	}
	public int getId() {
		return idNode;
	}
	public int getCounter() {
		return counter;
	}
	public void setId(int mId) {
		idNode = mId;
	}
	public void setCounter(int mCounter) {
		counter = mCounter;
	}
	public void incrementCounter() {
		counter++;
	}
}