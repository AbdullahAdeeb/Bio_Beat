import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerResend implements Runnable
{
	DatagramPacket sendPacket, receivedPacket;
	InetAddress guiPi; // 1
	InetAddress playerPi; // 3
	InetAddress serverPi; // 4
	InetAddress ioPi; // 2
	InetAddress destinationIp;
	int defaultPort = 69;
	public ServerResend(DatagramPacket receivedPacket)
	{
		this.receivedPacket = receivedPacket;
		inittest();
	}
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
	public void run() 
	{
		destinationIp = getIP(receivedPacket);
		sendPacket = new DatagramPacket(receivedPacket.getData(), receivedPacket.getData().length, destinationIp, defaultPort);
		System.out.println("ServerResend - packet is being sent data = |"
				+sendPacket.getData()[0]
				+"|"+sendPacket.getData()[1]
				+"|"+sendPacket.getData()[2]
				+"|"+sendPacket.getData()[3]+"|");
		Send s = new Send();
		s.send(sendPacket);
	}
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

}
