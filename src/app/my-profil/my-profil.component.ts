import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';
import { NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels } from 'ngx-qrcode2';
import { NgxSpinnerService } from "ngx-spinner";

@Component({
  selector: 'app-my-profil',
  templateUrl: './my-profil.component.html',
  styleUrls: ['./my-profil.component.css']
})
export class MyProfilComponent implements OnInit {
  tokenuser!:string
  me!:any;
  elementType = NgxQrcodeElementTypes.URL;
  correctionLevel = NgxQrcodeErrorCorrectionLevels.HIGH;
value !: string ;

  constructor(private authservice:AuthService,private router:Router,private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.spinner.show('sp6');
    let tokenuservar =  localStorage.getItem("usertoken");
    if(tokenuservar != null){
    this.tokenuser = tokenuservar
    this.value = tokenuservar
   this.getcurrentuser();
   }
  }
  getcurrentuser(){
  
    console.log(
      this.authservice.getCurrentUser(this.tokenuser).subscribe( 
        data => {
   
          this.me = data;
        },
        err => console.log("error")
  
  
      ).add(() => {    this.spinner.hide('sp6');
    })
      );
  }
}
