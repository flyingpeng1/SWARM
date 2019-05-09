echo -e "Attempting to kill SWARM Topology...\n"
heron kill kubernetes --service-url=http://localhost:8675/api/v1/proxy/namespaces/default/services/heron-apiserver:9000 swarm --verbose
echo -e "Success\n"