package udp.datatransmission;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class DataTransmission
{
	
	//User Commands
	public final static int CMD_ERROR = 0;
	public final static int CMD_PLAY = 1;
	public final static int CMD_PAUSE = 2;
	public final static int CMD_PREVIOUS = 3;
	public final static int CMD_NEXT = 4;
	public final static int CMD_CLOSE = 5;
	public final static int CMD_VOLINC = 6;
	public final static int CMD_VOLDEC = 7;
	public final static int CMD_STOP = 8;
	public final static int CMD_MOOD = 9;
	
	//Moods
	public final static int MOOD_RELAXED = 1;
	public final static int MOOD_EXCITED = 2;
	public final static int MOOD_SAD = 3;
	public final static int MOOD_HAPPY = 4;
	public final static int MOOD_ANGRY = 5;
	
	//Global variables
	InetAddress guiPi; // 1
	InetAddress playerPi; // 3
	InetAddress serverPi; // 4
	InetAddress ioPi; // 2
	int receivingPort = 70;
	private DatagramPacket newPacketInfo;
	private boolean newPacket = false;
	private DatagramSocket sendSocket,receiveSocket;
	private DatagramPacket sendPacket,receivePacket;
	Receive rec;
	Send sen;
	ActionListener al;

	//Constructor
	public DataTransmission(ActionListener al)
	{
		this.al = al;

		initIP();
	
		init();

		
	}
	//Initialize Receive and send class objects
	public void init()
	{
		rec = new Receive(al);
	//	rec.run();
		new Thread(rec).start();
		sen = new Send();
	}
	//initialize IPs as local host for testing
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
	//initialize proper IP addresses 
	public void initIP()
	{
		receivingPort = 5000;
		try 
		{
			guiPi= InetAddress.getByName("10.0.0.44");
			playerPi= InetAddress.getByName("10.0.0.43");
			serverPi= InetAddress.getByName("10.0.0.41");
			ioPi= InetAddress.getByName("10.0.0.42");
		}
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
			
	}
	//Send a UDP message based on the cmd passed into the method
	public void sendCMD(int CMD)
	{
		/*for the gui:
		 * 	1 Play
		 * 	2 Pause
		 * 	3 Previous
		 * 	4 Next
		 * 	5 Close
		 * for the player:
		 * for the server:
		 * for the I/O handler:
		 */
		/*
		 * byte format for messages
		 * data[0] = ack or not, 1 for ack, 0 for cmd
		 * data[1] = which pi board it is being sent to, 1 = gui, 2 = io, 3 = player, 4 = server
		 * data[2] = cmd
		 * data[3] = cmd
		 * data[4] = mood
		 * System.out.println("DataTransmision - preparing to Send Cmd "+CMD);
		*/
		//set Up byte DATA
		System.out.println("start of send");
		byte[] data = new byte[5];
		data[0] = 0;
		data[1] = 0;
		data[2] = 0;
		data[3] = 0;
		data[4] = 0;
		//initialize the Packet
		sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		//commands to player
		if(CMD == CMD_PLAY)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 1;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_PAUSE)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 2;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_PREVIOUS)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 3;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_NEXT)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 4;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_CLOSE)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 5;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_VOLINC)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 6;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_VOLDEC)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 7;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_STOP)
		{
			data[0] = 0;
			data[1] = 3;
			data[2] = 0;
			data[3] = 8;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_MOOD)
		{
			data[0] = 0;
			data[1] = 1;
			data[2] = 0;
			data[3] = 9;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		} 
		else
		{
			return;
		}
		// end commands to player
		System.out.println("Got command");
		
		
		//send Packet
		System.out.println("DataTransmission - packet is being sent data = |"
				+sendPacket.getData()[0]
				+"|"+sendPacket.getData()[1]
				+"|"+sendPacket.getData()[2]
				+"|"+sendPacket.getData()[3]
				+"|"+sendPacket.getData()[4]+"|");
		sen.send(sendPacket);
	}
	//Send a UDP message based on the mood passed into the method
	public void sendMood(int mood)
	{
		//set Up byte DATA
		byte[] data = new byte[5];
		data[0] = 0;
		data[1] = 0;
		data[2] = 0;
		data[3] = 0;
		data[4] = 0;
		//initialize the Packet
		sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		//commands to player
		if(mood == MOOD_RELAXED)
		{
			data[0] = 0;
			data[1] = 1;
			data[2] = 0;
			data[3] = 9;
			data[4] = MOOD_RELAXED;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(mood == MOOD_EXCITED)
		{
			data[0] = 0;
			data[1] = 1;
			data[2] = 0;
			data[3] = 9;
			data[4] = MOOD_EXCITED;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(mood == MOOD_SAD)
		{
			data[0] = 0;
			data[1] = 1;
			data[2] = 0;
			data[3] = 9;
			data[4] = MOOD_SAD;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(mood == MOOD_HAPPY)
		{
			data[0] = 0;
			data[1] = 1;
			data[2] = 0;
			data[3] = 9;
			data[4] = MOOD_HAPPY;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(mood == MOOD_ANGRY)
		{
			data[0] = 0;
			data[1] = 1;
			data[2] = 0;
			data[3] = 9;
			data[4] = MOOD_ANGRY;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else
		{
			return;
		}
		//end mood selection
		
		
		//send Packet
		System.out.println("DataTransmission - packet is being sent data = |"
				+sendPacket.getData()[0]
				+"|"+sendPacket.getData()[1]
				+"|"+sendPacket.getData()[2]
				+"|"+sendPacket.getData()[3]
				+"|"+sendPacket.getData()[4]+"|");
		sen.send(sendPacket);
	}
	//return newPacket
	public boolean isNewPacket()
	{
		return newPacket;
	}
	//check whether or not there is new packet info to get
	public void setNewPacketInfo()
	{
		rec.getNewPacketInfo();
	}
	//set newPacket to false
	public void setIsNewPacketFalse()
	{
		//System.out.println("DataTransmission - Set New Packet False");
		newPacket = false;
	}
	//Returns the packet that was received
	public DatagramPacket getPacketInfo()
	{
		newPacketInfo = rec.getNewPacketInfo();
		return newPacketInfo;
	}
	//return specific command based on packet data
	public static int returnCommand(DatagramPacket temp)
	{
		//compare the bytes vs expected values and return the specified command
		byte[] tempdata = temp.getData();
		//Start of data parser
		if(tempdata[3] == 1)
		{
			return CMD_PLAY;
		}
		else if(tempdata[3] == 2)
		{
			return CMD_PAUSE;
		}
		else if(tempdata[3] == 3)
		{
			return CMD_PREVIOUS;
		}
		else if(tempdata[3] == 4)
		{
			return CMD_NEXT;
		}
		else if(tempdata[3] == 5)
		{
			return CMD_CLOSE;
		}
		else if(tempdata[3] == 6)
		{
			return CMD_VOLINC;
		}
		else if(tempdata[3] == 7)
		{
			return CMD_VOLDEC;
		}
		else if(tempdata[3] == 8)
		{
			return CMD_STOP;
		}
		else if(tempdata[3] == 9)
		{
			return CMD_MOOD;
		}
		// end data parser
		return 0;
	}
	//get command will return a command if there are any, if not it will return 0
	public static int getMood(DatagramPacket temp)
	{
		return temp.getData()[4];
		
	}
	//start thread
	
}//End DataTransmission
	
