import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class Receive implements Runnable
{
	DatagramSocket receiveSocket;
	DatagramPacket receivePacket;
	boolean newPacket = false;
	DatagramPacket newPacketInfo;
	boolean listen = true;
	
	public Receive()
	{
		try {
			receiveSocket = new DatagramSocket(68);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run()
	{
		receiveMessage();
	}
	public void receiveMessage()
	{
		while(listen == true)
		{
			byte[] receiveData = new byte[12];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try
			{
				//System.out.println("Receive - waiting for a packet");
				receiveSocket.receive(receivePacket);
				System.out.println("Recieve - received a packet data = |"
						+receivePacket.getData()[0]
						+"|"+receivePacket.getData()[1]
						+"|"+receivePacket.getData()[2]
						+"|"+receivePacket.getData()[3]+"|");
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				System.out.println("Receive - Failed to receive something from the server");
			}			
			//System.out.println("Receive - waiting for last command to resolve");
			while(newPacket)
			{
				try
				{
					
					Thread.sleep(250);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			
			//System.out.println("Receive - Flagging boolean in Data Transmission and setting packet info");
			setNewPacket(true, receivePacket);
			
		}
		
	}
	public void setNewPacket(boolean temp, DatagramPacket newPack)
	{
		newPacket = temp;
		newPacketInfo = newPack;
	}
	public boolean getNewPacket()
	{
		if(newPacket == true)
		{
			newPacket = false;
			return true;
		}
		else
		{
			return false;
		}
	}
	public DatagramPacket getNewPacketInfo()
	{
		//System.out.println("Receive - Returning Packet Info");
		return newPacketInfo;
	}
}
