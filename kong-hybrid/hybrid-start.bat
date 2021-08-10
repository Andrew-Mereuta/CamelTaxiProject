cd sample-plugin/kong/plugins

kubectl create configmap kong-plugin-sample-plugin --from-file=sample-plugin

cd ../../../..

cd kong-necessary-files

kubectl create secret generic kong-session-config --from-file=admin_gui_session_conf --from-file=portal_session_conf
kubectl create secret generic kong-enterprise-license --from-file=license=./license.json
kubectl create secret generic kong-enterprise-superuser-password --from-literal=password=pass

cd ..

cd kong-hybrid
kubectl create secret tls kong-cluster-cert --cert=cluster.crt --key=cluster.key

timeout /t 10

helm install cp-release kong/kong --values=kong-enterprise-hybrid-control-plane.yml

timeout /t 5

helm install dp-release kong/kong --values=kong-enterprise-hybrid-data-plane.yml

timeout /t 10

kubectl apply -f ./secretMap.yaml
kubectl apply -f ./jwt-plugin.yaml
kubectl apply -f ./config-map.yaml
kubectl apply -f ./persistent-volume.yaml

timeout /t 40

kubectl apply -f ./camel-deploy.yaml

PAUSE