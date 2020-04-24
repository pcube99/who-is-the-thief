import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { LoaderComponent } from './loader/loader.component';
import { CreateRoomComponent } from './create-room/create-room.component';
import { JoinRoomComponent } from './join-room/join-room.component';



@NgModule({
  declarations: [
    HomeComponent,
    LoaderComponent,
    CreateRoomComponent,
    JoinRoomComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    HomeComponent,
    LoaderComponent
  ]
})
export class GameModule { }
