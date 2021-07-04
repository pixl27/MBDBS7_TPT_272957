import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Match } from '../list-teams/match.model';
import { MatchDate } from '../list-teams/matchdate.model';
import { MatchsService } from '../shared/matchs.service';
import { NgxSpinnerService } from "ngx-spinner";
import { MatSelectModule } from "@angular/material/select";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
@Component({
  selector: 'app-list-match',
  templateUrl: './list-match.component.html',
  styleUrls: ['./list-match.component.css']
})
export class ListMatchComponent implements OnInit {

   Matchs!:Match[];
   MatchDate!:MatchDate[];
   listetournois!:string[];
   listeteams!:string[];
   MatchDateOld!:MatchDate[];
   MatchDateNew!:MatchDate[];
  selected="option 2";
  constructor(private matchsservice:MatchsService,
    private route:ActivatedRoute,
    private router:Router,private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
      
      console.log("Dans le subscribe des queryParams")
      this.spinner.show('sp6');


      this.getMatchs();

   
   
  }
 
  tri(option:any){
    var MatchDateNew:MatchDate[]=[];
    var Matchs:Match[]=[];
    var count=0
    this.MatchDate =  this.MatchDateOld;
    for (let i = 0; i < this.MatchDate.length; i++){
      for (let j = 0; j < this.MatchDate[i].listematch.length; j++){
        if(this.MatchDate[i].listematch[j].tournois == option){
          console.log("china " + this.MatchDate[i].listematch[j].tournois )
          Matchs.push(this.MatchDate[i].listematch[j]);
          count = count + 1;
        }
        
        else {
          continue
        }
      }
      if(count>0)
      {
        var MatchDateTemp:MatchDate=new MatchDate();
        MatchDateTemp.listematch = [];
        MatchDateTemp.listematch = Matchs;
        MatchDateTemp.temp = MatchDateTemp.listematch[0].datematch;
  
        MatchDateNew.push(MatchDateTemp)
        count = 0;
      }
     

    }
    return MatchDateNew;
  }

  trinom(option:any){
    var MatchDateNew:MatchDate[]=[];
    var Matchs:Match[]=[];
    var count=0
    this.MatchDate =  this.MatchDateOld;

    for (let i = 0; i < this.MatchDate.length; i++){
      for (let j = 0; j < this.MatchDate[i].listematch.length; j++){
        if(this.MatchDate[i].listematch[j].nomTeam1 == option || this.MatchDate[i].listematch[j].nomTeam2 == option ){
          Matchs.push(this.MatchDate[i].listematch[j]);
          count = count + 1;
        }
        
        else {
          continue
        }
      }
      if(count>0)
      {
        var MatchDateTemp:MatchDate=new MatchDate();
        MatchDateTemp.listematch = [];
        MatchDateTemp.listematch = Matchs;
        MatchDateTemp.temp = MatchDateTemp.listematch[0].datematch;
  
        MatchDateNew.push(MatchDateTemp)
        count = 0;
      }
     

    }
    return MatchDateNew;
  }


 onChange(value:any){
  if(value.value!="all"){

    this.MatchDateNew  = this.trinom(value.value);
   this.MatchDate =  this.MatchDateNew;
  }
  else {
    this.MatchDate = this.MatchDateOld;
  }
 }
  doSomething(value:any){
    if(value.value!="all"){
      this.MatchDateNew  = this.tri(value.value);
     this.MatchDate =  this.MatchDateNew;
    }
    else {
      this.MatchDate = this.MatchDateOld;
    }
   

  }
   checkdate(MatchDateNew:MatchDate[],Datetest:Date){
    console.log("mi check date");

    for (let i = 0; i < MatchDateNew.length; i++) {
    if(MatchDateNew[i].temp == Datetest)
    {
      console.log("mitovy");
      return i;
    }
    }
    return null;

  }

  

  sortmatchbydate(Matchs:Match[]){
   var MatchDateNew:MatchDate[]=[];

   for (let i = 0; i < Matchs.length; i++) {

   var indice = this.checkdate(MatchDateNew,Matchs[i].datematch);
   if(indice!=null)
   MatchDateNew[indice].listematch.push(Matchs[i]);
   else 
   {
    var MatchDateTemp:MatchDate=new MatchDate();
    MatchDateTemp.temp = Matchs[i].datematch;
    console.log(MatchDateTemp + "ok");
    MatchDateTemp.listematch=[];
    MatchDateTemp.listematch.push(Matchs[i]);
    console.log(MatchDateTemp + "tafiditra");

    MatchDateNew.push(MatchDateTemp);

   }
  }
  return MatchDateNew;
  }
  getMatchs() {
    this.matchsservice.getMatchs()
    .subscribe(data => {

      this.Matchs = data;
      console.log("données reçues");
    }).add(() => {

this.MatchDate = this.sortmatchbydate(this.Matchs);
this.MatchDateOld = this.MatchDate;
var listet:string[]=[];
var listen:string[]=[];

for (let i = 0; i < this.MatchDate.length; i++){
  for (let j = 0; j < this.MatchDate[i].listematch.length; j++){
    var a ="";
    var b = "";
    var c = "";
     a = this.MatchDate[i].listematch[j].tournois;
     b = this.MatchDate[i].listematch[j].nomTeam1;
     c = this.MatchDate[i].listematch[j].nomTeam2;
     listet.push(a);
     listen.push(b);
     listen.push(c);

  }
}
listet = Array.from(listet.reduce((m, t) => m.set(t, t), new Map()).values());
listen = Array.from(listen.reduce((m, t) => m.set(t, t), new Map()).values());

this.listetournois = listet;
this.listeteams = listen;
this.spinner.hide('sp6');

 });
    ;
  }
}
