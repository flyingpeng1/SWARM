echo -e "Attempting to deploy SWARM Topology...\n"
heron submit kubernetes --service-url=http://localhost:8675/api/v1/proxy/namespaces/default/services/heron-apiserver:9000 --config-property heron.class.packing.algorithm=com.twitter.heron.packing.roundrobin.RoundRobinPacking  SWARM.jar com.nextcentury.SWARMTopology.SWARMTopology swarm --verbose
echo -e "Success\n"