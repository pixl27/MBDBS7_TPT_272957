import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';
import { ToastrService } from 'ngx-toastr';
import { NgxSpinnerService } from "ngx-spinner";

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
  constructor(private authservice:AuthService,private router:Router,private toastr: ToastrService,private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
  }
  showtoast(title:string,body:string,type:string) {
    if(type=="success"){
        this.toastr.success(body, title);

    }
    else {
        this.toastr.error(body, title);

    }
  }
signup(){
  this.spinner.show('sp6');
  console.log(
    this.authservice.signUp(this.nom,this.prenom,this.username,this.password).subscribe( 
      data => {
        this.router.navigate(["/"]).then(() => {
          this.spinner.hide('sp6');
          this.showtoast("Inscription reussie","félicitation votre inscription à réussie","success");
          window.location.reload();
        });

      },
      err =>{this.spinner.hide('sp6');this.showtoast("Inscription echouée","pseudo déja pris","error")}


    )
    );
}
}
