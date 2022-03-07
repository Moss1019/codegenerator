
npm ic
npm run build

cd server 

rm -r ./public
npm ic
npm run build

cd ..

mv ./build/* ./server/public
cp ./src/logo_iceberg.svg ./server/public/
