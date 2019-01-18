package NextCentury;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class LoadProperties {

	public static void main(String[] args) {

		try {
			Properties p = LoadProperties.getProperties();
			System.out.println(p.getProperty("TopicName"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Properties getProperties() throws IOException {
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			FileInputStream inputStream = new FileInputStream(propFileName);
			prop.load(inputStream);
			return prop;
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		return null;
	}

}
