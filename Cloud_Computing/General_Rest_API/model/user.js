const dbPool = require("../config/user.js");

const getAllUsers = () => {
  const SQLQuery = "SELECT * FROM user";
  return dbPool.execute(SQLQuery);
};

const createNewUser = (body) => {
  const SQLQuery = `INSERT INTO user (name, email) VALUES ('${body.name}', '${body.email}')`;
  return dbPool.execute(SQLQuery);
};

const updateUserById = (id, body) => {
  const SQLQuery = `UPDATE user SET name='${body.name}', email='${body.email}' WHERE id=${id}`;
  return dbPool.execute(SQLQuery);
};

const deleteUserById = (id) => {
  const SQLQuery = `DELETE FROM user WHERE id=${id}`;
  return dbPool.execute(SQLQuery);
};
const getUserByEmail = async (email) => {
  const [rows] = await dbPool.query(
    "SELECT * FROM user WHERE email = ?",
    email
  );
  return rows[0];
};

const createNewUserGoogle = (body) => {
  const SQLQuery = `INSERT INTO user (name, email) VALUES ('${body.name}', '${body.email}')`;
  return dbPool.execute(SQLQuery);
};

module.exports = {
  getAllUsers,
  createNewUser,
  updateUserById,
  deleteUserById,
  getUserByEmail,
  createNewUserGoogle,
};
