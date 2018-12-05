import random
import time
import json

import User
import SPFJSONFactory
import SensorConfigurator
import PubSubMessenger

#--------------------------
#Controls sensor simulator.
#--------------------------
class SensorMain:

	def __init__(self, sensorConfig, sysConfig):
		with open(sensorConfig) as f:
			self.config = json.load(f)
		self.configurator = SensorConfigurator.SensorConfigurator(sensorConfig)
		self.users = []
		self.jsonFactory = SPFJSONFactory.SPFJsonFactory()
		self.Messenger = PubSubMessenger.PubSubMessenger(sysConfig)
		self.setup()
		
	#-----------------------------------------
	#Follows config file to set up simulation
	#-----------------------------------------
	def setup(self):
		numberOfUsers = int(self.config["Numusers"])
		min = int(self.config["NumSensorsMin"])
		max = int(self.config["NumSensorsMax"])
	
		for i in range(0, numberOfUsers):
			sensorTypes = self.config["SensorTypes"].copy()
			user = User.User(1000 + i)
			for i in range(0, random.randint(min, max)):
				random.shuffle(sensorTypes)
				sensorName = sensorTypes.pop()
				user.addSensor(self.configurator.sensorObjFromString(sensorName, 100 + i))
			self.users.append(user)
	
	#---------------------------------------
	#Begins running the simulation
	#---------------------------------------
	def startSimulator(self):
		for user in self.users:
			user.startAllSensors(self.callback)
			time.sleep(float(random.randint(10,100)) / 100)
		for user in self.users:
			user.autoPullSetup(self.callback, self.config["PullDelay"])
	
	#---------------------------------------
	#Stops running the simulation
	#---------------------------------------
	def stopSimulator(self):
		for user in self.users:
			user.stopAllSensors()
		for user in self.users:
			user.stopAllPullThreads()
	
	#---------------------------------------
	#Called by pullThreads and push sensors.
	#(Data is not always the same object)
	#---------------------------------------
	def callback(self, data, type, SID, UID):
		dict = self.jsonFactory.createSPFDictionary(UID, type, data)
		self.Messenger.transmit(self.jsonFactory.createJSONString(dict))
	
s = SensorMain('SensorConfig.json', 'SystemConfig.json')
s.startSimulator()
input("Press ENTER to stop simulation...")
s.stopSimulator()
print('Ended')

			