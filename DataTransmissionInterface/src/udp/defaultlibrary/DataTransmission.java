package udp.defaultlibrary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class DataTransmission implements Runnable
{
	
	//User Commands
	final static int CMD_ERROR = 0;
	final static int CMD_PLAY = 1;
	final static int CMD_PAUSE = 2;
	final static int CMD_PREVIOUS = 3;
	final static int CMD_NEXT = 4;
	final static int CMD_CLOSE = 5;
	final static int CMD_TEST1 = 6;
	final static int CMD_TEST2 = 7;
	final static int CMD_TEST4 = 8;
	final static int CMD_MOOD = 9;
	
	//Moods
	final static int mood1 = 1;
	final static int mood2 = 2;
	final static int mood3 = 3;
	final static int mood4 = 4;
	final static int mood5 = 5;
	
	//Global variables
	InetAddress guiPi; // 1
	InetAddress playerPi; // 3
	InetAddress serverPi; // 4
	InetAddress ioPi; // 2
	int receivingPort = 68;
	private DatagramPacket newPacketInfo;
	private boolean newPacket = false;
	ArrayList<Integer> command = new ArrayList<Integer>();
	private DatagramSocket sendSocket,receiveSocket;
	private DatagramPacket sendPacket,receivePacket;
	Receive rec;
	Send sen;
	int commandCounter = 0;
	
	//Constructor
	public DataTransmission()
	{
		//initIP();
		inittest();
		init();
	}
	//Initialize Receive and send class objects
	public void init()
	{
		rec = new Receive();
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
		try 
		{
			guiPi= InetAddress.getByName("10.0.0.43");
			playerPi= InetAddress.getByName("10.0.0.42");
			serverPi= InetAddress.getByName("10.0.0.41");
			ioPi= InetAddress.getByName("10.0.0.44");
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
		else if(CMD == CMD_TEST1)
		{
			data[0] = 0;
			data[1] = 1;
			data[2] = 0;
			data[3] = 6;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_TEST2)
		{
			data[0] = 0;
			data[1] = 2;
			data[2] = 0;
			data[3] = 7;
			sendPacket = new DatagramPacket(data, data.length,serverPi, receivingPort);
		}
		else if(CMD == CMD_TEST4)
		{
			data[0] = 0;
			data[1] = 4;
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
		// end commands to player
		
		
		
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
	//checks whether or not there is a new command to get
	public void checkForIncoming()
	{
		while(true)
		{
			if(rec.getNewPacket())
			{
				receivePacket = getPacketInfo();
				setCommand(returnCommand(receivePacket));
				setIsNewPacketFalse();
			}
		}
	}
	//return specific command based on packet data
	public int returnCommand(DatagramPacket temp)
	{
		//compare the bytes vs expected values and return the specified command
		byte[] tempdata = temp.getData();
		if(tempdata[0] == 0)
		{
			if(tempdata[1] == 3)
			{
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
			
			}
			else if(tempdata[3] == 6)
			{
				return CMD_TEST1;
			}
			else if(tempdata[3] == 7)
			{
				return CMD_TEST2;
			}
			else if(tempdata[3] == 8)
			{
				return CMD_TEST4;
			}
			else if(tempdata[1] == 1)
			{
				if(tempdata[3] == 9)
				{
					return CMD_MOOD;
				}
				
			}
			
			
		}
		return 0;
	}
	//add command of paramater num to arraylist
	public void setCommand(int num)
	{
		command.add(num);
	}
	//get command at commandCounter, then increment command counter
	//get command will return a command if there are any, if not it will return 0
	public int getCommand()  
	{
		//checks if there is a command to return
		if(commandCounter<command.size())
		{
			int temp = command.get(commandCounter);
			commandCounter += 1;
			return temp;
		}
		return 0;
	}
	//start thread
	public void run() 
	{
		new Thread(rec).start();
		checkForIncoming();
	}
}//End DataTransmission
