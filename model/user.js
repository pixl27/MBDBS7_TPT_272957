let mongoose = require('mongoose');
var aggregatePaginate = require("mongoose-aggregate-paginate-v2");

let Schema = mongoose.Schema;

let UserSchema = Schema({
    id: Number,
    username: String,
    password: String,
    idrole: Number,
    nom: String,
    prenom: String,
    solde: Number

    
});

UserSchema.plugin(aggregatePaginate);


// C'est à travers ce modèle Mongoose qu'on pourra faire le CRUD
module.exports = mongoose.model('User', UserSchema);
