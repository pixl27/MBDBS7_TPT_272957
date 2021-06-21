// Assignment est le "modèle mongoose", il est connecté à la base de données
var jwt = require('jsonwebtoken');
var bcrypt = require('bcryptjs');
var config = require('../config');

let Role = require("../model/role");

// Récupérer tous les assignments (GET), AVEC PAGINATION
function getRoles(req,res) {

  Role.find({}, (error, role) => {
      if (error) {
        res.send(error);
      }
      res.json(role);
    });
}



module.exports = {
    getRoles,
    
};
