cd ../codegeneratorlib
mvn install
cd ../codegenerator
mvn clean
mvn package
docker rmi codegen
docker build -t codegen .

cd ../codegeneratorweb
rm -r ./build
npm run build
cd ./server
rm -r ./public
cd ..
mv ./build ./server/public
docker rmi codegenweb
docker build -t codegenweb .

cd ../docker

docker-compose up
