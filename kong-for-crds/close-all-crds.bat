@ECHO OFF
echo This command closes kong-ee, deletes everything related to camel-project from kubernetes(Use only for crds). Ready? Lets go!

helm delete kong-ee
kubectl delete -f ./secretMap.yaml
kubectl delete -f ./config-map.yaml
kubectl delete -f ./persistent-volume.yaml
kubectl delete -f ./camel-deploy.yaml
kubectl delete -f ./jwt-consumer.yaml
kubectl delete -f ./jwt-plugin.yaml

kubectl delete configmap kong-plugin-sample-plugin
kubectl delete secret kong-session-config 
kubectl delete secret kong-enterprise-license
kubectl delete secret kong-enterprise-superuser-password

PAUSE