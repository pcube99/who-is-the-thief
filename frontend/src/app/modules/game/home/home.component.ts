import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  baseUrl: any;
  constructor( private router: Router) { }

  ngOnInit(): void {
    const divInstall = document.getElementById('installContainer');
const btnAdd = document.getElementById('butInstall');

let deferredPrompt;

window.addEventListener('beforeinstallprompt', (e) => {
  // Prevent Chrome 67 and earlier from automatically showing the prompt
  e.preventDefault();
  // Stash the event so it can be triggered later.
  deferredPrompt = e;
});

// Installation must be done by a user gesture! Here, the button click
btnAdd.addEventListener('click', (e) => {
  // hide our user interface that shows our A2HS button
  // btnAdd.style.display = 'none';
  // Show the prompt
  deferredPrompt.prompt();
  // Wait for the user to respond to the prompt
  deferredPrompt.userChoice
    .then((choiceResult) => {
      if (choiceResult.outcome === 'accepted') {
        console.log('User accepted the A2HS prompt');
      } else {
        console.log('User dismissed the A2HS prompt');
      }
      deferredPrompt = null;
    });
});
  }

  createRoom() {
    this.router.navigate(['app/createRoom']);
  }

  joinRoom() {
    this.router.navigate(['app/joinRoom']);
  }

  
}
