let mongoose = require('mongoose');
var aggregatePaginate = require("mongoose-aggregate-paginate-v2");

let Schema = mongoose.Schema;

let HistoriquesoldeSchema = Schema({
    iduser!:String,
    montant:Number,
    type!:String,
    idparis!:Number,
    datehistorique!:Date
});

HistoriquesoldeSchema.plugin(aggregatePaginate);


// C'est à travers ce modèle Mongoose qu'on pourra faire le CRUD
module.exports = mongoose.model('Historiquesolde', HistoriquesoldeSchema);
