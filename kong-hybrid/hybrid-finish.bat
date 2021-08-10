kubectl delete -f ./secretMap.yaml
kubectl delete -f ./config-map.yaml
kubectl delete -f ./persistent-volume.yaml
kubectl delete -f ./camel-deploy.yaml
kubectl delete -f ./jwt-plugin.yaml
helm delete cp-release
helm delete dp-release

PAUSE