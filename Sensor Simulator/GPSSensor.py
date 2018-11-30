import time
import random
import base64
import math

import Sensor

#------------------------------------------
#Pull-style GPS sensor (returns bytearray).
#------------------------------------------
class PullGPSSensor(Sensor.PullSensor):

	def __init__(self, sensorID, updateFrequency, startingLat, startingLong, startingAlt, latMultiplier=1, longMultiplier=1, altMultiplier=1):
		Sensor.PullSensor.__init__(self, sensorID, updateFrequency)
		self.type = "PullGPS"
		self.updateFrequency = updateFrequency
		self.lat = startingLat
		self.long = startingLong
		self.alt = startingAlt
		self.latMult = latMultiplier
		self.altMult = altMultiplier
		self.longMult = longMultiplier
		self.bearing = 360.0
		self.speed = 0
		
	def __str__(self):
		string = "SID: " + str(self.simulID) + "\n"
		string = string + " updateFrequency: " + str(self.updateFrequency)
		string = string + " latMult: " + str(self.latMult)
		string = string + " longMult: " + str(self.longMult)
		string = string + " altMult: " + str(self.altMult)
		return string
	
	def getData(self):
		return self.latLongToBytes(self.lat, self.long, self.alt, self.bearing, self.speed)
	
	#---------------------------------------------------------
	#Called to simulate the sensor updating its internal data.
	#---------------------------------------------------------
	def newData(self):
		deltaLat = float(random.randint(-100* self.latMult,100* self.latMult)) / 1000000
		deltaLong = float(random.randint(-100 * self.longMult,100 * self.longMult)) / 1000000
		deltaAlt = float(random.randint(-5 * self.altMult,5 * self.altMult))
		#calculate speed and randomize bearing
		self.speed = (math.sqrt(deltaLat*deltaLat + deltaLong*deltaLong)*111000)/self.updateFrequency
		self.bearing = random.randint(0,360)
		#moves a random lat and long
		self.lat += deltaLat
		self.long += deltaLong
		self.alt += deltaAlt
		#loops if crossed starting lat/long line
		if (self.lat > 180):
			self.lat -= 360
		if (self.long > 180):
			self.long -= 360
		if (self.lat < -180):
			self.lat += 360
		if (self.long < -180):
			self.long += 360
		if (self.alt < 0):
			self.alt = 0
		
		return(self.latLongToBytes(self.lat,self.long,self.alt,self.bearing,self.speed))
	
	#-------------------------------------
	#Converts lat/long/bearing/speed into 10-byte array
	#byte 0 and 6 are 1 if negative, 2 if positive,
	#bytes 1-5 are lat in format 1.2345
	#bytes 6-10 are long in format 6.769(10)
	#-------------------------------------
	def latLongToBytes(self, lat, long, altitude, bearing, speed):
		#lat
		latArray = [2,0,0,0,0]
		if (lat < 0):
			latArray[0]=1
		latArray[1]=int(abs(lat))
		latArray[2]=int(abs((lat*100)%100))
		latArray[3]=int(abs((lat*10000)%100))
		latArray[4]=int(abs((lat*1000000)%100))
		#long
		longArray = [2,0,0,0,0]
		if (long < 1):
			longArray[0]=0
		longArray[1]=int(abs(long))
		longArray[2]=int(abs((long*100)%100))
		longArray[3]=int(abs((long*10000)%100))
		longArray[4]=int(abs((long*1000000)%100))
		#alt
		alt = int(altitude)
		a = alt % 256
		b = int(alt % 65536 / 256)
		c = int(alt % 16777216 / 65536)
		d = int(alt % 4294967296 / 16777216)
		altArray = [d, c, b, a]
		#bearing
		ber = int(bearing)
		a = ber % 256
		b = int(ber % 65536 / 256)
		berArray = [b, a]
		#speed
		spe = int(speed)
		a = spe % 256
		b = int(spe % 65536 / 256)
		c = int(spe % 16777216 / 65536)
		d = int(spe % 4294967296 / 16777216)
		speArray = [d, c, b, a]
		
		return bytearray(latArray + longArray + altArray + berArray + speArray)

#------------------------------------------
#Push-style GPS sensor (returns bytearray).
#------------------------------------------
class PushGPSSensor(Sensor.PushSensor):
 
	def __init__(self, sensorID, updateFrequency, startingLat, startingLong, startingAlt, latMultiplier=1, longMultiplier=1, altMultiplier=1):
		Sensor.PullSensor.__init__(self, sensorID, updateFrequency)
		self.type = "PushGPS"
		self.updateFrequency = updateFrequency
		self.lat = startingLat
		self.long = startingLong
		self.alt = startingAlt
		self.latMult = latMultiplier
		self.altMult = altMultiplier
		self.longMult = longMultiplier
		self.bearing = 360.0
		self.speed = 0
		
	def __str__(self):
		string = "SID: " + str(self.simulID) + "\n"
		string = string + " updateFrequency: " + str(self.updateFrequency)
		string = string + " latMult: " + str(self.latMult)
		string = string + " longMult: " + str(self.longMult)
		string = string + " altMult: " + str(self.altMult)
		return string
	
	#---------------------------------------------------------
	#Called to simulate the sensor updating its internal data.
	#---------------------------------------------------------
	def newData(self):
		deltaLat = float(random.randint(-100* self.latMult,100* self.latMult)) / 1000000
		deltaLong = float(random.randint(-100 * self.longMult,100 * self.longMult)) / 1000000
		deltaAlt = float(random.randint(-5 * self.altMult,5 * self.altMult))
		#calculate speed and randomize bearing
		self.speed = (math.sqrt(deltaLat*deltaLat + deltaLong*deltaLong)*111000)/self.updateFrequency
		self.bearing = random.randint(0,360)
		#moves a random lat and long
		self.lat += deltaLat
		self.long += deltaLong
		self.alt += deltaAlt
		#loops if crossed starting lat/long line
		if (self.lat > 180):
			self.lat -= 360
		if (self.long > 180):
			self.long -= 360
		if (self.lat < -180):
			self.lat += 360
		if (self.long < -180):
			self.long += 360
			
		if (self.alt < 0):
			self.alt = 0
		
		return(self.latLongToBytes(self.lat,self.long,self.alt,self.bearing,self.speed))
	
	#-------------------------------------
	#Converts lat/long/bearing/speed into 10-byte array
	#byte 0 and 6 are 1 if negative, 2 if positive,
	#bytes 1-5 are lat in format 1.2345
	#bytes 6-10 are long in format 6.769(10)
	#-------------------------------------
	def latLongToBytes(self, lat, long, altitude, bearing, speed):
		#lat
		latArray = [2,0,0,0,0]
		if (lat < 0):
			latArray[0]=1
		latArray[1]=int(abs(lat))
		latArray[2]=int(abs((lat*100)%100))
		latArray[3]=int(abs((lat*10000)%100))
		latArray[4]=int(abs((lat*1000000)%100))
		#long
		longArray = [2,0,0,0,0]
		if (long < 1):
			longArray[0]=0
		longArray[1]=int(abs(long))
		longArray[2]=int(abs((long*100)%100))
		longArray[3]=int(abs((long*10000)%100))
		longArray[4]=int(abs((long*1000000)%100))
		#alt
		alt = int(altitude)
		a = alt % 256
		b = int(alt % 65536 / 256)
		c = int(alt % 16777216 / 65536)
		d = int(alt % 4294967296 / 16777216)
		altArray = [d, c, b, a]
		#bearing
		ber = int(bearing)
		a = ber % 256
		b = int(ber % 65536 / 256)
		berArray = [b, a]
		#speed
		spe = int(speed)
		a = spe % 256
		b = int(spe % 65536 / 256)
		c = int(spe % 16777216 / 65536)
		d = int(spe % 4294967296 / 16777216)
		speArray = [d, c, b, a]
		return bytearray(latArray + longArray + altArray + berArray + speArray)
