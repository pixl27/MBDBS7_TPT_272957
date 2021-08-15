import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Team } from '../list-teams/team.model';
import { MatchsService } from '../shared/matchs.service';
import { TeamsService } from '../shared/teams.service';
import { MatchDetail } from './matchdetail.model';
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from "ngx-spinner";
import { AuthService } from '../shared/auth.service';
import { interval ,timer} from 'rxjs';
import { map } from 'rxjs/operators'
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-detail-match',
  templateUrl: './detail-match.component.html',
  styleUrls: ['./detail-match.component.css']
})
export class DetailMatchComponent implements OnInit {
  matchtransmis!:MatchDetail
  model: any = {};
  myDate !: any;
  counter!:any;
  closeResult = '';
  tokenuser!:string
  me!:any;
  idUser!:number
  idMatch!: number
  type!: string
  idTeamParier!: number
  montant!: number
  odds!: number
  date !: any ;
  myForm = new FormGroup({});

  constructor(private matchsservice:MatchsService,
    private route:ActivatedRoute,
    private router:Router,private toastr: ToastrService,private modalService: NgbModal,private spinner: NgxSpinnerService,private authservice:AuthService,private formBuilder: FormBuilder) 
  {
   
  }
  get f() { return this.myForm.controls; }

  ngOnInit(): void {
    this.spinner.show('sp6');
    this.getMatchById();
    let tokenuservar =  localStorage.getItem("usertoken");
   
   console.log(this.counter);
    //this.myDate = this.datePipe.transform(this.myDate, 'yyyy-MM-dd');
    if(tokenuservar != null){
    this.tokenuser = tokenuservar
   this.getcurrentuser();
   }
   
  }
  showtoast(title:string,body:string,type:string) {
    if(type=="success"){
        this.toastr.success(body, title);

    }
    else {
        this.toastr.error(body, title);

    }
  }
 
  getMatchById() {
    // les params sont des string, on va forcer la conversion
    // en number en mettant un "+" devant
    const id: number = +this.route.snapshot.params.id;

    this.matchsservice.getMatch(id).subscribe((match) => {
      this.matchtransmis = match;
      var sets = match.time.toString().replace("T"," ").replace("Z","");
   //   console.lo
      this.date = new Date(sets).getTime();
      this.date = this.date + 3*60*60*1000;
      console.log("seconds",this.date);
    }).add(() => {
      this.myForm = this.formBuilder.group({     
        numbercontrol: ['', [Validators.min(0), Validators.max(this.me.solde)]]
    }); 
    
      this.spinner.hide('sp6');
    

       });
  }
  getcurrentuser(){
  
    console.log(
      this.authservice.getCurrentUser(this.tokenuser).subscribe( 
        data => {
          this.me = data;
        },
        err => console.log("error")
  
  
      )
      );
  }

  open(content:any, idMatch:number,type:string,idTeamParier:number,odds:number) {
    this.setdefaultattribute(idMatch,type,idTeamParier,odds);
  
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
    this.spinner.show('sp6');
    if(this.montant == 0 || this.montant==null){
      this.spinner.hide('sp6');

      this.showtoast("Echec de paris","veuillez entrer un montant valide","error");

    }
    else {
     
      console.log(
        this.matchsservice.bet(this.idUser,this.idMatch,this.type,this.idTeamParier,this.montant,this.odds).subscribe( 
          data => {
            this.router.navigate(["/"]);
            this.showtoast("Paris Effectuer","Votre paris a été bien enregistrer","success");
    
          },
          err => {this.showtoast("Paris Effectuer","Votre paris a été bien enregistrer","success");this.spinner.hide('sp6');

        }
    
    
        ).add(() => {  this.spinner.hide('sp6');})
        );
       
    }
   

  }



}
