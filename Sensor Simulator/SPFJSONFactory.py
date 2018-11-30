import json
import time
import base64

#-------------------------------------------------------------
#Creates Json files for sending to pub/sub.
#-------------------------------------------------------------
class SPFJsonFactory:
    
	def __init__(self, indent=4, logDir=""):
		self.dir = logDir
		self.indent = indent
	
	#--------------------------------------------
	#Creates dictionary in Swarm Protocol Format
	#--------------------------------------------
	def createSPFDictionary(self, UID, SensorType="", payload=[]):
		data = {}
		data['UID']=UID
		data['Timestamp']=str(self.getCurrentTime())
		data['SensorType']=SensorType
		data['Payload']=str(list(payload))
		return data
	
	#---------------------------------------
	#Returns raw json
	#---------------------------------------
	def createJSON(self, dict):
		return json.dump(dict)
	
	#---------------------------------------
	#Returns json as a string.
	#---------------------------------------
	def createJSONString(self, dict):
		return json.dumps(dict, indent=self.indent)
	
	#---------------------------------------
	#Saves a json document
	#---------------------------------------
	def log(self, dict):
		with open(self.dir + 'SPFlog' + dict['UID'] + ':' + dict['Timestamp'] + '.json', 'w+') as outfile:
			json.dump(dict, outfile)

	#---------------------------------------
	#Return timestamp
	#---------------------------------------
	def getCurrentTime(self):
		return time.time()
