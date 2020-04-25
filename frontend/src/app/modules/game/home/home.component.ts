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
    // const header_obj = {
    //   'Content-Type': 'application/json',
    // };
    // this.baseUrl = "http://54.87.54.255/rooms?room_name=Raja_ka_Kamra&rounds=4 ";
    // const promise = this.http.post(this.baseUrl , { headers: header_obj
    //   , observe: 'response' }).toPromise();
    // promise.then((res) => {
    //   console.log("res " + JSON.stringify(res));
    // }).catch((err) => {
    //   console.log(err);
    // })
    this.router.navigate(['app/createRoom']);
  }

  joinRoom() {
    this.router.navigate(['app/joinRoom']);
  }
}
