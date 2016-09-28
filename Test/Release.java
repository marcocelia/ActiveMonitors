/* 
	Test all the branches of the case 'Release' of the server
*/

class Release{
	public static void main(String[] args){
		final int MAX_THREADS = 7;
		final int RA = 0;
		final int RB = 1;
		final int ANY = 2;
		Client[] threads = new Client[MAX_THREADS];
		Server s = new Server("Server");
		
		threads[0] = new Client("Client", s, ANY);
		threads[1] = new Client("Client", s, ANY);
		try { Thread.sleep(100); } catch (Exception ex){};
		threads[2] = new Client("Client", s, RA);
		threads[3] = new Client("Client", s, RB);
		try { Thread.sleep(100); } catch (Exception ex){};
		threads[4] = new Client("Client", s, ANY);
	}
}