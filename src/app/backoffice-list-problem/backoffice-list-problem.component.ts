import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { BackOfficeService } from '../shared/backoffice.service';
import { Probleme } from './Probleme.model';


@Component({
  selector: 'app-backoffice-list-problem',
  templateUrl: './backoffice-list-problem.component.html',
  styleUrls: ['./backoffice-list-problem.component.css']
})

export class BackofficeListProblemComponent implements OnInit {

  tokenuser!:string
  me!:any;
  pageOfItems!: Array<any>;
  problemes!:Probleme[];
  

  closeResult = '';
  constructor(private backofficeservice:BackOfficeService,private toastr: ToastrService,private spinner: NgxSpinnerService,private router:Router) { }

  ngOnInit(): void {
    this.spinner.show('sp6');
    let tokenuservar =  localStorage.getItem("usertoken");
    if(tokenuservar != null){
    this.tokenuser = tokenuservar
  
   }
   this.getProblemes();
  }
  onChangePage(pageOfItems: Array<any>) {
    // update current page of items
    this.pageOfItems = pageOfItems;
}
getProblemes(){

    console.log(

      this.backofficeservice.getProblemes().subscribe( 
        result => {
          this.problemes = result;
        },
        err => console.log("error")
  
  
      ).add(() => {this.spinner.hide('sp6');})
      );
  }
  showSuccess(title:string,body:string) {
        this.toastr.success(body, title);

  }
  fairegagner(idParis:string){
    this.spinner.show('sp6');

    console.log(

      this.backofficeservice.fairegagner(Number(idParis)).subscribe(
        result => {
this.showSuccess("Paris",result.message)        
},
        err => console.log("Error")
      ).add(() => {this.getProblemes()})
      );
  }
  faireperdre(idParis:string){
    this.spinner.show('sp6');

    console.log(

      this.backofficeservice.faireperdre(Number(idParis)).subscribe( result => {
        this.showSuccess("Paris",result.message)   
       },
       err => console.log("error")).add(() => {this.getProblemes()})
      );
  }
}
