import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AngularFireMessaging } from '@angular/fire/messaging';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable } from 'rxjs'
import { map } from 'rxjs/operators';
import { NotificationUser } from './NotificationUser.model';
@Injectable()
export class MessagingService {
currentMessage = new BehaviorSubject({});
yourtoken !:any;
uri ="https://backend-javaa-mbds272957.herokuapp.com/"
jsonany!:any;
notifnumber = 0
constructor(private angularFireMessaging: AngularFireMessaging,private http:HttpClient,private toastr: ToastrService) {

}
setnotifnumber(nombre:number){
  this.notifnumber = nombre;
}
getNotif(iduser:string):Observable<NotificationUser[]> {
  console.log("Dans le service de gestion des match...")
  //return of(this.matieres);
  return this.http.get<NotificationUser[]>(this.uri+ "getNotif?idUser=" + iduser );
}
setnotifvue(id:number):Observable<any> {
  return this.http.post<any>(this.uri +"setStatueVueNotif" , id).pipe(map(resultat => {
}));

}
insertnotifweb(idUser:string, token:string):Observable<any> {
    return this.http.post<any>(this.uri + "insererNotifWeb" , { idUser, token }).pipe(map(resultat => {
  }));
  
  }

requestPermission(iduser:any) {
this.angularFireMessaging.requestToken.subscribe(
(token) => {
    this.yourtoken = token;
 
this.insertnotifweb(iduser,this.yourtoken).subscribe( 
    data => {

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
         if(anymess.data.idUser==iduser){
          this.showSuccess(anymess.data.title,anymess.data.body);
            this.currentMessage.next(message);
         }
      });
     
  }
  receiveback(){
    this.angularFireMessaging.setBackgroundMessageHandler(function(message) {

        console.log("back " + message);
   
   });
  }

}
