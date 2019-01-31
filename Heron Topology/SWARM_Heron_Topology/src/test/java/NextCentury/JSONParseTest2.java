package NextCentury;

import java.util.Map;

import com.google.gson.Gson;
import com.nextcentury.SWARMTopology.SWARMTupleSchema;

public class JSONParseTest2 {
	public static void main (String args[]) {
		Gson gson = new Gson();
		
		String json = "{\"Time\": 1547503314.2242699, \"BPM\": 57.0}";
		Map<String, String> values = gson.fromJson(json, Map.class);
		
		Object o = values.get("BPM");
		System.out.println(o);
		
	}
}
