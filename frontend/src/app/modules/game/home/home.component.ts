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
  constructor( private router: Router,
    private http: HttpClient) { }

  ngOnInit(): void {
  }

  createRoom() {
    this.router.navigate(['app/createRoom']);
  }

  joinRoom() {
    this.router.navigate(['app/joinRoom']);
  }
}
