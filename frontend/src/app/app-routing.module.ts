import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './modules/game/home/home.component';
import { CreateRoomComponent } from './modules/game/create-room/create-room.component';
import { JoinRoomComponent } from './modules/game/join-room/join-room.component';


const routes: Routes = [
  {
  path: 'app',

  children: [
    {
      path: 'home',
      component: HomeComponent
    },
    {
      path: 'createRoom',
      component: CreateRoomComponent
    },
    {
      path: 'joinRoom',
      component: JoinRoomComponent
    },
    {
      path: '**',
      redirectTo: 'app/home'
    }
  ]
  },
  {
    path: '**',
    redirectTo: 'app/home'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
