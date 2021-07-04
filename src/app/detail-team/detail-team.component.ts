import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TeamDetail } from '../list-teams/teamdetail.model';
import { TeamsService } from '../shared/teams.service';
@Component({
  selector: 'app-detail-team',
  templateUrl: './detail-team.component.html',
  styleUrls: ['./detail-team.component.css']
})
export class DetailTeamComponent implements OnInit {
  teamTransmis!:TeamDetail;

  constructor(private teamsservice:TeamsService,
    private route:ActivatedRoute,
    private router:Router) { }

  ngOnInit(): void {
    this.getTeamById();
  }
  getTeamById() {
    // les params sont des string, on va forcer la conversion
    // en number en mettant un "+" devant
    const id: number = +this.route.snapshot.params.id;

    console.log('Dans ngOnInit de details, id = ' + id);
    this.teamsservice.getTeam(id).subscribe((team) => {
      this.teamTransmis = team;
    });
  }
}
