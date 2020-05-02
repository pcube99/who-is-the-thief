import { Component, OnInit } from '@angular/core';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
declare let $: any;
import * as _ from 'lodash';
@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.css']
})
export class PlayComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
    // Swal.fire('Each player have a limited amount of time to select the choice.')
    $(document).ready(() => {
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
    })
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
    $('#player1').addClass("pulse");
    $('#player2').addClass("pulse");
    $('#player3').addClass("pulse");
    $('#bluff').addClass('disabled');
    $('#reveal').addClass('disabled');
  }

  choosenPlayer(index) {
    console.log(index);
    $('#player1').removeClass("pulse");
    $('#player2').removeClass("pulse");
    $('#player3').removeClass("pulse");
  }

}
