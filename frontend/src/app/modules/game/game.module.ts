import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { LoaderComponent } from './loader/loader.component';
import { CreateRoomComponent } from './create-room/create-room.component';
import { JoinRoomComponent } from './join-room/join-room.component';
import { ProfileComponent } from './profile/profile.component';
import { ReactiveFormsModule ,FormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { PlayComponent } from './play/play.component';
import { ScoreboardComponent } from './scoreboard/scoreboard.component';
import { OtherPlayersComponent } from './other-players/other-players.component';
import { WaitingLobbyComponent } from './waiting-lobby/waiting-lobby.component';



@NgModule({
  declarations: [
    HomeComponent,
    LoaderComponent,
    CreateRoomComponent,
    JoinRoomComponent,
    ProfileComponent,
    PlayComponent,
    ScoreboardComponent,
    OtherPlayersComponent,
    WaitingLobbyComponent,
    
    
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  exports: [
    HomeComponent,
    LoaderComponent,
    CreateRoomComponent,
    JoinRoomComponent,
    ProfileComponent,
    ReactiveFormsModule,
    PlayComponent,
    ScoreboardComponent,
    OtherPlayersComponent,
    WaitingLobbyComponent,
  ]
})
export class GameModule { }
