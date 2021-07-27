import { NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DetailMatchComponent } from './detail-match/detail-match.component';
import { ListMatchComponent } from './list-match/list-match.component';
import { MyProfilComponent } from './my-profil/my-profil.component';
import { HistoriqueComponent } from './historique/historique.component';
import { RouterModule, Routes } from '@angular/router';
import { AcceuilComponent } from './acceuil/acceuil.component';
import { ListTeamsComponent } from './list-teams/list-teams.component';
import { DetailTeamComponent } from './detail-team/detail-team.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { LoginComponent } from './login/login.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxQRCodeModule } from 'ngx-qrcode2';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule } from "ngx-spinner";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSelectModule} from '@angular/material/select';
import {MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatBadgeModule } from "@angular/material/badge";
import { NgxSimpleCountdownModule } from 'ngx-simple-countdown';

import { JwPaginationModule  } from 'jw-angular-pagination';

import { AngularFireMessagingModule } from '@angular/fire/messaging';
import { AngularFireDatabaseModule } from '@angular/fire/database';
import { AngularFireAuthModule } from '@angular/fire/auth';
import { AngularFireModule } from '@angular/fire';
import { MessagingService } from './shared/messaging.service';
import { environment } from '../environments/environment';
import { AsyncPipe } from '../../node_modules/@angular/common';
import { ToastrModule } from 'ngx-toastr';
import { BackofficeListEmailComponent } from './backoffice-list-email/backoffice-list-email.component';
import { BackofficeListProblemComponent } from './backoffice-list-problem/backoffice-list-problem.component';
import { SortDirective } from './directive/sort.directive';


const routes:Routes = [
 

  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:'',
    component:AcceuilComponent
  },
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:'login',
    component:LoginComponent
  },
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:'sign-up',
    component:SignUpComponent
  },
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:'detail-team/:id',
    component:DetailTeamComponent
  },
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:'detail-match/:id',
    component:DetailMatchComponent
  },
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:'list-teams',
    component:ListTeamsComponent
  },
  
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:"bet",
    component:ListMatchComponent
  },
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:"acceuil",
    component:AcceuilComponent
  },
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:"mon-profil",
    component:MyProfilComponent
  },
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:"historique",
    component:HistoriqueComponent
  },
  // Tout les routes de Back Office
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:"emailadmin",
    component:BackofficeListEmailComponent
  },
  {
    // indique que http://localhost:4200 sans rien ou avec un "/" à la fin
    // doit afficher le composant AssignmentsComponent (celui qui affiche la liste)
    path:"problemeadmin",
    component:BackofficeListProblemComponent
  }
 
]


@NgModule({
  declarations: [
    AppComponent,
    DetailMatchComponent,
    ListMatchComponent,
    MyProfilComponent,
    HistoriqueComponent,
    AcceuilComponent,
    ListTeamsComponent,
    DetailTeamComponent,
    SignUpComponent,
    LoginComponent,
    BackofficeListEmailComponent,
    BackofficeListProblemComponent,
    SortDirective,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgxQRCodeModule,
    NgxSpinnerModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule ,
    MatFormFieldModule,
    MatIconModule,
    ReactiveFormsModule,
    MatBadgeModule,
    JwPaginationModule ,
    AngularFireDatabaseModule,
    NgxSimpleCountdownModule,
      AngularFireAuthModule,
      AngularFireMessagingModule,
      ToastrModule.forRoot(),
      AngularFireModule.initializeApp(environment.firebase),
    RouterModule.forChild(routes),
    NgbModule,
    BrowserAnimationsModule
    ],
  exports: [RouterModule,MatSelectModule,MatDatepickerModule,MatFormFieldModule],
  providers: [MatNativeDateModule],
  bootstrap: [AppComponent]
})
export class AppModule { }
