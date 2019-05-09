import datetime
import random
import threading
import logging

import GPSSensor
import PullThread

#-------------------------------------------------------------
#Represents a user and/or device which has sensors associated.
#-------------------------------------------------------------
class User:

	def __init__(self, UID):
		self.UID = UID
		self.sensors = []
		self.pullThreads = []
		self.logger = logging.getLogger('SWARM_Simulator')

	def __str__(self):
		string = ""
		string = string + "UID: " + str(self.UID) + "\nSensors: \n"
		for sensor in self.sensors:
			string = string + " " + str(sensor) + "\n"
		return string

	#-----------------------------------
	#registers a new sensor to the user.
	#-----------------------------------
	def addSensor(self, sensorObject):
		self.sensors.append(sensorObject)

	#-----------------------------------
	#Starts all user-linked sensors.
	#-----------------------------------
	def startAllSensors(self, callback=None):
		self.callback = callback
		for sensor in self.sensors:
			if ("Pull" in sensor.type):
				sensor.start()
			elif ("Push" in sensor.type):
				sensor.start(self.userCallback)
			self.logger.info("started sensor " + str(sensor) + " for " + str(self))
	
	#-----------------------------------
	#Intermediate callback to add UID.
	#-----------------------------------
	def userCallback(self, data, type, SID):
		self.callback(data, type, SID, self.UID)

	#-----------------------------------
	#Stops all user-registered sensors.
	#-----------------------------------
	def stopAllSensors(self):
		for sensor in self.sensors:
			sensor.stop()
	
	#-------------------------------------
	#Stops all user-registered pullthreads.
	#-------------------------------------
	def stopAllPullThreads(self):
		for thread in self.pullThreads:
			thread.stop()

	#----------------------------------------
	#Starts a pullthread for each pull sensor.
	#---------------------------------------
	def autoPullSetup(self, cb, delay):
		for sensor in self.sensors:
			if ("Pull" in sensor.type):
				pt = PullThread.PullThread(self.UID, sensor)
				self.pullThreads.append(pt)
		for thread in self.pullThreads:
			thread.start(cb, delay)
			
	#----------------------------------------------------------
	#For testing purposes: make a user directly from user class
	#----------------------------------------------------------
	def generateDefaultUser(self, numSensors=1, updateFreq=.5):
		for i in range(0, numSensors):
			self.addSensor(GPSSensor.PushGPSSensor(random.randint(1000, 9999), updateFreq, 39.264650, -76.793905, 1, random.randint(1, 4), random.randint(1, 4), random.randint(1, 4)))

#def cb(data):
#	print(str(data))

#(39.267650, -76.798905)
#u = User(12345)
#u.generateDefaultUser()
#print(u)
#u.startAllSensors(cb)

#u.addSensor()
#u.addSensor(GPSSensor.PushGPSSensor(123, .5, 39.267650, -76.798905, 1))

#print(u)
#u.startAllSensors(cb)
  