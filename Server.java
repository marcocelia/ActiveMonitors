public class Server extends Thread{
	private EntryVector ev = new EntryVector(4);			/* entry vector for request/release entries */
	private EntryVector nested_ev = new EntryVector(3);		/* nested entry vector for use entries */
	private boolean RAfree;									/* state of the resource RA */
	private boolean RBfree;									/* state of the resource RB */
	private int	RAwaiting;									/* number of clients waiting for the resource RA */
	private int RBwaiting;									/* number of clients waiting for the resource RB */
	private int waitingAny;									/* number of clients waiting for any resource */
	private int allocated;									/* holds the last allocated resource through a request  (RA == 0 ; RB == 1) */
	private int released;									/* holds the last released resource through a release   (RA == 0 ; RB == 1) */
/* Constants definition for each entry */
	private final int RAreq = 0;
	private final int RBreq = 1;
	private final int reqAny = 2;
	private final int release = 3;
	private final int RAuse = 0;
	private final int RBuse = 1;
	private final int useAny = 2;
/*--------------------------------------*/	

	public Server(String name){
		super(name);
		RAfree = true;
		RBfree = true;
		RAwaiting = 0;
		RBwaiting = 0;
		waitingAny = 0;
		allocated = -1;
		this.start();
	}

	public void RArequest(){
		try { 
			ev.entryCall(RAreq); 		
			ev.entryRet(RAreq); 
		} catch (InterruptedException ie){};
	}
	public int RAuse(){
		try { 
			nested_ev.entryCall(RAuse); 
			nested_ev.entryRet(RAuse); 
		} catch (InterruptedException ie){};
		return allocated;
	}
	public void RBrequest(){
		try { 
			ev.entryCall(RBreq); 
			ev.entryRet(RBreq); 
		} catch (InterruptedException ie){};
	}
	public int RBuse(){
		try { 
			nested_ev.entryCall(RBuse); 
			nested_ev.entryRet(RBuse); 
		} catch (InterruptedException ie){};
		return allocated;
	}
	public void requestAny(){
		try { 
			ev.entryCall(reqAny); 	
			ev.entryRet(reqAny); 
		} catch (InterruptedException ie){};	
	}
	public int useAny(){
		try { 
			nested_ev.entryCall(useAny); 	
			nested_ev.entryRet(useAny); 
		} catch (InterruptedException ie){};			
		return allocated;
	}
	public void release(int r){
		try { 
			ev.entryCall(release); 	
			released = r;
			ev.entryRet(release); 
		} catch (InterruptedException ie){};			
	}

	public void run(){
		while (true){
			try{
				int i = ev.acceptAny();						/* accept any request/release */
				
				switch (i){
					case RAreq:								/* received a request for RA */
						if (RAfree){
							RAfree = false;
							allocated = 0;					/* allocation of RA */
							nested_ev.accept(RAuse);
						}else 
							RAwaiting++;
					break;
					case RBreq:								/* received a request for RB */
						if (RBfree){
							RBfree = false;
							allocated = 1;					/* allocation of RB */
							nested_ev.accept(RBuse);		
						}else
							RBwaiting++;
					break;
					case reqAny:							/* received a request for RA or RB */
						if (RAfree || RBfree){
							if (RAfree){					/* if both are free the chosen one it's RA */
								RAfree = false;
								allocated = 0;				/* allocation of RA */
							} else {
								RBfree = false;
								allocated = 1;				/* allocation of RB */
							}	
							nested_ev.accept(useAny);
						}else
							waitingAny++;
					break;
					case release:										/* release of the resource previously allocated */
						if (waitingAny > 0){							/* priority to clients waiting for any resource */
							waitingAny--;
							allocated = released;						/* allocation of the resource just released */
							nested_ev.accept(useAny);
						} else{
							if (released == 0 && RAwaiting > 0){		/* if released == RA and some client it's waiting */
								RAwaiting--;
								allocated = 0;
								nested_ev.accept(RAuse);
							}else if (released == 1 && RBwaiting > 0){	/* if released == RB and some client it's waiting */
								RBwaiting--;
								allocated = 1;
								nested_ev.accept(RBuse);
							}else {										/* if none it's waiting for a resource */
								RAfree = (released == 0)?true:RAfree;
								RBfree = (released == 1)?true:RBfree;
								allocated = -1;
							}
						}
					break;
				}		
			}catch(Exception e){};
		}
	}	
}