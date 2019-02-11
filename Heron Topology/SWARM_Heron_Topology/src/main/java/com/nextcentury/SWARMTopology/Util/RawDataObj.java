package com.nextcentury.SWARMTopology.Util;

import java.util.ArrayList;

public class RawDataObj {
	private String UID;
	private double Timestamp;
	private String SensorType;
	private String Payload;
	
	public String getUID() {
		return UID;
	}
	
	public double getTimestamp() {
		return Timestamp;
	}
	
	public String getSensorType() {
		return SensorType;
	}
	
	public String getPayload() {
		return Payload;
	}
	
	public String toString() {
		return UID + " " + Timestamp + " " + SensorType + " " + Payload;
	}
}
