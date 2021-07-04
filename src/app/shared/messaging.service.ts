import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AngularFireMessaging } from '@angular/fire/messaging';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable } from 'rxjs'
import { map } from 'rxjs/operators';
@Injectable()
export class MessagingService {
currentMessage = new BehaviorSubject({});
yourtoken !:any;
uri ="https://backend-javaa-mbds272957.herokuapp.com/insererNotifWeb"
jsonany!:any;
constructor(private angularFireMessaging: AngularFireMessaging,private http:HttpClient,private toastr: ToastrService) {

}
insertnotifweb(idUser:string, token:string):Observable<any> {
    return this.http.post<any>(this.uri , { idUser, token }).pipe(map(resultat => {
  }));
  
  }

requestPermission(iduser:any) {
this.angularFireMessaging.requestToken.subscribe(
(token) => {
    this.yourtoken = token;
  
console.log(this.yourtoken);
console.log("here")
this.insertnotifweb(iduser,this.yourtoken).subscribe( 
    data => {
     console.log("the fuck " + data)

    },
    err => console.log(err)


  );
}).add(() => {


});

}
showSuccess(title:string,body:string) {
    if(title=="Felicitation"){
        this.toastr.success(body, title);

    }
    else {
        this.toastr.error(body, title);

    }
  }
receiveMessage(iduser:any) {
    var anymess:any;
    this.angularFireMessaging.messages
      .subscribe((message) => { anymess = message;
         console.log(anymess.data.idUser);
        // this.showSuccess();
         if(anymess.data.idUser==iduser){
             console.log("mitovy")
          this.showSuccess(anymess.data.title,anymess.data.body);
            this.currentMessage.next(message);
          //  this.toastr.success(anymess.data.title, anymess.data.body);
         }
      });
     
  }
  receiveback(){
    this.angularFireMessaging.setBackgroundMessageHandler(function(message) {

        console.log("back " + message);
   
       // return self.registration.showNotification(title, options);
   });
  }

}
