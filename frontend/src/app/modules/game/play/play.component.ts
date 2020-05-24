import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import Swal from 'sweetalert2';
import { Router, ActivatedRoute } from '@angular/router';
declare let $: any;
import * as _ from 'lodash';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.css']
})
export class PlayComponent implements OnInit {
  waiting: any;
  isReady: any;
  isReadyLoader: any;
  playerRoles: any;
  playerRole: any;
  selectedPlayer: any;
  error: any;
  rajaPlayer: any;
  loading: any;
  url: any;
  roomId: any;
  roomData: any;
  playersJoined: any;
  currentPlayerId: any;
  players: any;
  playerRoleImage: any;
  result: any;
  result1: any;
  result2: any;
  url1: any;
  roundNo: any;
  constructor(
    private router: Router,
    private http: HttpClient,
    private route: ActivatedRoute) {
    this.route.queryParams.subscribe(params => {
      this.roomId = params['roomId'];
    });
  }

  ngOnInit() {
    this.isReadyLoader = false;
    this.isReady = false;
    // Swal.fire('Each player have a limited amount of time to select the choice.')
    this.waiting = false;
    this.playerRoles = [];
    console.log('this.waiting ' + this.waiting);
    this.error = true;
    this.loading = true;
    this.currentPlayerId = localStorage.getItem('playerId');
    console.log('this.currentPlayerId ' + this.currentPlayerId);
    this.players = [];
    this.getRoomInfo();

    $(document).ready(() => {
      console.log('fixed');
      $('.fixed-action-btn').floatingActionButton();
    });
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
    });
  }

  async getRoomInfo() {
    this.loading = true;
    console.log(' this.roomId ' + this.roomId);
    this.url = environment.baseUrl + '/' + this.roomId;
    await this.http.get(this.url).toPromise()
      .then(data => {
        this.roomData = data;
        console.log('pp ' + JSON.stringify(this.roomData));
        this.loading = false;
        this.playersJoined = this.roomData.player_info.length;
        console.log('roomData ' + this.playersJoined);

        for (let i = 0; i < this.playersJoined; i++) {
          if (this.currentPlayerId === this.roomData.player_info[i].player_id) {
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
    const waitTimer = setInterval(() => {
      console.log('iswaiting' + this.roomId);
      this.url = environment.baseUrl + '/' + this.roomId;
      this.http.get(this.url).toPromise()
        .then(data => {
          this.roomData = data;
          this.playersJoined = this.roomData.player_info.length;
          console.log('roomData ' + this.playersJoined);
          if (this.playersJoined < 4) {
            this.waiting = true;
            console.log('this.waiting ' + this.waiting);
          } else {
            for (let i = 0; i < 4; i++) {
              if (this.currentPlayerId === this.roomData.player_info[i].player_id) {
                continue;
              }
              this.players.push(this.roomData.player_info[i]);
            }
            window.location.reload();
            console.log(this.players);
            console.log('waitTimer ' + waitTimer);
            clearInterval(waitTimer);
            console.log('time clear 11' + this.waiting + ' ' + this.playersJoined + ' err ' + this.error);
            this.waiting = false;
            console.log('time clear 22 ' + this.waiting + ' ' + this.playersJoined);
          }
        }).
        catch(error => {
          console.error('There was an error!', error);
        });
    }, 5000);
    console.log(' this.roomId after ' + this.roomId + ' - ' + waitTimer + ' ' + this.playersJoined);
    if (this.playersJoined < 4) {
      this.waiting = true;
      console.log('this.waiting ' + this.waiting);
    } else {
      this.waiting = false;
      console.log('this.waiting ' + this.waiting);
      clearInterval(waitTimer);
      return;
    }
  }

  exitGame() {
    this.router.navigate(['app/home']);
  }

  async choosenPlayer(index, selectedId) {
    console.log(index);
    if (this.selectedPlayer == undefined && selectedId != this.rajaPlayer) {
      console.log(index);
      this.selectedPlayer = index;
      $('#player' + index).css('border', '2px solid black');
      $('#player1').removeClass('pulse-button');
      $('#player2').removeClass('pulse-button');
      $('#player3').removeClass('pulse-button');
    }
    let url = environment.baseUrl + '/evaluate-scores?room_code=' + this.roomId + '&player_id=' + this.currentPlayerId+"&round_no="+this.roundNo+"&choice="+selectedId;
    console.log("evaluate url", url);
    await this.http.post(url, { title: 'ready player post request' }, { responseType: 'text' }).toPromise()
      .then()
      .catch((err) => {
        console.log(err);
      });
      this.roundNo = this.roundNo + 1;

  }

  async readyPlayer() {
    this.roundNo = parseInt(localStorage.getItem("roundNo"), 10);
    console.log("this.roundno " + this.roundNo);
    console.log(' this.roomId ' + this.roomId + " " + this.currentPlayerId);
    this.url = environment.baseUrl + '/update-status?room_code=' + this.roomId + '&player_id=' + this.currentPlayerId+"&round_no="+this.roundNo;
    await this.http.post(this.url, { title: 'ready player post request' }, { responseType: 'text' }).toPromise()
      .then((res) => {
        console.log("ready response" + res);
        this.result = res;

      })
      .catch(err => {
        console.log(err);
      });
    const allReadyTimer = setInterval( async () => {
      this.url = environment.baseUrl + '/all-ready?room_code=' + this.roomId+"&round_no="+this.roundNo;
      await this.http.post(this.url, { title: 'ready player post request' }, { responseType: 'text' }).toPromise()
        .then((res1) => {

          this.result1 = res1;
          console.log("interval " + this.result1 +"  -  " +  typeof this.result1);
          if (this.result1 == true || this.result1 == 'true') {
            clearInterval(allReadyTimer);

            this.url1 = environment.baseUrl + '/toss-chits?room_code=' + this.roomId+"&round_no="+this.roundNo;
            console.log("url1 " + this.url1);
            this.http.get(this.url1).toPromise()
              .then((res2) => {
                localStorage.setItem("roundNo", this.roundNo.toString());
                this.result2 = res2;
                console.log("toss chits ", this.result2);
                this.isReadyLoader = false;
                this.isReady = true;
                for (let i = 0; i < 4; i++) {
                  if (this.result2.playerRoles[i].playerId == this.currentPlayerId) {
                    let currentRole = this.result2.playerRoles[i].role;
                    if (currentRole === 'Raja') {
                      this.playerRole = 'Raja';
                      this.playerRoleImage = 'raja';
                    } else if (currentRole === 'Chor') {
                      this.playerRole = 'Chor';
                      this.playerRoleImage = 'chor';
                    } else if (currentRole === 'Sipahi') {
                      this.playerRole = 'Sipahi';
                      this.playerRoleImage = 'sipahi';
                    } else if (currentRole === 'Wazir') {
                      this.playerRole = 'Wazir';
                      this.playerRoleImage = 'vajir';
                    }
                  }
                }
                this.findRaja();
              })
              .catch(err => {
                console.log(err);
              });
          }
        })
        .catch((err) => {
          console.log(err);
        })

    }, 3000);
    $('#player1').addClass('pulse-button');
    $('#player2').addClass('pulse-button');
    $('#player3').addClass('pulse-button');
  }

  findRaja() {
    for(let player of this.result2.playerRoles) {
      console.log(player);
      if(player.role == 'Raja') {
        this.rajaPlayer = player.playerId;
        console.log("raja found " + this.rajaPlayer);
      }
    }
  }
}