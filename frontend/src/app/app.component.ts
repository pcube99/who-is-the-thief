import { Component, OnInit } from '@angular/core';
declare var M: any;
declare let $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'my-game';
  ngOnInit(): void {
  }
}


