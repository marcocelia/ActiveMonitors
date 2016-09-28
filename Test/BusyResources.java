/* 
	Test : 
			1- If a client requires the use of a specific resource, the client is blocked if the requested resource is not available:
				 client2 and client3 waits until client0 and client1 releases RA and RB respectively
			2- If a client requires the use of any one of the two resources, the client is blocked only if none resource is available:
				 client4 waits until one of client0 and client1 releases it's own resource
			3- When a resource is released, the server gives priority to blocked clients waiting for any one of the two resources respect 
			   to blocked clients waiting for the released resource:
				 client4 requests the use of any resource after client2 and client3 but it will get one resource before client2 or client3
*/

class BusyResources{
	public static void main(String[] args){
		final int MAX_THREADS = 5;
		final int RA = 0;
		final int RB = 1;
		final int ANY = 2;
		Client[] threads = new Client[MAX_THREADS];
		Server s = new Server("Server");
		
		threads[0] = new Client("Client", s, RA);
		threads[1] = new Client("Client", s, RB);
		try { Thread.sleep(100); } catch (Exception ex){};
		threads[2] = new Client("Client", s, RA);
		threads[3] = new Client("Client", s, RB);
		try { Thread.sleep(100); } catch (Exception ex){};
		threads[4] = new Client("Client", s, ANY);
		
	}
}