kubectl delete -n codegen-ns ingress codegen

kubectl delete -n codegen-ns svc codegenweb
kubectl delete -n codegen-ns svc codegen

kubectl delete -n codegen-ns rs codegenweb
kubectl delete -n codegen-ns rs codegen

kubectl delete ns codegen-ns
