package udp.defaultlibrary;
<<<<<<< HEAD:DataTransmissionInterface/src/udp/defaultlibrary/Send.java

=======
>>>>>>> 43001ab9168c3b3d11857a4d4bffa6dae11e5a00:DataTransmissionInterface/src/udp/defaultlibrary/Send.java
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class Send
{
	//Global Socket
	DatagramSocket sendSocket;
	//constructor
	public Send()
	{
		//initialize Socket to random Port
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
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
