package udp.defaultlibrary;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class Receive implements Runnable
{
	//Declare Global Variables
	DatagramSocket receiveSocket;
	DatagramPacket receivePacket, newPacketInfo;
	boolean newPacket = false;
	boolean listen = true;
	int defaultPort = 69;
	//Constructor
	public Receive()
	{
		//create new socket on port defaultPort
		try {
			receiveSocket = new DatagramSocket(defaultPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Run method, calls receiveMessage()
	public void run()
	{
		receiveMessage();
	}
	//Listen for packets
	public void receiveMessage()
	{
		while(listen == true)
		{
			//initialize receiving data
			byte[] receiveData = new byte[5];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try
			{
				//try to receive a packet
				receiveSocket.receive(receivePacket);
				System.out.println("Recieve - received a packet data = |"
						+receivePacket.getData()[0]
						+"|"+receivePacket.getData()[1]
						+"|"+receivePacket.getData()[2]
						+"|"+receivePacket.getData()[3]
						+"|"+receivePacket.getData()[4]+"|");
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				System.out.println("Receive - Failed to receive something from the server");
			}			
			//While there is a packet that has yet to be read by DataTransmission
			while(newPacket);
			//set newPacket to true and newPacketInfo to receivePacket
			setNewPacket(true, receivePacket);	
		}//end listen for packets
		
	}
	//Set newPacket and NewPacketInfo
	public void setNewPacket(boolean temp, DatagramPacket newPack)
	{
		newPacket = temp;
		newPacketInfo = newPack;
	}
	//Return newPacket boolean and if it was true set it to false
	public synchronized boolean getNewPacket()
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
	//return Packet information
	public synchronized DatagramPacket getNewPacketInfo()
	{
		return newPacketInfo;
	}
}//End Receive
