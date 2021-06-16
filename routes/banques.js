// Assignment est le "modèle mongoose", il est connecté à la base de données
var jwt = require('jsonwebtoken');
var bcrypt = require('bcryptjs');
var config = require('../config');

let Banque = require("../model/banque");

// Récupérer tous les assignments (GET), AVEC PAGINATION
function getBanques(req,res) {

    Banque.find({}, (error, banque) => {
      if (error) {
        res.send(error);
      }
      res.json(banque);
    });
}

function updateBanques(req, res,next) {
    console.log("UPDATE recu assignment : ");
    console.log(req.body);
    Banque.findByIdAndUpdate(
      req.body._id,
      req.body,
      { new: true },
      (err, banque) => {
        if (err) {
          console.log(err);
          res.send(err);
        } else {
          res.json({ message: "updated" });
        }
  
        // console.log('updated ', assignment)
      }
    );
  }

module.exports = {
    getBanques,
    updateBanques,
};
