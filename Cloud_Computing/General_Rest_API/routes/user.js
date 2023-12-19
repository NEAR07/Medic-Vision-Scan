const express = require('express');
const router = express.Router();
const usersController = require('../controller/user.js');

//create - post
router.post('/', usersController.createNewUser);

// read-get
router.get('/', usersController.getAllUsers);

// update - patch
router.patch('/:id', usersController.updateUserById);

// delete 
router.delete('/:id', usersController.deleteUserById);

module.exports = router;