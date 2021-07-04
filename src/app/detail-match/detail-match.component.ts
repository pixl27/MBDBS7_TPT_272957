import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Team } from '../list-teams/team.model';
import { MatchsService } from '../shared/matchs.service';
import { TeamsService } from '../shared/teams.service';
import { MatchDetail } from './matchdetail.model';
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from "ngx-spinner";
import { AuthService } from '../shared/auth.service';

@Component({
  selector: 'app-detail-match',
  templateUrl: './detail-match.component.html',
  styleUrls: ['./detail-match.component.css']
})
export class DetailMatchComponent implements OnInit {
  matchtransmis!:MatchDetail
  model: any = {};
  closeResult = '';
  tokenuser!:string
  me!:any;
  idUser!:number
  idMatch!: number
  type!: string
  idTeamParier!: number
  montant!: number
  odds!: number
  constructor(private matchsservice:MatchsService,
    private route:ActivatedRoute,
    private router:Router,private modalService: NgbModal,private spinner: NgxSpinnerService,private authservice:AuthService) { }

  ngOnInit(): void {
    this.spinner.show('sp6');
    let tokenuservar =  localStorage.getItem("usertoken");
    if(tokenuservar != null){
    this.tokenuser = tokenuservar
   this.getcurrentuser();
   }
    this.getMatchById();
  }
 
  getMatchById() {
    // les params sont des string, on va forcer la conversion
    // en number en mettant un "+" devant
    const id: number = +this.route.snapshot.params.id;

    console.log('Dans ngOnInit de details, id = ' + id);
    this.matchsservice.getMatch(id).subscribe((match) => {
      this.matchtransmis = match;
    }).add(() => {

      this.spinner.hide('sp6');
      
       });
  }
  getcurrentuser(){
  
    console.log(
      this.authservice.getCurrentUser(this.tokenuser).subscribe( 
        data => {
          this.me = data;
        },
        err => console.log("tsy nande pory")
  
  
      )
      );
  }

  open(content:any, idMatch:number,type:string,idTeamParier:number,odds:number) {
    this.setdefaultattribute(idMatch,type,idTeamParier,odds);
    console.log("Match Id Rivalry " + this.idMatch);
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  setdefaultattribute(idMatch:number,type:string,idTeamParier:number,odds:number){
    this.idUser=this.me._id;
    this.idMatch= idMatch;
    this.type= type;
    this.idTeamParier=idTeamParier;
    this.odds= odds;
  }

  bet(){
    console.log(this.idUser +" - " +this.montant)
    
    console.log(
      this.matchsservice.bet(this.idUser,this.idMatch,this.type,this.idTeamParier,this.montant,this.odds).subscribe( 
        data => {
          this.router.navigate(["/"]);
  
        },
        err => console.log("paris failed")
  
  
      )
      );

  }



}
