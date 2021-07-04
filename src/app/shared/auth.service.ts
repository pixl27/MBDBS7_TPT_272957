import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError, filter, map, mergeMap, tap } from 'rxjs/operators';
import { Sequence } from './sequence.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  loggedIn = false;
  admin = true;
  seq!:any;
  constructor(private http:HttpClient) { 
   
  }
  uri = "https://backend-node-mbds272957.herokuapp.com/api/user"
  uriseq = "https://backend-node-mbds272957.herokuapp.com/api/sequences"
/*
  logIn(login, password) {
    // typiquement, acceptera en paramètres un login et un password
    // vérifier qu'ils sont ok, et si oui, positionner la propriété loggedIn à true
    // si login/password non valides, positionner à false;

    if (login === 'admin') this.admin = true;

    this.loggedIn = true;
  }
*/
logIn(username:string, password:string):Observable<any> {
  return this.http.post<any>(this.uri , { username, password }).pipe(map(resultat => {
    // store user details and jwt token in local storage to keep user logged in between page refreshes
    let tokenuser = JSON.stringify(resultat["token"]).replace(/['"]+/g, '');
    localStorage.setItem('usertoken', tokenuser);

    return resultat;
}));

}

getSequence():Observable<Sequence> {
  return this.http.get<Sequence>(this.uriseq).pipe(map(resultat => {
    return resultat;
}));

}

updateSequence():Observable<any> {
  return this.http.post<any>(this.uriseq,{}).pipe(map(resultat => {
    return resultat;
}));

}

signUp(nom:string,prenom:string,username:string, password:string):Observable<any> {
  this.seq = this.getSequence();
  let sequence = this.seq.next;

  return this.http.post<any>(this.uri + 's' , { sequence,nom,prenom,username, password }).pipe(map(resultat => {
    // store user details and jwt token in local storage to keep user logged in between page refreshes
    let tokenuser = JSON.stringify(resultat["token"]).replace(/['"]+/g, '');
    localStorage.setItem('usertoken', tokenuser);

    return resultat;
}));
this.updateSequence();

}


getCurrentUser(token:string):Observable<any> {
  console.log("token " + token);
  return this.http.get<any>(this.uri + "s",{
    headers: {'x-access-token':token}
 }).pipe(map(resultat => {

    return resultat;
}));

}

  logOut() {
   localStorage.removeItem("usertoken")
  }

  // exemple d'utilisation :
  // isAdmin.then(admin => { console.log("administrateur : " + admin);})
  isAdmin() {
    return new Promise((resolve, reject) => {
      resolve(this.admin);
    });
  }
}
