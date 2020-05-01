import { Component, OnInit } from '@angular/core';
declare let $: any;
@Component({
  selector: 'app-scoreboard',
  templateUrl: './scoreboard.component.html',
  styleUrls: ['./scoreboard.component.css']
})
export class ScoreboardComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    $(document).ready(() => {
      $('.modal').modal();
    });
  }

}
