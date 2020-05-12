import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './modules/game/home/home.component';
import { CreateRoomComponent } from './modules/game/create-room/create-room.component';
import { JoinRoomComponent } from './modules/game/join-room/join-room.component';
import { PlayComponent } from './modules/game/play/play.component';
import { ErrorPageComponent } from './modules/game/error-page/error-page.component';


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
      path: 'play',
      component: PlayComponent
    },
    {
      path: 'error',
      component: ErrorPageComponent
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
