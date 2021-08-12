import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';
import { MatchsService } from '../shared/matchs.service';
import { Historique } from './historique.model';
import { NgxSpinnerService } from "ngx-spinner";
import { MessagingService } from '../shared/messaging.service';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-historique',
  templateUrl: './historique.component.html',
  styleUrls: ['./historique.component.css']
})
export class HistoriqueComponent implements OnInit {
  tokenuser!:string
  me!:any;
  pageOfItems!: Array<any>;
  myhistorique!:Historique[];
  @ViewChild('appcomponent', { static: false }) appcomponent!: AppComponent;

  constructor(private authservice:AuthService,private spinner: NgxSpinnerService,private matchservice:MatchsService,private route:ActivatedRoute,private router:Router,public messageservice:MessagingService) { }

  ngOnInit(): void {
    if(+this.route.snapshot.params.id!=null){
    
      if(+this.route.snapshot.params.vue==0)
      {
        this.setnotiftovu(+this.route.snapshot.params.id);
        this.messageservice.setnotifnumber(this.messageservice.notifnumber - 1);
       
      }
    
 
    
    }
    this.spinner.show('sp6');
    let tokenuservar =  localStorage.getItem("usertoken");
    if(tokenuservar != null){
    this.tokenuser = tokenuservar
   this.getcurrentuser();
   }
  }
  onChangePage(pageOfItems: Array<any>) {
    // update current page of items
    this.pageOfItems = pageOfItems;
}
  getHistorique(){
    console.log("userid" + this.me._id)

    console.log(

      this.matchservice.getHistorique(this.me._id).subscribe( 
        result => {
          this.myhistorique = result;
          console.log("historique " + this.myhistorique);
        },
        err => console.log("tsy nande pory")
  
  
      ).add(() => {this.spinner.hide('sp6');})
      );
  }
  setnotiftovu(id:number){
    this.messageservice.setnotifvue(id).subscribe( 
      result => {
     
      },
      err => console.log("tsy nande pory")
    )
  }
  getcurrentuser(){
  
    console.log(
      this.authservice.getCurrentUser(this.tokenuser).subscribe( data => {
    console.log(data.solde + "EZ");
          this.me = data;
        }).add(() => {

        this.getHistorique();
      })
      );
    
  }

}
