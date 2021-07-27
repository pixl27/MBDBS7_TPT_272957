import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError, filter, map, mergeMap, tap } from 'rxjs/operators';
import { MailAPI } from '../backoffice-list-email/MailAPI.model';
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
  uri = "https://backend-javaa-mbds272957.herokuapp.com/getAllEmailAdmin"

  getEmails():Observable<MailAPI[]> {
    console.log("Dans le service de gestion des match...")
    //return of(this.matieres);
    return this.http.get<MailAPI[]>(this.uri);
  }


}
