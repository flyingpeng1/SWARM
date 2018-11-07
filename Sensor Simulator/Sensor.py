import threading
import time
import random

class PullSensor:

	def __init__(self, simulID, updateFrequency=1):
		self.simulID = simulID
		self.type = type
		self.updateTime = updateFrequency
		self.run = False
		self.data=bytearray([1,2,3])

	def toString(self):
		return "Generic pull sensor " + self.simulID + ", updateFreq: " + self.updateTime

	def start(self):
		self.run=True
		self.sensorThread = threading.Thread(target=self.mainUpdateLoop).start()

	def stop(self):
		self.run=False

	def mainUpdateLoop(self):
		while (self.run):
			self.data = self.newData()
			time.sleep(self.updateTime)
	
	def newData(self):
		return(bytearray([random.randint(1,255), random.randint(1,255), random.randint(1,255)]))

		
class PushSensor:
	def __init__(self, simulID, updateFrequency=1):
		self.simulID = simulID
		self.type = type
		self.updateTime = updateFrequency
		self.run = False
		self.data=bytearray([1,2,3])

	def toString(self):
		return "Generic push sensor " + self.simulID + ", updateFreq: " + self.updateTime

	def start(self, callback):
		self.callback = callback
		self.run=True
		self.sensorThread = threading.Thread(target=self.mainUpdateLoop).start()

	def stop(self):
		self.run=False

	def mainUpdateLoop(self):
		while (self.run):
			self.data = self.newData()
			self.callback(self.data)
			time.sleep(self.updateTime)
	
	def newData(self):
		return(bytearray([random.randint(1,255), random.randint(1,255), random.randint(1,255)]))
		
		
		
		
#def cb(data):
#	print(data)
#	
#p = PushSensor(1234, 5)
#p.start(cb)

