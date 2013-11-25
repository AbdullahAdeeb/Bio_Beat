import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class ServerReceiver 
{
	
	DatagramPacket receivePacket;
	DatagramPacket sendPacket;
	DatagramSocket sendSocket;
	DatagramSocket receiveSocket;
	Send sender = new Send();
	InetAddress guiPi; // 1
	InetAddress playerPi; // 3
	InetAddress serverPi; // 4
	InetAddress ioPi; // 2
	
	public static void main( String args[])
	{
		ServerReceiver reciever = new ServerReceiver();
		Test t = new Test();
		new Thread(t).start();
	    reciever.receiveAndEcho();
	    
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
	public ServerReceiver()
	{
		//initIP();
		inittest();
	      try
	      {
	    	 sendSocket = new DatagramSocket();
	         receiveSocket = new DatagramSocket(68);
	      }
	      catch (SocketException se)
	      {
	         se.printStackTrace();
	         System.exit(1);
	      }
	}
	public void receiveAndEcho()
	{
		while(true)
		{
			byte data[] = new byte[12];
			receivePacket = new DatagramPacket(data, data.length);
			try 
			{
				receiveSocket.receive(receivePacket);
		        data = receivePacket.getData();
	        } 
			catch (IOException e) 
	        {
				e.printStackTrace();
	        	System.exit(1);
			}
			ServerResend sr = new ServerResend(receivePacket);
			new Thread(sr).start();
			
		}
	}
}
