tag='codegen'

docker rmi $tag

mvn clean
mvn package

docker build -t $tag .