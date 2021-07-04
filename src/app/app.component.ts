import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './shared/auth.service';
import { MessagingService } from './shared/messaging.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [MessagingService]

})
export class AppComponent {
  title = 'frontend-tpt-mbds';
  tokenuser!:string
  me!:any;
  message!:any;



  constructor(private authservice:AuthService,private router:Router,private messagingService: MessagingService,private toastr: ToastrService) { }

  ngOnInit(): void {
  
  //this.messagingService.receiveback();
  this.message = this.messagingService.currentMessage
    let tokenuservar =  localStorage.getItem("usertoken");
    if(tokenuservar != null){
     

    this.tokenuser = tokenuservar
   this.getcurrentuser();
   setTimeout(() => {
     console.log("myid" + this.me._id);
     this.messagingService.requestPermission(this.me._id)

    this.messagingService.receiveMessage(this.me._id);
}, 5000);

   

   }
  }

  logOut(){
    localStorage.removeItem("usertoken");
    window.location.reload();


  }
  getcurrentuser(){
  
    console.log(
      this.authservice.getCurrentUser(this.tokenuser).subscribe( 
        data => {
    console.log(data.solde + "EZ");
          this.me = data;
        },
        err => console.log("tsy nande pory")
  
  
      )
      );
  }
}
