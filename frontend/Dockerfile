FROM node:18-alpine

WORKDIR /app

COPY package*.json ./
RUN npm ci --only=production

COPY . .

RUN npm install tailwindcss postcss autoprefixer

RUN npm run build

EXPOSE 3000

CMD ["npm", "start"]