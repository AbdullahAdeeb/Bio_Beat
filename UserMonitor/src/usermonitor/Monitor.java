import com.pi4j.io.gpio.*;
import udp.defaultlibrary.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class Monitor{

	//Global variables used by all methods
	DataTransmission send;
	UserData data;
	double temp;
	int beat;

	//Constructor containing a UDP DataTransmission Object
	//and a UserData Object that retreives data via Gertboard
	public Monitor(){

		send = new DataTransmission(null);
		data = new UserData();
	}

	//initialize value of temp
	private void init(){

		double init = 37.0;
		temp = data.getTemp(init);
	}

	//Retreive a new temperature via UserData object
	private double newTemp(double prev){

		return temp = data.getTemp(prev);
	}

	//Retreieve a new heart rate via UserData object
	private int newBeat(){

		return beat = data.getBeat();
	}

	//Send the mood to ServerPi via DataTransmission object
	private void sendMood(int mood){

		send.sendMood(mood);
		System.out.printf("Mood Sent: %d\n", mood);
	}

	//Shutdown UserData object then quit program
	private void shutDown(){

		data.shutDown();
		System.out.print("MON SHUT DOWN\n");
	}

	//Method to determine the mood based on temperature and heart rate
	private int findMood(double temp, int beat){

		int mood = -1;
		/*double maxTemp = 40.0;
		double minTemp = 34.0;
		int maxBeat = 120;
		int minBeat = 40;*/

		//The first set of if/elseifs determine the heart rate
		//if the heart rate is not within the set range then returns a -1, indicating error
		//The nested if/elsifs determine if the temperature is within range and if not returns -1
		if(beat > 110 && beat <= 120){
			if(temp > 39.25 && temp <= 40.00){
				mood = 5;
			}else if(temp > 37.75 && temp <= 39.25){
				mood = 2;
			}else if(temp > 36.50 && temp <= 37.75){
				mood = 4;
			}else if(temp > 35.00 && temp <= 36.50){
				mood = 1;
			}else if(temp > 34.00 && temp <= 35.00){
				mood = 3;
			}else{
				mood = -1;
			}

		}else if(beat > 90 && beat <= 110){
			if(temp > 39.25 && temp <= 40.00){
				mood = 2;
			}else if(temp > 37.75 && temp <= 39.25){
				mood = 5;
			}else if(temp > 36.50 && temp <= 37.75){
				mood = 1;
			}else if(temp > 35.00 && temp <= 36.50){
				mood = 4;
			}else if(temp > 34.00 && temp <= 35.00){
				mood = 3;
			}else{
				mood = -1;
			}

		}else if(beat > 70 && beat <= 90){
			if(temp > 39.25 && temp <= 40.00){
				mood = 5;
			}else if(temp > 37.75 && temp <= 39.25){
				mood = 2;
			}else if(temp > 36.50 && temp <= 37.75){
				mood = 1;
			}else if(temp > 35.00 && temp <= 36.50){
				mood = 4;
			}else if(temp > 34.00 && temp <= 35.00){
				mood = 3;
			}else{
				mood = -1;
			}

		}else if(beat > 50 && beat <= 70){
			if(temp > 39.25 && temp <= 40.00){
				mood = 5;
			}else if(temp > 37.75 && temp <= 39.25){
				mood = 2;
			}else if(temp > 36.50 && temp <= 37.75){
				mood = 4;
			}else if(temp > 35.00 && temp <= 36.50){
				mood = 3;
			}else if(temp > 34.00 && temp <= 35.00){
				mood = 1;
			}else{
				mood = -1;
			}

		}else if(beat >= 40 && beat <= 60){
			if(temp > 39.25 && temp <= 40.00){
				mood = 4;
			}else if(temp > 37.75 && temp <= 39.25){
				mood = 5;
			}else if(temp > 36.50 && temp <= 37.75){
				mood = 3;
			}else if(temp > 35.00 && temp <= 36.50){
				mood = 1;
			}else if(temp > 34.00 && temp <= 35.00){
				mood = 2;
			}else{
				mood = -1;
			}

		}else{
			mood = -1;
		}
		return mood;
	}

	//Main method that runs the program
	public static void main(String argc[]) throws InterruptedException{

		//initialize variables and Monitor object
		Monitor mon = new Monitor();
		mon.init();
		double temp = 0;
		double prev = mon.temp;
		double totTemp = 0;
		int beat = 0;
		int totBeat = 0;
		int count = 0;
		int mood = 0;
		int numSamples = 5;
		while(true){
			//while loop to count the number of valid beats
			while(count < numSamples){
				//gets the beat
				beat = mon.newBeat();
				//if beat is valid then executes the following statements
				if(beat >= 40 && beat <= 120){
					//get a temperature and prints out data and sample number
					temp = mon.newTemp(prev);
					System.out.printf("Sample #: %d\n", count+1);
					System.out.printf("Temp: %.2f\nBeat: %d\n", temp, beat);
					prev = temp;
					//adds the temps and beats to get average later on
					totTemp = totTemp + temp;
					totBeat = totBeat + beat;
					//Thread.sleep(1000);
					count++;
				}
			}
			//once the number of samples is collected the average of beats and temps is calculated
			temp = (double)(totTemp/(count));
			beat = totBeat/(count);
			System.out.printf("Average Data\nTemp: %.2f\nBeat: %d\n", temp, beat);
			mood = mon.findMood(temp, beat);
			mon.sendMood(mood);
			//reset the values before returning into the samples while loops
			totBeat = 0;
			totTemp = 0;
			count = 0;
			mood = 0;
		}
		//mon.shutDown();

	}

	//UserData class that collects the data from the Gertboard
	public class UserData{

		//global pin controller objects
		final GpioController heart;
		final GpioPinDigitalOutput clk;
		final GpioPinDigitalOutput CS;
		final GpioPinDigitalOutput dataOut;
		final GpioPinDigitalInput dataIn;

		public UserData(){

			//create instance of GPIO controller
			heart = GpioFactory.getInstance();
			//initialize the input/output pins
			clk = heart.provisionDigitalOutputPin(RaspiPin.GPIO_14, "clk", PinState.LOW);
			CS = heart.provisionDigitalOutputPin(RaspiPin.GPIO_10, "CS", PinState.LOW);
			dataOut = heart.provisionDigitalOutputPin(RaspiPin.GPIO_12, "Ouput", PinState.LOW);
			dataIn = heart.provisionDigitalInputPin(RaspiPin.GPIO_13, "Input", PinPullResistance.OFF);

		}

		//method to retrieve data from gertboard
		//argument being passed is the channel
		//channel 0 is pulse sensor and channel 1 is temperature sensor
		private int getAnalogData(int channel){

			CS.high();
			clk.low();
			CS.low();

			int cmdOut = channel;
			cmdOut |= 0x18;
			cmdOut <<= 3;

			//for loop to send the command data to the ADC following SPI procedure
			for(int i = 0; i < 6; i++){
				if((cmdOut & 0x80) > 0){
					dataOut.high();
//					System.out.print("Out High\n");
				}else{
					dataOut.low();
//					System.out.print("Out Low\n");
				}

				cmdOut <<= 1;
				clk.high();
				clk.low();
			}

			int dataOut = 0;

			//for loop to collect 12 bits of data from ADC using SPI procedure
			for(int k = 0; k < 12; k++){
				clk.high();
				clk.low();

				//shifts dataOut 1-bit left to check the each of thr 12 bits
				dataOut <<= 1;

				//check pin state of input
				//if high, masks with 1 to set the dataOut bit high
				PinState input = dataIn.getState();
				if(input == PinState.HIGH){
//					System.out.print("High Input\n");
					dataOut |= 0x1;
				}else{
//					System.out.print("Low Input\n");
				}
			}

			CS.high();
			//shift right to eliminate 12th bit - contains no data
			dataOut >>>= 1;
			//masks remaining 11 bits with 1111111111 so only the required 10 bits of data remain
			dataOut &= 0x3FF;

			return dataOut;
		}

		//method that uses getAnalogData to calculate the current heart rate
		public int getBeat(){

			int count = 0;
			int heart = 0;
			boolean pulse = false;

			//finds the time required to count 5 beats
			long start = System.currentTimeMillis();
			while(count < 5){
				heart = getAnalogData(0);
				//System.out.print("Beat Data: " + heart + "\n");
				//if data input is greater than 511 bits, which is 1.65 volts, the recognizes a pulse
				//if the previous input was also a pule it does not detect a pulse but rather
				//a pulse is only recognized if the previous input was less then 1.65 volts or 511 bits
				if(heart > 511){
					if(pulse == false){
						pulse = true;
//						System.out.print("-------------------Beat\n");
						count++;
					}else{
//						System.out.print("------\n");
					}
				}else{
					pulse = false;
//					System.out.print("\n");
				}
				try{
					Thread.sleep(100);
//					System.out.print("SLEEP SUCCESS");
				}catch(InterruptedException except){
					System.out.print("SLEEP FAIL\n");
				}
			}
			long end = System.currentTimeMillis();
			long diff = end - start;
			//this conversion was found bcause heart rate is in beats per minute
			//the time found is in milli seconds
			//there are 60,000 milliseconds per minute and there are 5 beats counted
			//therefore (5*60000)/diff
			int rate = (int)(300000/diff);
			return rate;
		}

		//method to get temperature orginally using getAnalogData
		//now generates random number and requires the previous temperature to set the new range of the new numbers
		public double getTemp(double prev){

			//Our temperature was ruined during testing on Sunday and therefore create a pseaudo temp

			//(input - 500)/10
			/*int count = 0;
			int ave = 0;
			int temp = 0;
			while(count < 10){
				temp = getAnalogData(1);
				System.out.print("Temp Data: " + temp + "\n");
				temp = (temp-500)/10;
				System.out.print("Temp Cel: " + temp + "\n");
				ave = ave + temp;
				Thread.sleep(200);
				count++;
			}
			ave = ave/10;*/

			//this is where the random temperature within the set range is set
			double temp = 0;
			double minTemp = 35.5;
			double maxTemp = 38.5;
			double min = prev - 0.2;
			double max = prev + 0.2;
			Random rand = new Random();
			temp = min + (max - min) * rand.nextDouble();
			if(temp <= maxTemp && temp >= minTemp){
				return temp;
			}

			return getTemp(prev);
		}

		//shutdown method to close the GPIO Controller
		public void shutDown(){

			heart.shutdown();
			System.out.print("USER DATA SHUT DOWN\n");
			System.exit(0);
		}

	}
}
