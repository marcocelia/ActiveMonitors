import java.lang.Math;

public class EntryVector {
	private Entry[] e_vector;
	private int[] ncall;			/* counts the number of pending request for each entry */
	private int called;				/* counts the number of entries in witch there's at least one pending request */
	
	public EntryVector(int dim){
		e_vector = new Entry[dim];
		called = 0;
		ncall = new int[dim];
		for (int i = 0; i < dim; i++){
			e_vector[i] = new Entry(50);
			ncall[i] = 0;
		}
	}
	
	public void entryCall(int e) throws InterruptedException{
		synchronized (this){
			if (ncall[e] == 0){
				called++;
				notify();
			}
			ncall[e]++;
		}
		e_vector[e].entryCall();
	}
	
	public void entryRet(int e) throws InterruptedException{
		synchronized (this){
			ncall[e]--;
			if (ncall[e] == 0)
				called--;
		}
		e_vector[e].entryRet();
	}
	
	public void accept(int e) throws InterruptedException{
		e_vector[e].accept();
	}
	
	public int acceptAny() throws InterruptedException{
		synchronized (this){
			while (called == 0)		/* the are no request */
				wait();	
		}
		int[] requestedEntry = new int[called];
		int j = 0;											/* first available position in requestedEntry */
		for (int i = 0; i < ncall.length; i++)				/* scan the array ncall */
			if (ncall[i] != 0)								/* if the current entry has some pending request */
				requestedEntry[j++] = i;					/* store the index of the entry in the current available position of requestedEntry */
		int chosen = (int)(called*Math.random());			/* choose a random elements among those in requestedEntry */
		e_vector[requestedEntry[chosen]].accept();			/* call accept in the entry whose index it's contained in the chosen element */
		return requestedEntry[chosen];
	}
}