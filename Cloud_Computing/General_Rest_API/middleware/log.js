const logRequest = (req, res, next) => {
  console.log("Request received for path: " + req.path);
  next();
};

module.exports = logRequest;
