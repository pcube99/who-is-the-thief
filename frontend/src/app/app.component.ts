import { Component, OnInit } from '@angular/core';
declare var M: any;
declare let $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  ngOnInit(): void {
  
    // Or with jQuery
    $(document).ready(function(){
      $('.sidenav').sidenav();
    });  
  }
  title = 'my-game';
}


