package com.nextcentury.SWARMTopology.Util;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("classpath:Topology.properties")
public class TopologyConfig implements Serializable {

	//general topology parameters
	@Value("${Topology.StreamManagerNum}")
    private int StreamManagerNum;
    public int getStreamManagerNum() {
    	return StreamManagerNum;
    }
	@Value("${Topology.ContainerDiskSpace}")
    private int ContainerDiskSpace;
    public int getContainerDiskSpaceNum() {
    	return ContainerDiskSpace;
    }
	@Value("${Topology.ContainerRequestedCPU}")
    private float ContainerRequestedCPU;
    public float getContainerRequestedCPU() {
    	return ContainerRequestedCPU;
    }
	
    //------------------------------------
    //Node-specific parameters.
    //------------------------------------
	@Value("${Node.SensorDataSpout.RAM}")
    private int SensorDataSpoutRAM;
    public int getSensorDataSpoutRAM() {
    	return SensorDataSpoutRAM;
    }
	@Value("${Node.SensorDataSpout.InstanceNum}")
    private int SensorDataSpoutInstanceNum;
    public int getSensorDataSpoutInstanceNum() {
    	return SensorDataSpoutInstanceNum;
    }
    
	@Value("${Node.DigestRouterBolt.RAM}")
    private int DigestRouterBoltRAM;
    public int getDigestRouterBoltRAM() {
    	return DigestRouterBoltRAM;
    }
	@Value("${Node.DigestRouterBolt.InstanceNum}")
    private int DigestRouterBoltInstaceNum;
    public int getDigestRouterBoltInstanceNum() {
    	return DigestRouterBoltInstaceNum;
    }
    
	@Value("${Node.SimGPSAdapterBolt.RAM}")
    private int SimGPSAdapterBoltRAM;
    public int getSimGPSAdapterBoltRAM() {
    	return SimGPSAdapterBoltRAM;
    }
	@Value("${Node.SimGPSAdapterBolt.InstanceNum}")
    private int SimGPSAdapterBoltInstaceNum;
    public int getSimGPSAdapterBoltInstanceNum() {
    	return SimGPSAdapterBoltInstaceNum;
    }
    
	@Value("${Node.SimHeartRateAdapterBolt.RAM}")
    private int SimHeartRateAdapterBoltRAM;
    public int getSimHeartRateAdapterBoltRAM() {
    	return SimHeartRateAdapterBoltRAM;
    }
	@Value("${Node.SimHeartRateAdapterBolt.InstanceNum}")
    private int SimHeartRateAdapterBoltInstaceNum;
    public int getSimHeartRateAdapterBoltInstanceNum() {
    	return SimHeartRateAdapterBoltInstaceNum;
    }
    
	@Value("${Node.NormalizedRouterBolt.RAM}")
    private int NormalizedRouterBoltRAM;
    public int getNormalizedRouterBoltRAM() {
    	return NormalizedRouterBoltRAM;
    }
	@Value("${Node.NormalizedRouterBolt.InstanceNum}")
    private int NormalizedRouterBoltInstaceNum;
    public int getNormalizedRouterBoltInstanceNum() {
    	return NormalizedRouterBoltInstaceNum;
    }
    
	@Value("${Node.NormalizedAnalyticsPlaceholderBolt.RAM}")
    private int NormalizedAnalyticsPlaceholderBoltRAM;
    public int getNormalizedAnalyticsPlaceholderBoltRAM() {
    	return NormalizedAnalyticsPlaceholderBoltRAM;
    }
	@Value("${Node.NormalizedAnalyticsPlaceholderBolt.InstanceNum}")
    private int NormalizedAnalyticsPlaceholderBoltInstanceNum;
    public int getNormalizedAnalyticsPlaceholderBoltInstanceNum() {
    	return NormalizedAnalyticsPlaceholderBoltInstanceNum;
    }
    
	@Value("${Node.NormalizedDatabaseBolt.RAM}")
    private int NormalizedDatabaseBoltRAM;
    public int getNormalizedDatabaseBoltRAM() {
    	return NormalizedDatabaseBoltRAM;
    }
	@Value("${Node.NormalizedDatabaseBolt.InstanceNum}")
    private int NormalizedDatabaseBoltInstanceNum;
    public int getNormalizedDatabaseBoltInstanceNum() {
    	return NormalizedDatabaseBoltInstanceNum;
    }
    
	@Value("${Node.NormalizedDatabaseBolt.RAM}")
    private int NormalizedPubSubBoltRAM;
    public int getNormalizedPubSubBoltRAM() {
    	return NormalizedPubSubBoltRAM;
    }
	@Value("${Node.NormalizedPubSubBolt.InstanceNum}")
    private int NormalizedPubSubBoltInstanceNum;
    public int getNormalizedPubSubBoltInstanceNum() {
    	return NormalizedPubSubBoltInstanceNum;
    }
}
