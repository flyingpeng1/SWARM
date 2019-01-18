package com.nextcentury.SWARMTopology;

import com.twitter.heron.api.tuple.Fields;

public class SWARMTupleSchema {
	public final static String SCHEMA_UID = "UID";
	public final static String SCHEMA_TIMESTAMP = "Timestamp";
	public final static String SCHEMA_SENSOR_TYPE = "SensorType";
	public final static String SCHEMA_RAW_DATA = "RawData";
	
	//data categories
	public final static String SCHEMA_LATITUDE = "Latitude";
	public final static String SCHEMA_LONGITUDE = "Longitude";
	public final static String SCHEMA_ALTITUDE = "Altitude";
	public final static String SCHEMA_BEARING = "Bearing";
	public final static String SCHEMA_SPEED = "Speed";
	public final static String SCHEMA_HEART_RATE = "HeartRate";
	
	
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
