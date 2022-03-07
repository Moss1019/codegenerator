FROM node

COPY ./server/public/* /var/app/site/

WORKDIR /var/app/site

RUN npm ic
# ENTRYPOINT ["npm", "run", "start"]
ENTRYPOINT ["bash"]
