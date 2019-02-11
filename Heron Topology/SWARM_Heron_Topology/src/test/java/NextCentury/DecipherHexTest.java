package NextCentury;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

@SuppressWarnings("restriction")
public class DecipherHexTest {
	
	public static void main(String args[]) {
		HexBinaryAdapter adapter = new HexBinaryAdapter();
		byte[] bytes = adapter.unmarshal("0127550519004c1e4316000001d700d10000000e");

		for (byte b : bytes) {
			System.out.print(unsignify(b));
			System.out.print(",");
		}

		double latitude = 0;
		double longitude = 0;
		double altitude = 0;
		double bearing = 0;
		double speed = 0;
		
		//latitude
		String latString = "";
		if (!(unsignify(bytes[0]) > 0)) {
			latString = "-";
		}
		latString += Integer.toString(unsignify(bytes[1]));
		latString += ".";
		latString += Integer.toString(unsignify(bytes[2]));
		latString += Integer.toString(unsignify(bytes[3]));
		latString += Integer.toString(unsignify(bytes[4]));
		latitude = Double.parseDouble(latString);
		//longitude
		String longString = "";
		if (!(bytes[5] > 0)) {
			longString = "-";
		}
		longString += Integer.toString(unsignify(bytes[6]));
		longString += ".";
		longString += Integer.toString(unsignify(bytes[7]));
		longString += Integer.toString(unsignify(bytes[8]));
		longString += Integer.toString(unsignify(bytes[9]));
		longitude = Double.parseDouble(longString);
		//altitude
		altitude += unsignify(bytes[13]);
		altitude += unsignify(bytes[12]) * 256;
		altitude += unsignify(bytes[11]) * 65536;
		altitude += unsignify(bytes[10]) * 16777216;
		//bearing
		bearing += unsignify(bytes[15]);
		bearing += unsignify(bytes[14]) * 256;
		//speed
		speed += unsignify(bytes[19]);
		speed += unsignify(bytes[18]) * 256;
		speed += unsignify(bytes[17]) * 65536;
		speed += unsignify(bytes[16]) * 16777216;

		
		System.out.println("\n" + latitude);
		System.out.println("\n" + longitude);
		System.out.println("\n" + altitude);
		System.out.println("\n" + bearing);
		System.out.println("\n" + speed);
		
	}
	
	private static int unsignify(byte b) {
		int num = b;
		
		if (num < 0) {
			num = num + 256;
		}
		
		return num;
		
	}
}
