import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username!: ''
  password!: ''
  constructor(private authservice:AuthService,private router:Router) { }

  ngOnInit(): void {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;

  }

  login(){
    console.log(
      this.authservice.logIn(this.username,this.password).subscribe( 
        data => {
          this.router.navigate(["/"]);
  
        },
        err => console.log("erreur de  connexion")
  
  
      )
      );
  }

}
