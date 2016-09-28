public class Semaphore{									/* It will be used like a private semaphore, at most one thread can be blocked */
	private int value;
	
	public Semaphore(int value){
		this.value = value;
	}
	public synchronized void P() throws InterruptedException{
		while (value == 0) wait();
		value--;
	}
	public synchronized void V() throws InterruptedException{
		value++;
		notify();															
	}
}