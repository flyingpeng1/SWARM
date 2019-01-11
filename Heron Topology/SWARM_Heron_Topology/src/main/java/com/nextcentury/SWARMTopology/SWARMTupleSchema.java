package com.nextcentury.SWARMTopology;

import com.twitter.heron.api.tuple.Fields;

public class SWARMTupleSchema {
	final static String SCHEMA_UID = "UID";
	final static String SCHEMA_TIMESTAMP = "Timestamp";
	final static String SCHEMA_SENSOR_TYPE = "SensorType";
	final static String SCHEMA_RAW_DATA = "RawData";
	
	//data categories
	final static String SCHEMA_LATITUDE = "Latitude";
	final static String SCHEMA_LONGITUDE = "Longitude";
	final static String SCHEMA_ALTITUDE = "Altitude";
	final static String SCHEMA_BEARING = "Bearing";
	final static String SCHEMA_SPEED = "Speed";
	final static String SCHEMA_HEART_RATE = "HeartRate";
	
	
	 //JSON is turned directly to this tuple
	 public static Fields getRawSchema(){
	        return new Fields(
	        		SCHEMA_UID, 
	        		SCHEMA_TIMESTAMP,
	        		SCHEMA_SENSOR_TYPE,
	        		SCHEMA_RAW_DATA
	                );
	    }
	 
	 //used for storing all possible data types collected by sensors
	 public static Fields getNormalizedSchema() {
	        return new Fields(
	        		SCHEMA_UID, 
	        		SCHEMA_TIMESTAMP,
	        		SCHEMA_LATITUDE,
	        		SCHEMA_LONGITUDE,
	        		SCHEMA_ALTITUDE,
	        		SCHEMA_BEARING,
	        		SCHEMA_SPEED,
	        		SCHEMA_HEART_RATE
	                );
	 }
}
