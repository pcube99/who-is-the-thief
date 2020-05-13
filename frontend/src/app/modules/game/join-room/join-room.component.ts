import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

declare let $: any;
@Component({
  selector: 'app-join-room',
  templateUrl: './join-room.component.html',
  styleUrls: ['./join-room.component.css']
})
export class JoinRoomComponent implements OnInit {
  images: any = [];
  loading: any;
  hideCarousel: any;
  selectedProfile: any;
  profilePicNumber: any;
  joinRoomForm: FormGroup;
  userNameErrorMessage: any;
  userName: any;
  roomId: any;
  url: any;
  submitted: any;
  roomIdInvalidError: any;
  constructor(private fb: FormBuilder,
    private router: Router,
    private http: HttpClient) { }

  ngOnInit(): void {
    this.loading = false;
    this.submitted = false;
    this.hideCarousel = false;
    this.selectedProfile = false;
    this.profilePicNumber = 0;
    this.roomIdInvalidError = false;
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
        Validators.maxLength(12)]),
      roomId: new FormControl('', [Validators.required, Validators.minLength(4)]),
    });
  }

  get joinFormControls() {
    return this.joinRoomForm.controls;
  }
  validateForm() {
    if(this.joinFormControls.userName.valid && this.joinFormControls.roomId.valid) {
      this.joinRoom();
      return true;
    } else {
      return false;
    }
  }

  joinRoom() {
    this.loading = true;
    console.log('this.userName' + this.userName + " this.roomId " + this.roomId);
    this.url = environment.baseUrl + "?room_code="+ this.roomId + "&player_name=" + this.userName;
    this.http.get( this.url, {responseType: 'text'}).subscribe({
    next: data => {
      console.log(data);
      this.loading = false;
      localStorage.setItem('playerId' , data);
      this.router.navigate(['app/play'], { queryParams: { roomId: this.roomId } });
    },
    error: error =>  {
      console.error('There was an error!', error);
      this.roomIdInvalidError = true;
      setTimeout(() => {
        this.roomIdInvalidError = false;
      }, 9000)
      this.loading = false;
    }
  });
  }

  ngAfterViewInit() {
    if(localStorage.getItem('roomId') != null) {
      $('#icon_play').click();
      this.roomId = localStorage.getItem('roomId');
      console.log("room id " + this.roomId);
      localStorage.removeItem('roomId');
    }
  }
}
