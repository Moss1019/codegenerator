cd ../codegeneratorlib
mvn install
cd ../codegenerator
mvn clean
mvn package
docker rmi codegen
docker build -t codegen .

cd ../codegeneratorweb
rm -r ./build
npm ic && npm run build
cd ./server
rm -r ./node_modules
rm -r ./public
cd ..
cp -r ./build ./server
mv ./server/build ./server/public
docker rmi codegenweb
docker build -t codegenweb .

cd ../docker

docker-compose up
