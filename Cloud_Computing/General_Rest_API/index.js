const express = require("express");
const session = require("express-session");
const passport = require("passport");
const GoogleStrategy = require("passport-google-oauth20").Strategy;
const bodyParser = require("body-parser");
const axios = require("axios");
const dotenv = require("dotenv");
dotenv.config();
const ML_API_URL = process.env.ML_API_URL || "http://localhost:8080";

const userRoutes = require("./routes/user.js");
const middlewareLogRequest = require("./middleware/log.js");
const usersController = require("./controller/user.js");

const app = express();
const PORT = process.env.PORT || 5000;
app.use(
  session({
    secret: process.env.SESSION_SECRET_KEY,
    resave: true,
    saveUninitialized: true,
  })
);
app.use(passport.initialize());
app.use(passport.session());
app.use(middlewareLogRequest);
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use("/user", userRoutes);

// Passport Configuration
passport.use(
  new GoogleStrategy(
    {
      clientID: process.env.GOOGLE_CLIENT_ID,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET,
      callbackURL: process.env.GOOGLE_CALLBACK_URL || "http://localhost:5000/auth/google/callback",
    },
    (accessToken, refreshToken, profile, done) => {
      // Use profile information to create or update user in your database
      // For simplicity, let's assume you have a function `findOrCreateUser` in your controller
      usersController.findOrCreateUser(profile, accessToken, done);
    }
  )
);

passport.serializeUser((user, done) => {
  done(null, user);
});

passport.deserializeUser((obj, done) => {
  done(null, obj);
});

app.get("/", (req, res) => {
  res.status(200).json({ success: true, data: "MedicVisionScan API" });
});

// Google OAuth Routes
app.get(
  "/auth/google",
  passport.authenticate("google", { scope: ["profile", "email"] })
);

app.get(
  "/auth/google/callback",
  passport.authenticate("google", { failureRedirect: "/" }),
  (req, res) => {
    // Successful authentication, redirect or respond as needed
    res.redirect("/api/profile");
  }
);

app.post(
  "/auth/google/access_token",
  passport.authenticate("google", { failureRedirect: "/" }),
  (req, res) => {
    if (req.isAuthenticated()) {
      res.status(200).json({ success: true, data: req.user, access_token: req.user.access_token});
    } else {
      res.status(401).send({ success: false, message: "Unauthorized" });
    }
  }
);

app.get("/api/profile", (req, res) => {
  // Ensure user is authenticated
  if (req.isAuthenticated()) {
    // Access user information from req.user
    res.status(200).send({
      success: true,
      message: { user: req.user.name, email: req.user.email },
    });
  } else {
    res.status(401).send({ success: false, message: "Unauthorized" });
  }
});

app.post("/api/predict", (req, res) => {
  if (req.isAuthenticated()) {
    const { data } = req.body;
    filename = data.filename;
    b64_img_data = data.b64_img_data;

    axios
      .post(`${ML_API_URL}/predict`, {
        data: {
          filename: filename,
          b64_img_data: b64_img_data,
        },
      })
      .then(function (response) {
        res.status(200).json({ success: true, data: response.data });
      })
      .catch(function (error) {
        res.status(500).json({ success: false, data: error });
      });
  } else {
    res.status(401).send({ success: false, message: "Unauthorized" });
  }
});

app.listen(PORT, () => console.log(`Server running at port: ${PORT}`));
