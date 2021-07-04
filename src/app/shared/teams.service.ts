import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError, filter, map, mergeMap, tap } from 'rxjs/operators';
import { Team } from '../list-teams/team.model';
import { TeamDetail } from '../list-teams/teamdetail.model';


@Injectable({
  providedIn: 'root'
})
export class TeamsService {
  Teams!:Team[];
  Team!:TeamDetail[];
  constructor(private http:HttpClient) { 
    this.getTeams()
    .subscribe(teams => {
        this.Teams = teams as Team[]
    })
  }

  //uri = "http://localhost:8010/api/assignments";
  uri = "https://backend-javaa-mbds272957.herokuapp.com/getallteam"
uriopendota = "https://api.opendota.com/api/teams";
  getTeams():Observable<Team[]> {
    console.log("Dans le service de gestion des match...")
    //return of(this.matieres);
    return this.http.get<Team[]>(this.uri);
  }
  getTeam(id:number):Observable<TeamDetail> {
    //let assignementCherche = this.assignments.find(a => a.id === id);

    //return of(assignementCherche);
  
        return this.http.get<TeamDetail>(this.uriopendota + "/" + id)
      
  }

}
