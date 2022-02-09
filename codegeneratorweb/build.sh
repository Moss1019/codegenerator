tag='mossonthetree/codegeneratorweb:latest'

rm -rf ./build
docker rmi $tag

npm ic && npm run build

cd ./server

rm -rf ./node_modules
rm -rf ./public

cd ..

cp -r ./build ./server
mv ./server/build ./server/public

docker build -t $tag .
