import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { LoaderComponent } from './loader/loader.component';
import { CreateRoomComponent } from './create-room/create-room.component';
import { JoinRoomComponent } from './join-room/join-room.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { PlayComponent } from './play/play.component';
import { ScoreboardComponent } from './scoreboard/scoreboard.component';
import { WaitingLobbyComponent } from './waiting-lobby/waiting-lobby.component';
import { ErrorPageComponent } from './error-page/error-page.component';
import { FinalScoreboardComponent } from './final-scoreboard/final-scoreboard.component';



@NgModule({
  declarations: [
    HomeComponent,
    LoaderComponent,
    CreateRoomComponent,
    JoinRoomComponent,
    PlayComponent,
    ScoreboardComponent,
    WaitingLobbyComponent,
    ErrorPageComponent,
    FinalScoreboardComponent,


  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  exports: [
    HomeComponent,
    LoaderComponent,
    CreateRoomComponent,
    JoinRoomComponent,
    ReactiveFormsModule,
    PlayComponent,
    ScoreboardComponent,
    WaitingLobbyComponent,
  ]
})
export class GameModule { }
