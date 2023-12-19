// get the client
const mysql = require("mysql2");

// Create the connection pool. The pool-specific settings are the defaults
const dbPool = mysql.createPool({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASS,
  database: process.env.DB_NAME,
});

// Export the pool
module.exports = dbPool.promise();
