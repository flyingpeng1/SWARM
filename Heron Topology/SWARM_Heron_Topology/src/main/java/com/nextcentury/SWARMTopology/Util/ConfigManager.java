package com.nextcentury.SWARMTopology.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
	
	public static Properties getProperties() {
		Properties prop = new Properties();
		
		try {
			String propFileName = "config.properties";
			FileInputStream inputStream = new FileInputStream(propFileName);
			prop.load(inputStream);
			return prop;
		} catch (Exception e) {
			prop.setProperty("TopicName", "SWARMUserSensorData");
			prop.setProperty("KafkaServers", "10.44.0.7:9093");
			prop.setProperty("GroupName", "KafkaConsumer");
		}
		return null;
	}
}
