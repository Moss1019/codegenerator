FROM node
# FROM arm64v8/node

COPY ./server/ /var/app/site

EXPOSE 3000

WORKDIR /var/app/site

RUN npm ic
CMD ["npm", "run", "start"]
