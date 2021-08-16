import { Component, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { ActivatedRoute, ResolveEnd, Router } from '@angular/router';
import { AuthService } from './shared/auth.service';
import { MessagingService } from './shared/messaging.service';
import { ToastrService } from 'ngx-toastr';
import { NotificationUser } from './shared/NotificationUser.model';

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
  actualroute!:string;
notificationtrue!:boolean;
listnotif!:NotificationUser[];
notifnumber!:number;
@ViewChild('togglenotif') togglenotif!: ElementRef;
@ViewChild('menu') menu!: ElementRef;

  constructor(private authservice:AuthService,public router:Router,public messagingService: MessagingService,private route:ActivatedRoute,private toastr: ToastrService,private renderer: Renderer2) { }

  ngOnInit(): void {
    this.notificationtrue = false;

    this.renderer.listen('window', 'click',(e:Event)=>{
    
       if( !this.togglenotif.nativeElement.contains(e.target)){
         this.notificationtrue = false;
    
       }
      
 });


  
  this.router.events.subscribe((routerData) => {
    if(routerData instanceof ResolveEnd){ 
      this.actualroute = routerData.url;
      if(this.actualroute == "/emailadmin"){
       
      }
  } 
})

  this.message = this.messagingService.currentMessage
    let tokenuservar =  localStorage.getItem("usertoken");
    if(tokenuservar != null){
     

    this.tokenuser = tokenuservar
   this.getcurrentuser();
   setTimeout(() => {
    
     this.messagingService.requestPermission(this.me._id)

    this.messagingService.receiveMessage(this.me._id);
}, 5000);

   

   }
  }
  getListNotif(){

    console.log(

      this.messagingService.getNotif(this.me._id).subscribe( 
        result => {
    
          this.listnotif = result;
       
        },
        err => console.log("error")
  
  
      ).add(() => {})
      );
  }
  consolelog(){
    console.log( "this is log component " + this.notifnumber);
  }
   getNombreNotifNonLu(){
    this.notifnumber=0;
    console.log(

      this.messagingService.getNotif(this.me._id).subscribe( 
        result => {
         
          for(let a of result){
            if(a.vue==0){
          
              this.notifnumber = this.notifnumber + 1;
            }
          }
          this.messagingService.setnotifnumber(this.notifnumber);
        },
        err => console.log("error")
  
  
      ).add(() => {})
      );
  }
shownotification(){
  this.notificationtrue = !this.notificationtrue;
  if(this.notificationtrue){
    this.getListNotif();
  }

}
  logOut(){
    localStorage.removeItem("usertoken");
    this.router.navigate(["/"]).then(() => {
      window.location.reload();
    });

  }
  getcurrentuser(){
  
    console.log(
      this.authservice.getCurrentUser(this.tokenuser).subscribe( 
        data => {
          this.me = data;
        },
        err => console.log("error")
  
  
      ).add(() => {this.getNombreNotifNonLu()})
      );
  }
}
