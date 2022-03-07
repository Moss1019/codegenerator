
# npm ic
npm run build

cd server 

rm -r ./public
# npm ic
npm run build

cd ..

mv ./build/* ./server/public
