import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Test implements Runnable
{
	Send sen = new Send();
	DatagramPacket sendPacket;
	int receivingPort = 68;
	static InetAddress serverPi;
	final static int CMD_PLAY = 1;
	final static int CMD_PAUSE = 2;
	final static int CMD_PREVIOUS = 3;
	final static int CMD_NEXT = 4;
	final static int CMD_CLOSE = 5;
	final static int CMD_TEST1= 6;
	final static int CMD_TEST2 = 7;
	
	public void sendCMD(int CMD)
	{
		/*for the gui:
		 * 	1 Play
		 * 	2 Pause
		 * 	3 Previous
		 * 	4 Next
		 * 	5 Close
		 * for the player:
		 * 
		 * 
		 * 
		 * for the server:
		 * 
		 * for the I/O handler:
		 * 
		 * 
		 */
		/*
		 * byte format for messages
		 * data[0] = ack or not, 1 for ack, 0 for cmd
		 * data[1] = which pi board it is being sent to, 1 = gui, 2 = io, 3 = player, 4 = server
		 * data[2] = cmd
		 * data[3] = cmd
		 * data[4] = 0; buffer bit
		 * data[5] = 0; Heart pulse 100 digit
		 * data[6] = 0; Heart pulse 10 digit
		 * data[7] = 0; Heart Pulse 1 digit
		 * data[8] = 0; buffer bit
		 * data[9] = 0; temperature 10 digit
		 * data[10] = 0; temperature 1 digit
		 * data[11] = 0; end bit
		 * System.out.println("DataTransmision - preparing to Send Cmd "+CMD);
		*/
		
		byte[] data = new byte[12];
		data[0] = 0;
		data[1] = 0;
		data[2] = 0;
		data[3] = 0;
		data[4] = 0;
		data[5] = 0;
		data[6] = 0;
		data[7] = 0;
		data[8] = 0;
		data[9] = 0;
		data[10] = 0;
		data[11] = 0;
		sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		//commands to player
		if(CMD == CMD_PLAY)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 1;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_PAUSE)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 2;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_PREVIOUS)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 3;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_NEXT)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 4;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_CLOSE)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 5;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_TEST1)
		{
			data[0] = 0;
			data[1] = 2;
			data[2] = 0;
			data[3] = 6;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_TEST2)
		{
			data[0] = 0;
			data[1] = 1;
			data[2] = 0;
			data[3] = 7;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		// end commands to player
		System.out.println("DataTransmission - packet is being sent data = |"
				+sendPacket.getData()[0]
				+"|"+sendPacket.getData()[1]
				+"|"+sendPacket.getData()[2]
				+"|"+sendPacket.getData()[3]+"|");
		sen.send(sendPacket);
	}

	public void run() 
	{
		try {
			serverPi = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sendCMD(1);
		sendCMD(2);
		sendCMD(3);
		sendCMD(4);
		sendCMD(5);
		sendCMD(6);
		sendCMD(7);
		
	}
}
