import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { BackOfficeService } from '../shared/backoffice.service';
import { MailAPI } from './MailAPI.model';

@Component({
  selector: 'app-backoffice-list-email',
  templateUrl: './backoffice-list-email.component.html',
  styleUrls: ['./backoffice-list-email.component.css']
})
export class BackofficeListEmailComponent implements OnInit {
  tokenuser!:string
  me!:any;
  pageOfItems!: Array<any>;
  emails!:MailAPI[];
  constructor(private backofficeservice:BackOfficeService,private spinner: NgxSpinnerService,private router:Router) { }

  ngOnInit(): void {
    this.spinner.show('sp6');
    let tokenuservar =  localStorage.getItem("usertoken");
    if(tokenuservar != null){
    this.tokenuser = tokenuservar
  
   }
  }
  onChangePage(pageOfItems: Array<any>) {
    // update current page of items
    this.pageOfItems = pageOfItems;
}
  getEmails(){

    console.log(

      this.backofficeservice.getEmails().subscribe( 
        result => {
          this.emails = result;
          console.log("email " + this.emails);
        },
        err => console.log("tsy nande pory")
  
  
      ).add(() => {this.spinner.hide('sp6');})
      );
  }
  deleteEmail(email:MailAPI){
    console.log(email.email);
    console.log(

      this.backofficeservice.deleteEmails(email).subscribe( 
        result => {
         
        },
        err => console.log("tsy nande pory")
  
  
      ).add(() => {this.spinner.hide('sp6');})
      );
  }
 
}
