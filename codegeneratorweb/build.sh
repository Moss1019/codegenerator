tag='codegenweb'

rm -r ./build
docker rmi $tag

npm ic && npm run build

cd ./server

rm -r ./node_modules
rm -r ./public

cd ..

cp -r ./build ./server
mv ./server/build ./server/public

docker build -t $tag .
