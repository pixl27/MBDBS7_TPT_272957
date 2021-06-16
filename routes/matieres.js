// Assignment est le "modèle mongoose", il est connecté à la base de données
let Matiere = require("../model/matiere");


// Récupérer tous les assignments (GET), AVEC PAGINATION
function getMatieres(req,res) {

    Matiere.find({}, (error, matiere) => {
      if (error) {
        res.send(errpr);
      }
      res.json(matiere);
    });
}

// Récupérer un assignment par son id (GET)
function getMatiere(req, res) {
  let matiereId = req.params.id;
//fix
  Matiere.findOne({ _id: matiereId }, (err, matiere) => {
    if (err) {
      res.send(err);
    }
    res.json(matiere);
  });
}

module.exports = {
  getMatieres,
  getMatiere,
};
