import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';
import { NgxSpinnerService } from "ngx-spinner";

@Component({
  selector: 'app-acceuil',
  templateUrl: './acceuil.component.html',
  styleUrls: ['./acceuil.component.css']
})
export class AcceuilComponent implements OnInit {

  constructor(private authservice:AuthService,private router:Router) { }
tokenuser!:string
  ngOnInit(): void {
    let tokenuservar =  localStorage.getItem("usertoken");
    if(tokenuservar != null){
    this.tokenuser = tokenuservar
  
   }
  }
  
  getcurrentuser(){
  
    console.log(
      this.authservice.getCurrentUser(this.tokenuser).subscribe( 
        data => {

  
        },
        err => console.log("erreur")
  
  
      )
      );
  }

}
