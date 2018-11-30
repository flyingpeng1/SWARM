import datetime
import random
import threading
import time

import GPSSensor

#------------------------------------------------------------------------
#Asynchronously pulls data from pull sensor threads and calls a callback.
#------------------------------------------------------------------------
class PullThread:

	def __init__(self, UID, sensor):
		self.UID = UID
		self.sensor = sensor
		self.run=True

	def __str__(self):
		string = ""
		string = string + "Belongs to: " + str(self.UID)
		string = string + "\nwatching: " + str(self.sensor)
		return string

	#-----------------------------------
	#Starts pulling data
	#-----------------------------------
	def start(self, cb, delay):
		self.run=True
		self.cb = cb
		self.delay = delay
		self.sensorThread = threading.Thread(target=self.pullData).start()
	
	#-----------------------------------
	#Stops pulling data
	#-----------------------------------
	def stop(self):
		self.run=False
		self.sensorThread = None
	
	#-----------------------------------
	#Main pull loop
	#-----------------------------------
	def pullData(self):
		while(self.run): 
			data = self.sensor.getData()
			UID = self.UID
			type = self.sensor.type
			SID = self.sensor.simulID
			self.cb(data, type, SID, UID)
			time.sleep(int(self.delay))
  