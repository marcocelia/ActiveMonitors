import java.lang.Math;
/*
	Generic program that random creates new client 
*/
class Example{
	
	public static int random(int inf, int sup){
		int r = sup - inf + 1;
		return (int)(r*Math.random())+inf;
	}
	
	public static void main(String[] args){
		final int RA = 0;
		final int RB = 1;
		final int ANY = 2;
		Server s = new Server("Server");
		int i = 0;
		
		while(i++ < 3){
			int num_client = random(1,4);
			int sleep_time = random(1,9)*100;
			System.out.println("Created " + num_client + " clients");
			for (int j = 0; j < num_client; j++)
				new Client("Client", s, random(0,2));
			try{ Thread.sleep(sleep_time); } catch (Exception e){};
		}
	}
}