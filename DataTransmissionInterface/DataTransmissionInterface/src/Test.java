
public class Test 
{
	public static void main(String[] args) 
	{
		DataTransmission test = new DataTransmission();
		System.out.println("Test - DataTransmission Start thread");
		//test.run();
		new Thread(test).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

		test.sendCMD(1);
		System.out.println("Test - Message1 Send cmd 1");


	
		
		
		test.sendCMD(2);
		System.out.println("Test - Message2 Send cmd 2");


		
		test.sendCMD(3);
		System.out.println("Test - Message3 Send cmd 3");

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		System.out.println("Test - Message1 Received cmd "+test.getCommand());

		System.out.println("Test - Messag2 Received cmd "+test.getCommand());
		System.out.println("Test - Message3 Received cmd "+test.getCommand());
	}
}
