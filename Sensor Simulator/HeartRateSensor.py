import Sensor
import time
import random
import math
import json

class HeartRateMonitor(Sensor.PullSensor):
 
	def __init__(self, sensorID, updateFrequency, startingBPM, rateMultiplier=1):
		Sensor.PullSensor.__init__(self, sensorID, updateFrequency)
		self.type = "PullHeartRateMonitor"
		self.bpm = startingBPM
		self.rateMult = rateMultiplier
		
	def __str__(self):
		string = "SID: " + str(self.simulID) + "\n"
		string = string + " updateFrequency: " + str(self.updateFrequency)
		string = string + " rateMult: " + str(rateMultiplier.bpm)
		return string
	
	#-------------------------------------
	#Sets up sensor with configured values.
	#-------------------------------------
	def getData(self):
		return self.bpmJSON(self.bpm)
		
	def newData(self):
		deltaBPM = float(random.randint(-3 * self.rateMult, 3 * self.rateMult))
		
		#changes BPM randomly
		self.bpm += deltaBPM
		
		if (self.bpm < 0):
			self.bpm = 0

		return(self.bpmJSON(self.bpm))
	
	def bpmJSON(self, bpm):
		dict = {}
		dict["Time"] = time.time()
		dict["BPM"] = bpm
		
		jsonDump = json.dumps(dict)
		return jsonDump

			