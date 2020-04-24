import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { LoaderComponent } from './loader/loader.component';
import { CreateRoomComponent } from './create-room/create-room.component';
import { JoinRoomComponent } from './join-room/join-room.component';
import { ProfileComponent } from './profile/profile.component';
import { ReactiveFormsModule ,FormsModule} from '@angular/forms';



@NgModule({
  declarations: [
    HomeComponent,
    LoaderComponent,
    CreateRoomComponent,
    JoinRoomComponent,
    ProfileComponent,
    
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    HomeComponent,
    LoaderComponent,
    CreateRoomComponent,
    JoinRoomComponent,
    ProfileComponent,
    ReactiveFormsModule
  ]
})
export class GameModule { }
