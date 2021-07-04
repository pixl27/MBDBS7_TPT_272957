import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TeamsService } from '../shared/teams.service';
import { Team } from './team.model';

@Component({
  selector: 'app-list-teams',
  templateUrl: './list-teams.component.html',
  styleUrls: ['./list-teams.component.css']
})
export class ListTeamsComponent implements OnInit {
Teams!: Team[];
pageOfItems!: Array<any>;

  constructor(private teamsservice:TeamsService,
    private route:ActivatedRoute,
    private router:Router) { }

  ngOnInit(): void {
    this.getTeams();
  }
  onChangePage(pageOfItems: Array<any>) {
    // update current page of items
    this.pageOfItems = pageOfItems;
}
  getTeams() {
    this.teamsservice.getTeams()
    .subscribe(data => {

      this.Teams = data;
      console.log("données reçues");
    });
  }
}
