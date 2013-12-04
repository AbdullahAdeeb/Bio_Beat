package serverpi;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class ServerReceiver 
{
	//Declare Global Variables
	Send sender = new Send();
	DatagramPacket receivePacket, sendPacket;
	DatagramSocket sendSocket, receiveSocket;
	int defaultPort = 5000;
	
	//Main
	public static void main( String args[])
	{
		//Start a serverReceiver
		ServerReceiver reciever = new ServerReceiver();
	    reciever.receiveAndEcho(); 
	}

	
	//constructor
	public ServerReceiver()
	{
	    try
	    {
	       sendSocket = new DatagramSocket();
	       receiveSocket = new DatagramSocket(defaultPort);
	    }
	    catch (SocketException se)
	    {
	       se.printStackTrace();
	       System.exit(1);
	    }
	}
	//listen for packets
	public void receiveAndEcho()
	{
		while(true)
		{
			byte data[] = new byte[5];
			receivePacket = new DatagramPacket(data, data.length);
			try 
			{
				//receive a packet
				receiveSocket.receive(receivePacket);
		        data = receivePacket.getData();
		        System.out.println("ServerReceiver - received a packet data = |"
						+receivePacket.getData()[0]
						+"|"+receivePacket.getData()[1]
						+"|"+receivePacket.getData()[2]
						+"|"+receivePacket.getData()[3]
						+"|"+receivePacket.getData()[4]+"|");
	        } 
			catch (IOException e) 
	        {
				e.printStackTrace();
	        	System.exit(1);
			}
			//Forward packet along
			ServerResend sr = new ServerResend(receivePacket);
			new Thread(sr).start();	
		}
	}
}//End ServerReceiver
