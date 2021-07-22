// Assignment est le "modèle mongoose", il est connecté à la base de données
var jwt = require('jsonwebtoken');
var bcrypt = require('bcryptjs');
var config = require('../config');

let User = require("../model/user");
let Historiquesolde = require ("../model/historiquesolde")
let Sequence = require("../model/sequence");
let Banque = require("../model/banque");

const { ObjectId } = require('mongodb');

const tokenadmin = "c2FtYmF0cmFpemF5bWlub2ZhdHN5bWFoaXRh"


//auto build node
function transaction(req,res) {
  if(req.body.token == tokenadmin){

  
  if(req.body.type == "debit"){
    User.updateOne({_id: ObjectId(req.body.iduser)}, {
      $inc : {solde : req.body.montant}
  }, function(err, affected, resp) {
     console.log(resp);
  })

  Banque.updateOne({_id: ObjectId("60c315e614982b5375e5a537")}, {
    $inc : {montant : -req.body.montant}
}, function(err, affected, resp) {
   console.log(resp);
})

  let historique = new Historiquesolde();
  historique.iduser =  ObjectId(req.body.iduser);
  historique.montant = req.body.montant;
  historique.type = req.body.type;
  historique.idparis = req.body.idparis;
  historique.description = req.body.description;
  historique.datehistorique = req.body.datehistorique;

  historique.save((err) => {
    if (err) {
      console.log(err);
    }
    console.log("historique saved");
  });
  /// Historique Banque

  let historiquebanque = new Historiquesolde();
  historiquebanque.iduser =  ObjectId("60c315e614982b5375e5a537");
  historiquebanque.montant = req.body.montant;
  historiquebanque.type = "credit";
  historiquebanque.idparis = req.body.idparis;
  historiquebanque.description = req.body.description;
  historiquebanque.datehistorique = req.body.datehistorique;

  historiquebanque.save((err) => {
    if (err) {
      console.log(err);
    }
    console.log("historique saved");
  });


  }
  else {
    User.updateOne({_id: ObjectId(req.body.iduser)}, {
      $inc : {solde : -req.body.montant}
  }, function(err, affected, resp) {
     console.log(resp);
  })
  Banque.updateOne({_id: ObjectId("60c315e614982b5375e5a537")}, {
    $inc : {montant : req.body.montant}
}, function(err, affected, resp) {
   console.log(resp);
})
  let historique = new Historiquesolde();
  historique.iduser =  ObjectId(req.body.iduser);
  historique.montant = req.body.montant;
  historique.type = req.body.type;
  historique.idparis = req.body.idparis;
  historique.description = req.body.description;
  historique.datehistorique = req.body.datehistorique;

  historique.save((err) => {
    if (err) {
      console.log(err)
    }
    console.log("historique saved");
  });

  //Historique Banque

  let historiquebanque = new Historiquesolde();
  historiquebanque.iduser =  ObjectId("60c315e614982b5375e5a537");
  historiquebanque.montant = req.body.montant;
  historiquebanque.type = "debit";
  historiquebanque.idparis = req.body.idparis;
  historiquebanque.description = req.body.description;
  historiquebanque.datehistorique = req.body.datehistorique;

  historiquebanque.save((err) => {
    if (err) {
      console.log(err);
    }
    console.log("historique saved");
  });


  }
  res.json({ message: "updated" });
}
else {
  res.json({ message: "token needed" });

}
}
function inscription(req, res) {
  
    var hashedPassword = bcrypt.hashSync(req.body.password, 8);

    //change this commentary again
    User.findOne({ username: req.body.username }, function (err, user) {
      if(user==null){
        User.create({
          id:req.body.sequence,
          username : req.body.username,
          password : hashedPassword,
          idrole: 1,
          nom: req.body.nom,
          prenom: req.body.prenom,
          solde: 5000
        },
        function (errr, userinsert) {
          if (errr) return res.status(500).send("There was a problem registering the user.")
          // create a token
          var token = jwt.sign({ id: userinsert._id }, config.secret, {
            expiresIn: 86400 // expires in 24 hours
          });
          res.status(200).send({ auth: true, token: token });
        });
      }
      else {
        res.status(400).send({message:"pseudo deja pris" });
      }
    });
    
    
  }

  function decode(req, res) {
    var token = req.headers['x-access-token'];
    if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });
    
    jwt.verify(token, config.secret, function(err, decoded) {
      if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
      
      User.findById(decoded.id, 
        { password: 0 }, function (err, user) {
        if (err) return res.status(500).send("There was a problem finding the user.");
        if (!user) return res.status(404).send("No user found.");
        
        res.status(200).send(user);
      });
    });

  }

  function login(req, res) {

    User.findOne({ username: req.body.username }, function (err, user) {
      if (err) return res.status(500).send('Error on the server.');
      if (!user) return res.status(404).send('No user found.');
      
      var passwordIsValid = bcrypt.compareSync(req.body.password, user.password);
      if (!passwordIsValid) return res.status(401).send({ auth: false, token: null });
      
      var token = jwt.sign({ id: user._id }, config.secret, {
        expiresIn: 86400 // expires in 24 hours
      });
      
      res.status(200).send({ auth: true, token: token });
    });
    
  }

  function logout(req, res) {
    res.status(200).send({ auth: false, token: null });
  }
module.exports = {
    decode,
  inscription,
  login,
  logout,
  transaction
};
