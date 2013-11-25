import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class Send
{
	DatagramSocket sendSocket;
	
	public Send()
	{
		
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void send(DatagramPacket toSend)
	{
		//System.out.println("Send - trying to send message");
		System.out.println("Send - sent a packet data = |"
				+toSend.getData()[0]
				+"|"+toSend.getData()[1]
				+"|"+toSend.getData()[2]
				+"|"+toSend.getData()[3]+"|");
		try 
		{
			
			sendSocket.send(toSend);
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
}
