public class Controller implements Runnable{
	private int[] deletionBuffer;
	private Thread thread;
	private int id_thread;
	private CRDT collaboration_text;
	
	Controller(int threadid, int[] buff, CRDT collab_text){
		id_thread = threadid;
		for (int buffElement: buff)
		{
		    deletionBuffer.append(buffElement);
		}
		collaboration_text = collab_text;
	}
	
	public void getCRDT() {
		return collaboration_text;
	}
	
	public void getDeletionBuffer() {
		return deletionBuffer;
	}
	
	public void setCRDT(CRDT collab_text) {
		collaboration_text = collab_text;
	}
	
	public void setDeletionBuffer(int[] buff) {
		for (int buffElement: buff)
		{
		    deletionBuffer.append(buffElement);
		}
	}
	
	public void run() {
		System.out.println("Running " +  threadName );
		try {
			for(int i = 4; i > 0; i--) {
				System.out.println("Thread: " + threadName + ", " + i);
				// Let the thread sleep for a while.
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			System.out.println("Thread " +  threadName + " interrupted.");
		}
		System.out.println("Thread " +  threadName + " exiting.");
	}
	   
	public void start () {
		if (thread == null) {
			thread = new Thread (this, threadName);
			thread.start ();
		}
	}
		
	public insertCRDT(int node, char add_char, int index) {
		if(thread_id == node) {
			collaboration_text.getString().Insert1(add_char, index);
		}
	}
	public insertCRDT(int node, char add_char, int index, CRDT collaboration_text) {
		collaboration_text.getString().Insert1(add_char, index);
	}
}