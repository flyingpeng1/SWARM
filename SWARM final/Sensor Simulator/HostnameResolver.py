import socket

def resolve(names):
	ipList = []
	resolvedIP = ""
	for name in names:
		try:
			if (":" in name):
				hostname = socket.gethostbyname(name[0:name.find(":")])
				port = name[name.find(":") + 1:]
				resolvedIP = (str(hostname) + ":" + port)
			else:
				resolvedIP =  socket.gethostbyname(name)
			ipList.append(resolvedIP)
		except:
			print('Hostname resolution error!')
	return ipList
	
