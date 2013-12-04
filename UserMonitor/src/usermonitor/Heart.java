import com.pi4j.io.gpio.*;
import java.util.*;
import java.lang.*;
import java.io.*;


public class Heart{

	final GpioController heart;
	final GpioPinDigitalOutput clk;
	final GpioPinDigitalOutput CS;
	final GpioPinDigitalOutput dataOut;
	final GpioPinDigitalInput dataIn;
	//final GpioPinDigitalInput tempIn;

	public Heart(){

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

	public long getBeat(){

		int count = 0;
		int heart = 0;
		boolean pulse = false;
		long start = System.currentTimeMillis();
		while(count < 10){
			heart = getAnalogData(0);
			if(heart > 700){
				if(pulse == false){
					pulse = true;
					count++;
				}
			}else{
				pulse = false;
			}
		}
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.print("DIFF: " + diff + "\n");
		long sec = diff/1000;
		System.out.print("SEC: " + sec + "\n");
		long rate = (600/sec);
		return rate;
	}

	public int getTemp() throws InterruptedException{

		//(input - 500)/10
		int count = 0;
		int body = 0;
		while(count < 4){
			body = body + getAnalogData(1);
			Thread.sleep(200);
			count++;
		}
		body = body/4;
		body = (body-500)/10;
		return body;
	}

	public static void main(String argc[]) throws InterruptedException{

		Heart test = new Heart();
		int beat = 0;
		int temp = 0;
		int count;
		int thresh = 700;
		boolean pulse;
		long start = 0;
		long end = 0;
		long diff = 0;
		//long sec = 0;
		while(true){
			count = 0;
			pulse = false;
			start = System.currentTimeMillis();
			while(count < 10){
				beat = test.getAnalogData(0);
				int beat2 = test.getBeat();
				if(beat > thresh){
					if(pulse == false){
						pulse = true;
						count++;
					}
				}else{
					pulse = false;
				}
			}
			end = System.currentTimeMillis();
			diff = end - start;
			System.out.print("TIme for 10: " + diff + "\n");
			//sec = diff/1000;
			beat = (int)(600/diff);
			count = 0;
		/*	while(count < 10){
				temp = temp + test.getAnalogData(1);
				Thread.sleep(200);
				count++;
			}
			temp = temp/10;
			temp = (temp-500)/10;*/
			System.out.print("Heart Rate: " + beat + "\nTemperature: " + temp + "\n\n");
			beat = 0;
			temp = 0;
			Thread.sleep(1000);
		}
		//test.shutdown();
	}
}
