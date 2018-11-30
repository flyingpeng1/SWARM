import threading
import time
import random

#----------------------
#Basic abstract sensor.
#----------------------
class Sensor:

	def __init__(self, simulID, updateFrequency=1):
		self.simulID = simulID
		self.type = ""
		self.updateTime = updateFrequency
		self.run = False
		self.data=None
		
	def __str__(self):
		return "Generic sensor " + self.simulID + ", updateFreq: " + self.updateTime
		
	#-----------------------------------------
	#Called to start sensor simulation thread.
	#-----------------------------------------
	def start(self):
		self.run=True
		self.sensorThread = threading.Thread(target=self.mainUpdateLoop).start()

	#-----------------------------------------
	#Called to stop sensor simulation thread.
	#-----------------------------------------
	def stop(self):
		self.run=False

	#-----------------------------------------
	#Called by thread to start update loop.
	#-----------------------------------------
	def mainUpdateLoop(self):
		while (self.run):
			self.data = self.newData()
			time.sleep(self.updateTime)
	
	#---------------------------------------------------------
	#Called to simulate the sensor updating its internal data.
	#---------------------------------------------------------
	def newData(self):
		return(None)
	
#------------------
#Basic Pull sensor.
#------------------
class PullSensor(Sensor):

	def __init__(self, simulID, updateFrequency=1):
		Sensor.__init__(self, simulID, updateFrequency)
		self.type = "Pull"
		self.data=bytearray([1,2,3])

	def __str__(self):
		return "Generic pull sensor " + self.simulID + ", updateFreq: " + self.updateTime

	#----------------------------------
	#Called to retrieve internal data.
	#----------------------------------
	def getData(self):
		return self.data

	#---------------------------------------------------------
	#Called to simulate the sensor updating its internal data.
	#---------------------------------------------------------
	def newData(self):
		return(bytearray([random.randint(1,255), random.randint(1,255), random.randint(1,255)]))

#------------------
#Basic Push sensor.
#------------------	
class PushSensor(Sensor):
	def __init__(self, simulID, updateFrequency=1):
		Sensor.__init__(self, simulID, updateFrequency)
		self.type = "Push"
		self.data=bytearray([1,2,3])

	def __str__(self):
		return "Generic push sensor " + self.simulID + ", updateFreq: " + self.updateTime
		
	#----------------------------------------------------------------------------
	#Called to start sensor simulation thread that sends data back automatically.
	#----------------------------------------------------------------------------
	def start(self, callback):
		self.callback = callback
		self.run=True
		self.sensorThread = threading.Thread(target=self.mainUpdateLoop).start()

	#-----------------------------------------
	#Called by thread to start update loop.
	#-----------------------------------------
	def mainUpdateLoop(self):
		while (self.run):
			self.data = self.newData()
			self.callback(self.data, self.type, self.simulID)
			time.sleep(self.updateTime)

	#---------------------------------------------------------
	#Called to simulate the sensor updating its internal data.
	#---------------------------------------------------------
	def newData(self):
		return(bytearray([random.randint(1,255), random.randint(1,255), random.randint(1,255)]))
