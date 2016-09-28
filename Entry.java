public class Entry{
	private final int NUM_THREAD;
	private Semaphore[] sem;				/* private semaphores */
	private boolean free;					/* state of the entry */
//QUEUE OF BLOCKED THREAD HANDLING:
	private int[] blocked;
	private int front;
	private int rear;
	private int waiting;					/* number of waiting clients for the entry */
//REDENZVOUS:
	private Semaphore server_sync;			/* implements the redenz-vous barrier between client and server */
	private Semaphore client_sync;			/* implements the redenz-vous barrier between client and server */
	private Semaphore entryRet;				/* synchronization semaphore to notify the server the call of an entryRet */
		
	public Entry(int n){
		NUM_THREAD = n;
		sem = new Semaphore[NUM_THREAD];
		blocked = new int[NUM_THREAD];
		front = 0;
		rear = 0;
		waiting = 0;
		free = true;
		for (int i = 0; i < NUM_THREAD; i++)
			sem[i] = new Semaphore(0);
		server_sync = new Semaphore(0);
		client_sync = new Semaphore(0);
		entryRet = new Semaphore(0);
	}
	
	public void entryCall() throws InterruptedException{
		int id = ((Client)Thread.currentThread()).getMyId();
		synchronized(this){
			if (free){
				free = false;
				sem[id].V();
			}
			else{
				blocked[rear] = id;
				rear = (rear + 1)%NUM_THREAD;
				waiting++;
			}
		}/* avoid nested monitor calls */
		sem[id].P();/* blocking operation only if free initially false */
		client_sync.V();
		server_sync.P();
	}
	
	public void accept() throws InterruptedException{
		server_sync.V();
		client_sync.P();
		entryRet.P();
		synchronized(this){
			if (waiting > 0){
				int id = blocked[front];
				front = (front + 1)%NUM_THREAD;
				free = false;
				waiting--;
				sem[id].V();
			}
		}
	}
	
	public synchronized void entryRet() throws InterruptedException{
		free = true;
		entryRet.V();
	}
}