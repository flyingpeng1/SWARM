import json
import time
import base64

class SPFJsonFactory:
    
	def __init__(self, indent=4, logDir=""):
		self.dir = logDir
		self.indent = indent
		
	def createSPFDictionary(self, UID, SensorType="", payload=[]):
		data = {}
		data['UID']=UID
		data['Timestamp']=str(self.getCurrentTime())
		data['SensorType']=SensorType
		data['Payload']=str(base64.encodestring(payload)))
		return data
	
	def createJSON(self, dict):
		return json.dump(dict)
	
	def createJSONString(self, dict):
		return json.dumps(dict, indent=self.indent)
	
	def log(self, dict):
		with open(self.dir + 'SPFlog' + dict['UID'] + ':' + dict['Timestamp'] + '.json', 'w+') as outfile:
			json.dump(dict, outfile)

	def getCurrentTime(self):
		return time.time()
	   

#f = SPFJsonFactory()
#ba = bytearray([1, 23, 5, 34, 12])
#dict = f.createSPFDictionary('1234', 'SimGPS', ba)
#print(f.createJSONString(dict))
