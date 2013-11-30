import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class ServerReceiver 
{
	//Declare Global Variables
	Send sender = new Send();
	DatagramPacket receivePacket, sendPacket;
	DatagramSocket sendSocket, receiveSocket;
	InetAddress guiPi; // 1
	InetAddress playerPi; // 3
	InetAddress serverPi; // 4
	InetAddress ioPi; // 2
	int defaultPort = 68;
	
	//Main
	public static void main( String args[])
	{
		//Start a serverReceiver
		ServerReceiver reciever = new ServerReceiver();
	    reciever.receiveAndEcho(); 
	}
	//Initialize IPS to their proper IPS
	public void initIP()
	{
		try 
		{
			guiPi = InetAddress.getByName("10.0.0.43");
			playerPi = InetAddress.getByName("10.0.0.42");
			serverPi = InetAddress.getByName("10.0.0.41");
			ioPi = InetAddress.getByName("10.0.0.44");
		}
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
			
	}
	//initialize IPS to local address for testing
	public void inittest()
	{
		try 
		{
			guiPi = InetAddress.getLocalHost();
			playerPi= InetAddress.getLocalHost();
			serverPi= InetAddress.getLocalHost();
			ioPi= InetAddress.getLocalHost();
		}
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
			
	}
	//constructor
	public ServerReceiver()
	{
		//initIP();
		inittest();
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
