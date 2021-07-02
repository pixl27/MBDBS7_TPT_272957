let express = require('express');
let app = express();
let bodyParser = require('body-parser');

let assignment = require('./routes/assignments');
let matiere = require('./routes/matieres');
var banque = require('./routes/banques');
var sequence = require('./routes/sequences');

var VerifyToken = require('./routes/VerifyToken');
var user = require('./routes/users');
var role = require('./routes/roles');
var historique = require('./routes/historiquesoldes');

let mongoose = require('mongoose');
mongoose.Promise = global.Promise;
//mongoose.set('debug', true);

// remplacer toute cette chaine par l'URI de connexion à votre propre base dans le cloud s
//const uri = 'mongodb+srv://mb:P7zM3VePm0caWA1L@cluster0.zqtee.mongodb.net/assignments?retryWrites=true&w=majority';
const uri = 'mongodb+srv://user:user@cluster0.f4uwm.mongodb.net/tpts?retryWrites=true&w=majority';

const options = {
  useNewUrlParser: true,
  useUnifiedTopology: true,
  useFindAndModify:false
};

mongoose.connect(uri, options)
  .then(() => {
    console.log("Connecté à la base MongoDB assignments dans le cloud !");
    console.log("at URI = " + uri);
    console.log("vérifiez with http://localhost:8010/api/assignments que cela fonctionne")
    },
    err => {
      console.log('Erreur de connexion: ', err);
    });

// Pour accepter les connexions cross-domain (CORS)
app.use((req, res, next) =>  {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,X-Access-Token");
  res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
  next();
});

// Pour les formulaires
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

let port = process.env.PORT || 8010;

// les routes
const prefix = '/api';

app.route(prefix + '/users')
.post(user.inscription)
.get(user.decode);

app.route(prefix + '/historiques')
.post(historique.postHistoriqueSolde)
app.route(prefix + '/historique')
.post(historique.getHistoriqueSoldeById)

app.route(prefix + '/parier')
.post(user.transaction)


app.route(prefix + '/user')
.post(user.login)
.get(user.logout)

  
  app.route(prefix + '/matieres')
  .get(matiere.getMatieres)
  
  app.route(prefix + '/matieres/:id')
  .get(matiere.getMatiere)

  app.route(prefix + '/banques')
  .get(banque.getBanques);

  app.route(prefix + '/sequences')
  .get(sequence.getSequences)
  .post(sequence.updateSequence)


  app.route(prefix + '/roles')
  .get(role.getRoles);

 // app.route(prefix + '/banque')
 // .post(user.login)
 // .get(user.logout)
// On démarre le serveur
app.listen(port, "0.0.0.0");
console.log('Serveur démarré  sur http://localhost:' + port);

module.exports = app;


