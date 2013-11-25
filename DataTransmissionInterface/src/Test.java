
public class Test 
{
	static DataTransmission test = new DataTransmission();
	
	public static void main(String[] args) 
	{
		
		System.out.println("Test - DataTransmission Start thread");
		//test.run();
		new Thread(test).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		for(int i = 1;i<9;i++)
		{
			 sendTest(i);
		}
	}
	public static void sendTest(int i)
	{
		test.sendCMD(i);
		System.out.println("Test - Message "+i+" Send cmd "+i);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("*********Test - Message "+i+" Received cmd "+test.getCommand()+"*********");
		
	}
}
