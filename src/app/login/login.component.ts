import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../shared/auth.service';
import { NgxSpinnerService } from "ngx-spinner";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username!: ''
  password!: ''
  constructor(private authservice:AuthService,private router:Router,private toastr: ToastrService,private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;

  }
  showtoast(title:string,body:string,type:string) {
    if(type=="success"){
        this.toastr.success(body, title);

    }
    else {
        this.toastr.error(body, title);

    }
  }
  login(){
    this.spinner.show('sp6');
    console.log(
      this.authservice.logIn(this.username,this.password).subscribe( 
        data => {
          this.router.navigate(["/"]).then(() => {
            this.spinner.hide('sp6');
            window.location.reload();
          });
  
        },
        err => { this.spinner.hide('sp6');this.showtoast("Erreur de Connexion","veuiller verifiez votre pseudo ou mot de passe","error")}
  
  
      )
      );
  }

}
