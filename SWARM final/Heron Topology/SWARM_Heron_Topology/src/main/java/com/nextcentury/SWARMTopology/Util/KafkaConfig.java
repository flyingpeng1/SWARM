package com.nextcentury.SWARMTopology.Util;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan
@PropertySource("classpath:Kafka.properties")
public class KafkaConfig implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//---------------------------------
	//spout Kafka
	//---------------------------------
    @Value("${Kafka.SpoutTopicName}")
    private String SpoutTopicName;
    
    @Value("${Kafka.SpoutServers}")
    private String SpoutServers;
    
    @Value("${Kafka.SpoutGroupName}")
    private String SpoutGroupName;
	
    public String getSpoutKafkaTopicName() {
    	return SpoutTopicName;
    }
    
    public String getSpoutKafkaServers() {
    	return SpoutServers;
    }
    
    public String getSpoutKafkaGroupName() {
    	return SpoutGroupName;
    }
    
	//---------------------------------
	//Normalized Kafka
	//---------------------------------
    @Value("${Kafka.NormOutTopicName}")
    private String NormOutTopicName;
    
    @Value("${Kafka.NormOutServers}")
    private String NormOutServers;
    
    @Value("${Kafka.NormOutGroupName}")
    private String NormOutGroupName;
    
    @Value("${Kafka.NormOutAcksVal}")
    private String NormOutAcksVal;
	
    public String getNormOutKafkaTopicName() {
    	return NormOutTopicName;
    }
    
    public String getNormOutKafkaServers() {
    	return NormOutServers;
    }
    
    public String getNormOutKafkaGroupName() {
    	return NormOutGroupName;
    }
    
    public String getNormOutAcksVal() {
    	return NormOutAcksVal;
    }
    
	//---------------------------------
	//Alerts Kafka
	//---------------------------------
    @Value("${Kafka.AlertOutTopicName}")
    private String AlertOutTopicName;
    
    @Value("${Kafka.AlertOutServers}")
    private String AlertOutServers;
    
    @Value("${Kafka.AlertOutGroupName}")
    private String AlertOutGroupName;
	
    public String getAlertOutKafkaTopicName() {
    	return AlertOutTopicName;
    }
    
    public String getAlertOutKafkaServers() {
    	return AlertOutServers;
    }
    
    public String getAlertOutKafkaGroupName() {
    	return AlertOutGroupName;
    }
    
    //--------------------------------------------------
    //Global configuration.
    //--------------------------------------------------
    @Value("${Kafka.UseAsync}")
    private boolean UseAsync;
    
    public boolean getUseAsync() {
    	return UseAsync;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
            return new PropertySourcesPlaceholderConfigurer();
    }
}
