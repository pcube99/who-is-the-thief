import { Component, OnInit } from '@angular/core';
import Swal from 'sweetalert2';
import { Router, ActivatedRoute } from '@angular/router';
declare let $: any;
import * as _ from 'lodash';
import { HttpClient } from '@angular/common/http';
@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.css']
})
export class PlayComponent implements OnInit {
  waiting: any;
  error: any;
  loading: any;
  baseUrl: any;
  roomId: any;
  roomData: any;
  playersJoined: any;
  currentPlayerId: any;
  players: any;
  waitTimer: any;
  constructor(private router: Router,
    private http: HttpClient,
    private route: ActivatedRoute) { 
      this.route.queryParams.subscribe(params => {
        this.roomId = params['roomId'];
      });
  }

  ngOnInit(): void {
    // Swal.fire('Each player have a limited amount of time to select the choice.')
    this.waiting = false;
    this.error = true;
    this.loading = true;
    this.currentPlayerId = localStorage.getItem('playerId');
    console.log("this.currentPlayerId " + this.currentPlayerId);
    this.players = [];
    this.getRoomInfo();

    // $(document).ready(() => {
    //   $('.fixed-action-btn').floatingActionButton();
    // });   
  }
 
  exitGamePopup() {
    Swal.fire({
      title: 'Are you sure?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Leave the game'
    }).then((result) => {
      if (result.value) {
        this.exitGame();
      }
    })
  }

  async getRoomInfo() {
    this.loading = true;
    // this.roomId = '6458';
    console.log(" this.roomId " + this.roomId);
    this.baseUrl = "http://54.87.54.255/rooms/" + this.roomId;
    await this.http.get(this.baseUrl).toPromise()
      .then(data => {
        this.roomData = data;
        console.log('pp ' + JSON.stringify(this.roomData));
        this.loading = false;
        this.playersJoined = this.roomData.player_info.length;
        console.log('roomData ' + this.playersJoined);
    
        for(let i=0; i<this.playersJoined;i++) {
          if(this.currentPlayerId == this.roomData.player_info[i].player_id) {
            continue;
          }
          this.players.push(this.roomData.player_info[i]);
        }
        console.log(this.players);
      })
      .catch(error => {
        console.error('There was an error!', error);
        this.loading = false;
      });
      this.waitTimer = setInterval( this.isWaiting, 5000);
      console.log(" this.roomId after " + this.roomId + this.waitTimer);
      if (this.playersJoined < 2) {
        this.waiting = true;
      } else {
        this.waiting = false;
        clearInterval(this.waitTimer);
        return;
      }
  }

  test() {
    console.log('test');
  }

  isWaiting(roomId) {
    console.log("iswaiting" + roomId);
    this.baseUrl = "http://54.87.54.255/rooms/1a28";
    this.http.get(this.baseUrl).subscribe({
      next(data){
        this.roomData = data;
        this.playersJoined = this.roomData.player_info.length;
        console.log('roomData ' + this.playersJoined);
        if (this.playersJoined < 2) {
          this.waiting = true;
        } else {
          this.waiting = false;
          clearInterval(this.waitTimer);
          console.log("time clear " + this.waiting);
          return;
        }
      },
      error(error) {
        console.error('There was an error!', error);
      }
    });
    
  }
  playerStatus() {
    $('#player1').css('border', '2px solid red');
  }
  exitGame() {
    this.router.navigate(['app/home']);
  }

  //TODO assign it to a variable
  tossCheats() {
    // this.playerStatus();
    const roles = ["Raja", "Chor", "Sipahi", "Mantri"];
    console.log(_.sampleSize( roles, 4));
    $('#player1').addClass("pulse-button");
    $('#player2').addClass("pulse-button");
    $('#player3').addClass("pulse-button");
    $('#bluff').addClass('disabled');
    $('#reveal').addClass('disabled');
  }

  choosenPlayer(index) {
    console.log(index);
    $('#player1').removeClass("pulse-button");
    $('#player2').removeClass("pulse-button");
    $('#player3').removeClass("pulse-button");
  }

}
