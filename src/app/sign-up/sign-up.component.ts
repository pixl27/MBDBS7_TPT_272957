import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  nom!: ''
  prenom!: ''
  username!: ''
  password!: ''
  constructor(private authservice:AuthService,private router:Router) { }

  ngOnInit(): void {
  }
signup(){
  console.log(
    this.authservice.signUp(this.nom,this.prenom,this.username,this.password).subscribe( 
      data => {
        this.router.navigate(["/"]);

      },
      err => console.log(err)


    )
    );
}
}
