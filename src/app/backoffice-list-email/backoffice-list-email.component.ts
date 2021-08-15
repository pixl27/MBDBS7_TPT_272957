import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
  emailtodelete!:string;
  emailtoinsert!:string;

  closeResult = '';
  constructor(private backofficeservice:BackOfficeService,private modalService: NgbModal,private spinner: NgxSpinnerService,private router:Router) { }

  ngOnInit(): void {
    this.spinner.show('sp6');
    let tokenuservar =  localStorage.getItem("usertoken");
    if(tokenuservar != null){
    this.tokenuser = tokenuservar
  
   }
   this.getEmails();
  }
  onChangePage(pageOfItems: Array<any>) {
 
    this.pageOfItems = pageOfItems;
}
  getEmails(){

    console.log(

      this.backofficeservice.getEmails().subscribe( 
        result => {
          this.emails = result;
         
        },
        err => console.log("error")
  
  
      ).add(() => {this.spinner.hide('sp6');})
      );
  }
  deleteEmail(){

    this.spinner.show('sp6');
   
    console.log(

      this.backofficeservice.deleteEmails(this.emailtodelete).subscribe( 
        result => {
         
        },
        err => console.log("error")
  
  
      ).add(() => {this.getEmails()})
      );
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
  setemailtodelete(email:string){
    this.emailtodelete = email;
  }
  setemailtoinsert(email:string){
    this.emailtoinsert = email;
  }
  insertemail(){
    this.spinner.show('sp6');
    console.log(

      this.backofficeservice.insererEmail(this.emailtoinsert).subscribe( 
        result => {
         
        },
        err => console.log("error")
  
  
      ).add(() => {this.getEmails()})
      );
  }
  open(content:any, email:string) {
    this.setemailtodelete(email);
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }
  openinsert(content1:any) {
    this.modalService.open(content1, {ariaLabelledBy: 'modal-insert-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }
 
}
