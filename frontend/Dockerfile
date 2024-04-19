FROM node:latest as build

WORKDIR /usr/local/app

COPY /src/main/angular /usr/local/app

RUN npm i && npm run build

FROM nginx:latest

COPY --from=build /usr/local/app/dist/browser /usr/share/nginx/html

EXPOSE 80