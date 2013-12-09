package serverpi;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

<<<<<<< HEAD
=======
/*******Written By Drew Gascoigne 100827090*******/
>>>>>>> f35d1112730f5ba881bd23c395a8c11683f987da

public class Send
{
	//Global Socket
	DatagramSocket sendSocket;
	//constructor
	public Send()
	{
		//initialize socket on random port
		try 
		{
			sendSocket = new DatagramSocket();
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
	}
	//send a packet
	public void send(DatagramPacket toSend)
	{
	
		System.out.println("Send - sent a packet data = |"
				+toSend.getData()[0]
				+"|"+toSend.getData()[1]
				+"|"+toSend.getData()[2]
				+"|"+toSend.getData()[3]
				+"|"+toSend.getData()[4]+"|");
		try 
		{
			
			sendSocket.send(toSend);
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}	
}//End Send
