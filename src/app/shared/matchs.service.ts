import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError, filter, map, mergeMap, tap } from 'rxjs/operators';
import { MatchDetail } from '../detail-match/matchdetail.model';
import { Historique } from '../historique/historique.model';
import { Match } from '../list-teams/match.model';
import { timeout} from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class MatchsService {
  Matchs!:Match[];

  constructor(private http:HttpClient) { 
    this.getMatchs()
    .subscribe(matchs => {
        this.Matchs = matchs as Match[]
    })
  }

  uri = "https://backend-javaa-mbds272957.herokuapp.com/"
  urihistorique ="https://backend-node-mbds272957.herokuapp.com/api/historiques"

  getMatchs():Observable<Match[]> {
    return this.http.get<Match[]>(this.uri + "getallmatchtest");
  }


  getMatch(id:number):Observable<MatchDetail> {
   
  
        return this.http.get<MatchDetail>(this.uri + "getmatchbyIdRivalry?idRivalry=" + id).pipe(
          timeout(10000)
      );
      
  }
  
  getHistorique(iduser:string):Observable<Historique[]>{
    return this.http.get<Historique[]>(this.urihistorique + "/" + iduser)
  }

  bet(idUser:number, idMatch:number,type:string,idTeamParier:number,montant:number,odds:number):Observable<any> {
    return this.http.post<any>(this.uri + "parier" , { idUser, idMatch,type,idTeamParier,montant,odds }).pipe(map(resultat => {
  
      return resultat;
  }));
  
  }

}
