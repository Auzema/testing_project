# Testing Project

This repository contains the RealWorld example apps and Katalon automation project:

- `node-express-realworld-example-app`: Express/Prisma backend
- `react-redux-realworld-example-app`: React/Redux frontend
- `Katalon-conduit`: Katalon Studio test automation project with web regression and data-driven tests

Project documents:

- [AI-Supported Test Data Generation Plan](AI-SUPPORTED-TEST-DATA-GENERATION-PLAN.md)
- [Run App and Katalon Guide](RUN_APP_AND_KATALON_GUIDE.md)

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
