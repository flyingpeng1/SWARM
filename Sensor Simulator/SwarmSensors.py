class SwarmSensors:
 
 def __init__(self, UID):
  self.UID = UID
  self.sensors = {}
 
 def registerSensor(self, sensor):
  self.sensors[sensor.ID]
