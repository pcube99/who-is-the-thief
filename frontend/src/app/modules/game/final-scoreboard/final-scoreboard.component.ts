import { Component, OnInit, Input } from '@angular/core';
declare let $: any;
@Component({
  selector: 'app-final-scoreboard',
  templateUrl: './final-scoreboard.component.html',
  styleUrls: ['./final-scoreboard.component.css']
})
export class FinalScoreboardComponent implements OnInit {
  @Input() winnerPlayer: any;
  constructor() { }

  ngOnInit(): void {
  }

}
