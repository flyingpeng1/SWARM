import time
import random
import math
import json

import Sensor
#------------------------------------
#Represents a pull heart rate sensor.
#------------------------------------
class PullHeartRateMonitor(Sensor.PullSensor):
 
	def __init__(self, sensorID, updateFrequency, startingBPM, rateMultiplier=1):
		Sensor.PullSensor.__init__(self, sensorID, updateFrequency)
		self.updateFrequency = updateFrequency
		self.type = "PullHeartRateMonitor"
		self.bpm = startingBPM
		self.rateMult = rateMultiplier
		
	def __str__(self):
		string = "SID: " + str(self.simulID) + "\n"
		string = string + " type: " + str(self.type)
		string = string + " updateFrequency: " + str(self.updateFrequency)
		string = string + " rateMult: " + str(self.rateMult)
		string = string + " bpm: " + str(self.bpm)
		return string
	
	#----------------------------------
	#Called to retrieve internal data.
	#----------------------------------
	def getData(self):
		return self.bpmJSON(self.bpm)
	
	#---------------------------------------------------------
	#Called to simulate the sensor updating its internal data.
	#---------------------------------------------------------
	def newData(self):
		deltaBPM = float(random.randint(-3 * self.rateMult, 3 * self.rateMult))
		#change BPM randomly
		self.bpm += deltaBPM
		if (self.bpm < 0):
			self.bpm = 0
		return(self.bpmJSON(self.bpm))

	#------------------------------------------
	#Creates Json representation of bpm output.
	#------------------------------------------
	def bpmJSON(self, bpm):
		dict = {}
		dict["Time"] = time.time()
		dict["BPM"] = bpm
		
		jsonDump = json.dumps(dict)
		return jsonDump

			