import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';
import { MatchsService } from '../shared/matchs.service';
import { Historique } from './historique.model';
import { NgxSpinnerService } from "ngx-spinner";

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
  constructor(private authservice:AuthService,private spinner: NgxSpinnerService,private matchservice:MatchsService,private router:Router) { }

  ngOnInit(): void {
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
          console.log("historique " + this.myhistorique.length);
        },
        err => console.log("tsy nande pory")
  
  
      ).add(() => {this.spinner.hide('sp6');})
      );
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
