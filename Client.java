public class Client extends Thread{
	private static int NUM_CLIENT = 0;
	private final int MyId;
	private final int res;
	private int received;
	private Server s;
	
	public Client(String name, Server s, int res){
		super(name);
		this.s = s;
		MyId = NUM_CLIENT;
		NUM_CLIENT++;
		this.res = res;
		this.start();
	}
	
	public int getMyId(){
		return MyId;
	}
	
	public void prologue(){
		if (res == 0){
			System.out.println(Thread.currentThread().getName() + ((Client)Thread.currentThread()).getMyId() + " request RA");
			s.RArequest();
			received = s.RAuse();
		} else if (res == 1) {
			System.out.println(Thread.currentThread().getName() + ((Client)Thread.currentThread()).getMyId() + " request RB");
			s.RBrequest();
			received = s.RBuse();
		} else {
			System.out.println(Thread.currentThread().getName() + ((Client)Thread.currentThread()).getMyId() + " request Any");
			s.requestAny();
			received = s.useAny();
		}
	}
	
	public void run(){
		prologue();
		System.out.println(Thread.currentThread().getName() + ((Client)Thread.currentThread()).getMyId() + " using " + ((received == 0)?"RA":"RB"));	
		try { Thread.sleep(3000); } catch (Exception ex){}; /* simulating the use of the resource */
		System.out.println(Thread.currentThread().getName() + ((Client)Thread.currentThread()).getMyId() + " release " + ((received == 0)?"RA":"RB"));	
		s.release(received);
	}	
}
