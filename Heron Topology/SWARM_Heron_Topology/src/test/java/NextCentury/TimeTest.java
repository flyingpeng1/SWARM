package NextCentury;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.nextcentury.SWARMTopology.SWARMTupleSchema;
import com.nextcentury.SWARMTopology.Bolts.MetricsBolt;

public class TimeTest {

	public static void main(String[] args) {
		double timeSum;
		double count;
		double startTime;
		double currentTime = 0;
		int elapsedMinTotal;
		int latestLoggedMinute;
		
		timeSum = 0;
		count = 0;
		latestLoggedMinute = 0;
		startTime = 0;
		
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		try {
			//start trial when heron finishes its first tuple
			if (count == 0) {
				startTime = new Long(System.currentTimeMillis()).doubleValue()/1000d;
			}
			
			currentTime = new Long(System.currentTimeMillis()).doubleValue()/1000d;
			timeSum += ((currentTime + 1) - currentTime);
			count++;

			//1549658178.056304
			elapsedMinTotal = (int)((double)(currentTime - startTime) / 10d);
			
			if (elapsedMinTotal > latestLoggedMinute) {

				
				System.out.println("\nElapsed simulation time (min): " + elapsedMinTotal + "\nAverage Latencey per tuple (ms):" + (timeSum / count) + "\nTotal tuples processed (#):" + count);
				latestLoggedMinute = elapsedMinTotal;
			}
		} catch (Exception e){
			System.out.println(e.getStackTrace().toString());
		}
		}
	}

}
