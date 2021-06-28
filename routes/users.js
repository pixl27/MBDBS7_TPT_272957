// Assignment est le "modèle mongoose", il est connecté à la base de données
var jwt = require('jsonwebtoken');
var bcrypt = require('bcryptjs');
var config = require('../config');

let User = require("../model/user");
const { ObjectId } = require('mongodb');

function transaction(req,res) {
  if(req.body.type == "debit"){
    res.json({ message: "debit ok " });
    User.update({id:0}, {solde:req.body.solde});

  }
  else {
    User.update({id:0}, {solde:0});

  }
  res.json({ message: "updated" });
}
function inscription(req, res) {
  
    var hashedPassword = bcrypt.hashSync(req.body.password, 8);
    
    User.create({
      id:getNextSequenceValue("id"),
      username : req.body.username,
      password : hashedPassword,
      idrole: req.body.idrole,
      nom: req.body.nom,
      prenom: req.body.prenom,
      solde: req.body.solde
    },
    function (err, user) {
      if (err) return res.status(500).send("There was a problem registering the user.")
      // create a token
      var token = jwt.sign({ id: user._id }, config.secret, {
        expiresIn: 86400 // expires in 24 hours
      });
      res.status(200).send({ auth: true, token: token });
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
