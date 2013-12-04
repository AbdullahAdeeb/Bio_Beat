package serverpi;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/*Written By Drew Gascoigne 100827090
 * ****** This java class is a runnable and will be passed a received packet.
 * ****** It then parses through the data and finds the destination ip of the packet
 * ****** Send is called to forward the packet along
 */

public class ServerResend implements Runnable
{
	//Global Variables
	DatagramPacket sendPacket, receivedPacket;
	InetAddress guiPi; // 1
	InetAddress playerPi; // 3
	InetAddress serverPi; // 4
	InetAddress ioPi; // 2
	InetAddress destinationIp;
	int defaultPort = 69;
	//Constructor
	public ServerResend(DatagramPacket receivedPacket)
	{
		this.receivedPacket = receivedPacket;
		initIP();
	}
	//Initialize IPS to their proper IPS
	public void initIP()
	{
		defaultPort = 5000;
		try 
		{
			guiPi = InetAddress.getByName("10.0.0.44");
			playerPi = InetAddress.getByName("10.0.0.43");
			serverPi = InetAddress.getByName("10.0.0.41");
			ioPi = InetAddress.getByName("10.0.0.42");
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
	//runnable method
	public void run() 
	{
		//check IP
		destinationIp = getIP(receivedPacket);
		//initialize sendPacket datagram
		sendPacket = new DatagramPacket(receivedPacket.getData(), receivedPacket.getData().length, destinationIp, defaultPort);
		System.out.println("Send - sent a packet data = |"
				+sendPacket.getData()[0]
				+"|"+sendPacket.getData()[1]
				+"|"+sendPacket.getData()[2]
				+"|"+sendPacket.getData()[3]
				+"|"+sendPacket.getData()[4]+"|");
		//create a new Send object
		Send s = new Send();
		//send the packet
		s.send(sendPacket);
	}
	//Return IP address of the pi board this packet is being sent to
	//This information is taken from the packets DATA
	public InetAddress getIP(DatagramPacket temp)
	{
		if(temp.getData()[1] == 1)
		{
			System.out.println("ServerResend - send to gui");
			return guiPi;
		}
		else if(temp.getData()[1] == 2)
		{
			System.out.println("ServerResend - send to io");
			return ioPi;
		}
		else if(temp.getData()[1] == 3)
		{
			System.out.println("ServerResend - send to player");
			return playerPi;
		}
		else if(temp.getData()[1] == 4)
		{
			System.out.println("ServerResend - send to server");
			return serverPi;
		}
		else
		{
			return null;
		}
	
	}
}//End ServerResend
