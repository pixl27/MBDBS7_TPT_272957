// Assignment est le "modèle mongoose", il est connecté à la base de données
var jwt = require('jsonwebtoken');
var bcrypt = require('bcryptjs');
var config = require('../config');

let Sequence = require("../model/sequence");

// Récupérer tous les assignments (GET), AVEC PAGINATION
function getSequences(req,res) {

    Sequence.find({}, (error, seq) => {
      if (error) {
        res.send(error);
      }
      res.json(seq);
    });
}

function updateSequence(req, res) {
    Sequence.updateOne({id: 0}, {
        $inc : {next : 1}
    }, function(err, affected, resp) {
       console.log(resp);
    })
  }

module.exports = {
    getSequences,
    updateSequence,
};
