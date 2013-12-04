import com.pi4j.io.gpio.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class Monitor(

	

public class UserData{

	final GpioController heart;
	final GpioPinDigitalOutput clk;
	final GpioPinDigitalOutput CS;
	final GpioPinDigitalOutput dataOut;
	final GpioPinDigitalInput dataIn;
	//final GpioPinDigitalInput tempIn;

	public UserData(){

		//create instance of IO controller
		heart = GpioFactory.getInstance();

		clk = heart.provisionDigitalOutputPin(RaspiPin.GPIO_14, "clk", PinState.LOW);
		CS = heart.provisionDigitalOutputPin(RaspiPin.GPIO_10, "CS", PinState.LOW);
		dataOut = heart.provisionDigitalOutputPin(RaspiPin.GPIO_12, "Ouput", PinState.LOW);
		dataIn = heart.provisionDigitalInputPin(RaspiPin.GPIO_13, "Input", PinPullResistance.OFF);
	//	tempIn = heart.provisionDigitalInputPin(RaspiPin.GPIO_07, "Temp", PinPullResistance.OFF);

	}

	private int getAnalogData(int channel){

		CS.high();
		clk.low();
		CS.low();

		int cmdOut = channel;
		cmdOut |= 0x18;
		cmdOut <<= 3;
//		System.out.print("CMD OUT: " + cmdOut + "\n");

		for(int i = 0; i < 6; i++){
			if((cmdOut & 0x80) > 0){
				dataOut.high();
//				System.out.print("Out High\n");
			}else{
				dataOut.low();
//				System.out.print("Out Low\n");
			}

			cmdOut <<= 1;
			clk.high();
			clk.low();
		}

		int dataOut = 0;

		for(int k = 0; k < 12; k++){
			clk.high();
			clk.low();

			dataOut <<= 1;
//			System.out.print(dataOut + "\n");
			PinState input = dataIn.getState();
			if(input == PinState.HIGH){
//				System.out.print("High Input\n");
				dataOut |= 0x1;
			}else{
//				System.out.print("Low Input\n");
			}
		}

		CS.high();
//		System.out.print("DataOut: " + dataOut + "\n");
		dataOut >>>= 1;
//		System.out.print("DataOut: " + dataOut + "\n");
		dataOut &= 0x3FF;
//		System.out.print("DataOut: " + dataOut + "\n");
		return dataOut;
	}

	public int getBeat() throws InterruptedException{

		int count = 0;
		int heart = 0;
		boolean pulse = false;
		long start = System.currentTimeMillis();
		while(count < 10){
			heart = getAnalogData(0);
			heart &= 0x1FF;
//			System.out.print("Beat Data: " + heart + "\n");
			if(heart > 503){
				if(pulse == false){
					pulse = true;
					System.out.print("-------------------Beat\n");
					count++;
				}else{
					System.out.print("------\n");
				}
			}else{
				pulse = false;
				System.out.print("\n");
			}
			Thread.sleep(100);
		}
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.print("DIFF: " + diff + "\n");
		//long sec = diff/1000;
		//System.out.print("SEC: " + sec + "\n");
		int rate = (int)(600000/diff);
		return rate;
	}

	public double getTemp(double prev) throws InterruptedException{

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

}
