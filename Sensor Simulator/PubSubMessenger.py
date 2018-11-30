import json

#--------------------------------
#Posts messages to pub/sub.
#--------------------------------
class PubSubMessenger:
	def __init__(self, config):
		self.TFDict = {"True":True, "False":False}
		
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
		
	def kafkaSetup(self):
		ack = self.config["acks"]
		if (len(ack) == 1):
			ack = int(ack)
		print("Start")
		from kafka import KafkaProducer
		self.KafkaProducer = KafkaProducer(
		bootstrap_servers=self.config["bootstrap_servers"],
		retries=int(self.config["retries"]),
		acks=ack,
		value_serializer=lambda v: v.encode('utf-8')
		)
		print("connected")
		
	
	def kafkaTransmit(self, SPFJsonPayload):
		self.KafkaProducer.send(self.config["topic"], SPFJsonPayload)
			
		
	
			