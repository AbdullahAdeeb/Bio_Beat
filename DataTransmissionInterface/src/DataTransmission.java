import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class DataTransmission implements Runnable
{
	private DatagramSocket sendSocket,receiveSocket;
	private DatagramPacket sendPacket,receivePacket;
	Receive rec;
	Send sen;
	final static int CMD_PLAY = 1;
	final static int CMD_PAUSE = 2;
	final static int CMD_PREVIOUS = 3;
	final static int CMD_NEXT = 4;
	final static int CMD_CLOSE = 5;
	final static int CMD_TEST1 = 6;
	final static int CMD_TEST2 = 7;
	final static int CMD_TEST4 = 8;
	InetAddress guiPi; // 1
	InetAddress playerPi; // 3
	InetAddress serverPi; // 4
	InetAddress ioPi; // 2
	int receivingPort = 68;
	private DatagramPacket newPacketInfo;
	private boolean newPacket = false;
	private int command = 0;
	
	public DataTransmission()
	{
		//initSocket();
		//initIP();
		inittest();
		init();
	}
	public void init()
	{
		rec = new Receive();
		sen = new Send();
	
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
	public void initSocket()
	{
	/*	try 
		{
			//sendSocket = new DatagramSocket();
			//receiveSocket = new DatagramSocket(68);
		} 
		catch (SocketException e) 
		{
			System.out.println("Failed to create a socket, the program will terminate");
			System.exit(1);
		}
		*/
		
	}//end init
	public void sendCMD(int CMD)
	{
		/*for the gui:
		 * 	1 Play
		 * 	2 Pause
		 * 	3 Previous
		 * 	4 Next
		 * 	5 Close
		 * for the player:
		 * 
		 * 
		 * 
		 * for the server:
		 * 
		 * for the I/O handler:
		 * 
		 * 
		 */
		/*
		 * byte format for messages
		 * data[0] = ack or not, 1 for ack, 0 for cmd
		 * data[1] = which pi board it is being sent to, 1 = gui, 2 = io, 3 = player, 4 = server
		 * data[2] = cmd
		 * data[3] = cmd
		 * data[4] = 0; buffer bit
		 * data[5] = 0; Heart pulse 100 digit
		 * data[6] = 0; Heart pulse 10 digit
		 * data[7] = 0; Heart Pulse 1 digit
		 * data[8] = 0; buffer bit
		 * data[9] = 0; temperature 10 digit
		 * data[10] = 0; temperature 1 digit
		 * data[11] = 0; end bit
		 * System.out.println("DataTransmision - preparing to Send Cmd "+CMD);
		*/
		
		byte[] data = new byte[12];
		data[0] = 0;
		data[1] = 0;
		data[2] = 0;
		data[3] = 0;
		data[4] = 0;
		data[5] = 0;
		data[6] = 0;
		data[7] = 0;
		data[8] = 0;
		data[9] = 0;
		data[10] = 0;
		data[11] = 0;
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
		// end commands to player
		System.out.println("DataTransmission - packet is being sent data = |"
				+sendPacket.getData()[0]
				+"|"+sendPacket.getData()[1]
				+"|"+sendPacket.getData()[2]
				+"|"+sendPacket.getData()[3]+"|");
		sen.send(sendPacket);
	}
	public boolean isNewPacket()
	{
		return newPacket;
	}
	public void setNewPacketInfo()
	{
		rec.getNewPacketInfo();
	}
	public void setIsNewPacketFalse()
	{
		//System.out.println("DataTransmission - Set New Packet False");
		newPacket = false;
	}
	public DatagramPacket getPacketInfo()
	{
		newPacketInfo = rec.getNewPacketInfo();
		return newPacketInfo;
	}
	public void checkForIncoming()
	{
		while(true)
		{
		//	System.out.println("waiting for a packet");
			if(rec.getNewPacket())
			{
				receivePacket = getPacketInfo();
				//System.out.println("DataTransmission - Trying to set a New Command");
				while(command!=0)
				{
					/*try 
					{
						Thread.sleep(100);
						
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}*/
				}
				//System.out.println("DataTransmission - New Command Set");
				setCommand(returnCommand(receivePacket));
				setIsNewPacketFalse();
			}
		}
	}
	public int returnCommand(DatagramPacket temp)
	{
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
			
		}
		return 0;
	}
	public void setCommand(int num)
	{
		command = num;
	}
	public int getCommand()  
	{
		int temp = command;
		setCommand(0);
		return temp;
	}
	public void run() 
	{
		new Thread(rec).start();
		//	rec.run();
		//System.out.println("DataTransmission - Start Receive thread");
		//System.out.println("DataTransmission - Calling checkForIncoming");
		checkForIncoming();
	}
	

}
