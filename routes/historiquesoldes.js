// Assignment est le "modèle mongoose", il est connecté à la base de données
var jwt = require('jsonwebtoken');
var bcrypt = require('bcryptjs');
var config = require('../config');
const { ObjectId } = require('mongodb');

let Historiquesolde = require("../model/historiquesolde");

// Récupérer tous les assignments (GET), AVEC PAGINATION
function getHistoriqueSoldeById(req,res) {
    let iduser = req.params.id;

    Historiquesolde.find({"iduser":iduser}, (error, seq) => {
      if (error) {
        res.send(error);
      }
      res.json(seq);
    });
}

function postHistoriqueSolde(req, res) {
    let historique = new Historiquesolde();
    historique.iduser =  ObjectId(req.body.iduser);
    historique.montant = req.body.montant;
    historique.type = req.body.type;
    historique.idparis = req.body.idparis;
    historique.datehistorique = req.body.datehistorique;

    historique.save((err) => {
      if (err) {
        res.send("cant post assignment ", err);
      }
      res.json({ message: `historique saved!` });
    });
  }

module.exports = {
    getHistoriqueSoldeById,
    postHistoriqueSolde,
};
