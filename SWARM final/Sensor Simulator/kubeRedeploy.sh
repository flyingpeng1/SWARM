(echo -e "Removing existing K8s simulator instance...\n" && sudo kubectl delete -f sensorsimulator.yml && sleep 30) || echo -e "Preparing to launch simulator from non-deployed state...\n"
echo -e "Updating image...\n"
sudo docker image build -t flyingpenguin2001/swarmsensorsimulator:latest .
echo -e "Pushing image to repo...\n"
sudo docker push flyingpenguin2001/swarmsensorsimulator:latest
sleep 5
echo -e "Deploying to K8s...\n"
sudo kubectl create -f sensorsimulator.yml
echo -e "Success\n"
