import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError, filter, map, mergeMap, tap } from 'rxjs/operators';
import { MailAPI } from '../backoffice-list-email/MailAPI.model';
import { Probleme } from '../backoffice-list-problem/Probleme.model';
import { Historique } from '../historique/historique.model';


@Injectable({
  providedIn: 'root'
})
export class BackOfficeService {
  Emails!:MailAPI[];

  constructor(private http:HttpClient) { 
    this.getEmails()
    .subscribe(emails => {
        this.Emails = emails as MailAPI[]
    })
  }

  //uri = "http://localhost:8010/api/assignments";
  uri = "https://backend-javaa-mbds272957.herokuapp.com/"

  getEmails():Observable<MailAPI[]> {
    console.log("Dans le service de gestion des match...")
    //return of(this.matieres);
    return this.http.get<MailAPI[]>(this.uri + "getAllEmailAdmin");
  }
  getProblemes():Observable<Probleme[]> {
    console.log("Dans le service de gestion des match...")
    //return of(this.matieres);
    return this.http.get<Probleme[]>(this.uri + "getAllProbleme");
  }
  deleteEmails(email:string):Observable<any> {
    return this.http.post<any>(this.uri + "deleteEmailAdmin", {email});

  }
  insererEmail(email:string):Observable<any> {
    return this.http.post<any>(this.uri + "insererEmailAdmin", {email});

  }
  fairegagner(idParis:Number):Observable<any>{
console.log("enfoiré " + idParis);
    return this.http.post<any>(this.uri + "finaliserManuelWin", idParis);

  }
  faireperdre(idParis:Number):Observable<any>{
    return this.http.post<any>(this.uri + "finaliserManuelLoss",idParis);

  }
  
}
