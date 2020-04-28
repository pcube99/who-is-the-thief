import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
declare let $: any;
@Component({
  selector: 'app-join-room',
  templateUrl: './join-room.component.html',
  styleUrls: ['./join-room.component.css']
})
export class JoinRoomComponent implements OnInit {
  images: any = [];
  hideCarousel: any;
  selectedProfile: any;
  profilePicNumber: any;
  joinRoomForm: FormGroup;
  userNameErrorMessage: any;
  userName: any;
  roomId: any;
  baseUrl: any;
  submitted: any;
  constructor(private fb: FormBuilder,
    private router: Router,
    private http: HttpClient) { }

  ngOnInit(): void {
    this.submitted = false;
    this.hideCarousel = false;
    this.selectedProfile = false;
    this.profilePicNumber = 0;
    for(let i=0;i<16;i++) {
      this.images[i] = 0;
    }
    $(document).ready(function(){
      $('.carousel').carousel();
    });
    this.createForm();
  }

  selectProfilePic(i) {
    console.log("select profile " + i);
    this.hideCarousel = true;
    this.selectedProfile = true;
    this.profilePicNumber = i;
  }

  createForm() {
    this.joinRoomForm = this.fb.group({
      userName: new FormControl('', [Validators.required, Validators.minLength(3),
        Validators.maxLength(30)]),
      roomId: new FormControl('', [Validators.required, Validators.minLength(4)]),
    });
  }

  get joinFormControls() {
    return this.joinRoomForm.controls;
  }
  validateForm() {
    if(this.joinFormControls.userName.valid && this.joinFormControls.roomId.valid) {
      return true;
    } else {
      return false;
    }
  }

  joinRoom() {
    // http://54.87.54.255/rooms?room_code=635b&player_name=Jeet
    console.log('this.userName' + this.userName + " this.roomId " + this.roomId);
    this.baseUrl = "http://54.87.54.255/rooms?room_code="+ this.roomId + "&player_name=" + this.userName;
    this.http.get( this.baseUrl).subscribe({
    next: data => console.log(data),
    error: error => console.error('There was an error!', error)
  });
  }

}
