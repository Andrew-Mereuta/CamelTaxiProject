@ECHO OFF
echo this command will create necessary files for kong crd demonstration

cd sample-plugin/kong/plugins
echo -----------------------
cd
echo -----------------------

kubectl create configmap kong-plugin-sample-plugin --from-file=sample-plugin

cd ../../../../

echo -----------------------
cd
echo -----------------------

cd kong-necessary-files

echo -----------------------
cd
echo -----------------------

PAUSE

kubectl create secret generic kong-session-config --from-file=admin_gui_session_conf --from-file=portal_session_conf
kubectl create secret generic kong-enterprise-license --from-file=license=./license.json
kubectl create secret generic kong-enterprise-superuser-password --from-literal=password=pass

cd ../

echo -----------------------
cd
echo -----------------------

cd kong-for-crds

timeout /t 6

echo -----------------------
cd
echo -----------------------

helm install kong-ee kong/kong --values=kong-enterprise-full-k4k8s.yml

timeout /t 6

kubectl apply -f ./secretMap.yaml
kubectl apply -f ./jwt-plugin.yaml
kubectl apply -f ./config-map.yaml
kubectl apply -f ./persistent-volume.yaml

timeout /t 30

kubectl apply -f ./camel-deploy.yaml
kubectl apply -f ./jwt-consumer.yaml

PAUSE