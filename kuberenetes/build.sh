kubectl create -f ./namespace.yaml

kubectl create -f ./server/replica_set.yaml
kubectl create -f ./web/replica_set.yaml

kubectl create -f ./server/nodeport.yaml
kubectl create -f ./web/nodeport.yaml

kubectl create -f ./ingress.yaml
