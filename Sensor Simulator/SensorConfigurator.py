
import GPSSensor
import HeartRateSensor

import random
import time
import json

class SensorConfigurator:

	def __init__(self, config):
		with open(config) as f:
			self.config = json.load(f)
		self.delay = float(self.config["SensorDelay"])
		self.tf = {}
		self.tf["True"] = True
		self.tf["False"] = False
	
	#-------------------------------------
	#Creates sensor from string argument.
	#-------------------------------------
	def sensorObjFromString(self, sensorName, id):
		self.id = id
		if (sensorName == "PullGPS"):
			return self.PullGPS()
		elif(sensorName == "PushGPS"):
			return self.PushGPS()
		elif(sensorName == "PullHeartRateMonitor"):
			return self.HeartRateMonitor()
	
	#-------------------------------------
	#Sets up sensor with configured values.
	#-------------------------------------
	def HeartRateMonitor(self):
		#load config
		if (self.tf[self.config["RandomizeBPMMult"]]):
			BPMMult = random.randint(1,3)
		else:
			BPMMult = int(self.config["BPMMult"])
			
		if (self.tf[self.config["RandomizeStartBPM"]]):
			BPM = random.randint(0,250)
		else:
			BPM = int(self.config["startBPM"])
			
		return HeartRateSensor.HeartRateMonitor(self.id, self.delay, BPM, BPMMult)
		
	#-------------------------------------
	#Sets up sensor with configured values.
	#-------------------------------------
	def PullGPS(self):
		#Load all configs
		if (self.tf[self.config["randomizeDeltaLongMult"]]):
			deltaLongMult = random.randint(1, 10)
		else:
			deltaLongMult = int(self.config["deltaLong"])
			
		if (self.tf[self.config["randomizeDeltaLatMult"]]):
			deltaLatMult = random.randint(1, 10)
		else:
			deltaLatMult = int(self.config["deltaLat"])
			
		if (self.tf[self.config["randomizeDeltaAltMult"]]):
			deltaAltMult = random.randint(1, 10)
		else:
			deltaAltMult = int(self.config["deltaAlt"])
			
		if (self.tf[self.config["randomizeStartLat"]]):
			startLat = float(self.config["startLat"] + str(random.randint(0,999999)))
		else:
			startLat = float(self.config["startLat"])
			
		if (self.tf[self.config["randomizeStartLong"]]):
			startLong = float(self.config["startLong"] + str(random.randint(0,999999)))
		else:
			startLong = float(self.config["startLong"])
			
		if (self.tf[self.config["randomizeStartAlt"]]):
			startAlt = float(random.randint(0, 1000))
		else:
			startAlt = float(self.config["startAlt"])
			
		return GPSSensor.PullGPSSensor(self.id, self.delay, startLat, startLong, startAlt, deltaLatMult, deltaLongMult, deltaAltMult)

	#-------------------------------------
	#Sets up sensor with configured values.
	#-------------------------------------
	def PushGPS(self):
		#Load all configs
		if (self.tf[self.config["randomizeDeltaLongMult"]]):
			deltaLongMult = random.randint(1, 10)
		else:
			deltaLongMult = int(self.config["deltaLong"])
			
		if (self.tf[self.config["randomizeDeltaLatMult"]]):
			deltaLatMult = random.randint(1, 10)
		else:
			deltaLatMult = int(self.config["deltaLat"])
			
		if (self.tf[self.config["randomizeDeltaAltMult"]]):
			deltaAltMult = random.randint(1, 10)
		else:
			deltaAltMult = int(self.config["deltaAlt"])
			
		if (self.tf[self.config["randomizeStartLat"]]):
			startLat = float(self.config["startLat"] + str(random.randint(0,999999)))
		else:
			startLat = float(self.config["startLat"])
			
		if (self.tf[self.config["randomizeStartLong"]]):
			startLong = float(self.config["startLong"] + str(random.randint(0,999999)))
		else:
			startLong = float(self.config["startLong"])
			
		if (self.tf[self.config["randomizeStartAlt"]]):
			startAlt = float(random.randint(0, 1000))
		else:
			startAlt = float(self.config["startAlt"])
			
		return GPSSensor.PushGPSSensor(self.id, self.delay, startLat, startLong, startAlt, deltaLatMult, deltaLongMult, deltaAltMult)
			