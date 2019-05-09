package NextCentury;

import com.google.gson.Gson;
import com.nextcentury.SWARMTopology.Util.RawDataObj;

public class JSONParseTest {

	public static void main(String args[]) {
		String Json = "{\r\n" + 
				"    \"UID\": 1002,\r\n" + 
				"    \"Timestamp\": \"1547238360.8119342\",\r\n" + 
				"    \"SensorType\": \"PullHeartRateMonitor\",\r\n" + 
				"    \"Payload\": \"{\\\"Time\\\": 1547238360.8119342, \\\"BPM\\\": 198.0}\"\r\n" + 
				"}";
		
		Gson g = new Gson();
		RawDataObj d = g.fromJson(Json, RawDataObj.class);
		
		System.out.println(d);
	}
	
}
