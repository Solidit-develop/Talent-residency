const express = require('express');
const routes = require('./routes')
const config = require('./config');


const app = express();
const PORT = config.port;


app.use(express.json());
app.use('/api/v1/', routes)


app.listen(PORT, () => {
  console.log(`API Gateway running on port ${PORT}`);
});
