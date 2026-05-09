# Testing Project

This repository contains two RealWorld example apps:

- `node-express-realworld-example-app`: Express/Prisma backend
- `react-redux-realworld-example-app`: React/Redux frontend

## Run Locally

Install dependencies once in each app:

```bash
cd node-express-realworld-example-app
npm install
cd ../react-redux-realworld-example-app
npm install
```

Start the backend from the backend folder:

```bash
PORT=3001 npm start
```

Start the frontend from the frontend folder in a second terminal:

```bash
BROWSER=none npm start
```

Open:

```text
http://localhost:4100/
```

The frontend is configured to call the backend at:

```text
http://localhost:3001/api
```

If the frontend fails on a newer Node.js version with an OpenSSL/Webpack error, start it with:

```bash
NODE_OPTIONS=--openssl-legacy-provider BROWSER=none npm start
```
