import { Component, OnInit } from '@angular/core';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.css']
})
export class PlayComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    // Swal.fire('Each player have a limited amount of time to select the choice.')
  }

}
