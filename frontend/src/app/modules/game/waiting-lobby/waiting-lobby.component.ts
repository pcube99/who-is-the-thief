import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-waiting-lobby',
  templateUrl: './waiting-lobby.component.html',
  styleUrls: ['./waiting-lobby.component.css']
})
export class WaitingLobbyComponent implements OnInit {
  @Input() players: any;
  constructor() { }

  ngOnInit(): void {
    console.log(this.players);
  }

}
