import datetime

class DataObject:

 def __init__(self, UID):
  self.timeCreated = datetime.datetime.now()
  self.UID = UID

 def toString(self):
  