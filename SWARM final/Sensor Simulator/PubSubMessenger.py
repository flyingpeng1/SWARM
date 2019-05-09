import json
import traceback
import time
import logging

import HostnameResolver

#--------------------------------
#Posts messages to pub/sub.
#--------------------------------
class PubSubMessenger:
	def __init__(self, config):
		self.logger = logging.getLogger('SWARM_Simulator')
		self.TFDict = {"True":True, "False":False}
		self.emitCount = 0
		
		self.configName = config
		with open(config) as f:
			self.config = json.load(f)
			
		if (self.TFDict[self.config["use_kafka"]]):
			self.kafkaSetup()
		if (self.TFDict[self.config["use_redis"]]):
			print("WIP")
		
	def transmit(self, SPFJsonPayload):
		if (self.TFDict[self.config["use_kafka"]]):
			self.kafkaTransmit(SPFJsonPayload)
		if (self.TFDict[self.config["use_redis"]]):
			print("WIP")
		self.emitCount = self.emitCount + 1
		if (self.emitCount % 1000 == 0):
			print("Number of emitted entries: " + str(self.emitCount))
			self.logger.info("Number of emitted entries: " + str(self.emitCount))
		
	def kafkaSetup(self):
		ack = self.config["acks"]
		if (len(ack) == 1):
			ack = int(ack)
		from kafka import KafkaProducer
		connecting = True
		while connecting:
			with open(self.configName) as f:
				self.config = json.load(f)
			servers = HostnameResolver.resolve(self.config["bootstrap_servers"])
			try:
				print("Connecting to Kafka...")
				self.logger.info("Connecting to Kafka...")
				self.KafkaProducer = KafkaProducer(
				bootstrap_servers=servers,
				retries=int(self.config["retries"]),
				acks=ack,
				value_serializer=lambda v: v.encode('utf-8')
				)
				connecting = False
			except Exception:
				traceback.print_exc()
				time.sleep(10)
		print("connected to Kafka at " + str(self.config["bootstrap_servers"]))
		self.logger.info("connected to Kafka at " + str(self.config["bootstrap_servers"]))
		
	
	def kafkaTransmit(self, SPFJsonPayload):
		self.KafkaProducer.send(self.config["topicName"], SPFJsonPayload)
		#self.logger.info(str(self.config["topicName"]) + ' sent ' + str(SPFJsonPayload))
			
		
	
			