### STAGE 1: Build ###
FROM node:12.7-alpine AS build
WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build --prod

### STAGE 2: Run ###
FROM nginx:1.17.1-alpine
COPY --from=build /usr/src/app/build /usr/share/nginx/html
