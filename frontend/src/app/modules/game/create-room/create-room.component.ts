import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormGroup, Validators, FormControl, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from '../../../../environments/environment';
declare let $: any;
@Component({
  selector: 'app-create-room',
  templateUrl: './create-room.component.html',
  styleUrls: ['./create-room.component.css']
})
export class CreateRoomComponent implements OnInit {
  url: any;
  createRoomForm: FormGroup;
  result: any;
  selectedProfile: any;
  shareLink: any;
  roomName: any;
  numberOfRounds: any;
  shareLink1: any;
  submitted: any;
  loading: any;
  constructor(
    private http: HttpClient,
    private fb: FormBuilder,
    private router: Router) {
    window.addEventListener('load', this.detectOffline);
    window.addEventListener('online', this.detectOffline);
    window.addEventListener('offline', this.detectOffline);
  }

  ngOnInit(): void {
    this.loading = false;
    this.submitted = false;
    this.shareLink = 'You have been invited to play *Who is the thief?* \n \n' +
      'Click below link to join the room and start playing. \n \n' +
      'https://whoisthetheif.web.app/app/play?roomId=f153';
    this.shareLink = encodeURI(this.shareLink);
    console.log(this.shareLink);
    this.selectedProfile = true;
    this.createForm();
    $(document).ready(() => {
      $('select').formSelect();
    });
  }

  createForm() {
    this.createRoomForm = this.fb.group({
      roomName: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]),
      numberOfRounds: new FormControl('', [Validators.required]),
    });
  }

  get createRoomFormControls() {
    return this.createRoomForm.controls;
  }
  validateForm() {
    if (this.createRoomFormControls.roomName.valid && this.createRoomFormControls.numberOfRounds.valid) {
      this.createRoom();
      return true;
    } else {
      return false;
    }
  }

  createRoom() {
    this.loading = true;
    console.log('this.roomName' + this.createRoomFormControls.roomName.value +
      ' this.numberOfRounds ' + this.createRoomFormControls.numberOfRounds.value);
    this.url = environment.baseUrl + '?room_name=' + this.createRoomFormControls.roomName.value +
      '&rounds=' + this.createRoomFormControls.numberOfRounds.value;
    this.http.post(this.url, { title: 'Create room post request' }).toPromise()
      .then((data) => {
        console.log(data);
        this.result = data;
        this.loading = false;
        localStorage.setItem('roomId', this.result.room_code);
        this.router.navigate(['app/joinRoom']);
      })
      .catch((err) => {
        console.error('There was an error!', err);
        this.loading = false;
      });
  }

  detectOffline() {
    if (navigator.onLine) {
      console.log('online');
      document.body.style.backgroundColor = '#ebe8e4';
    } else {
      console.log('offline');
      document.body.style.backgroundColor = 'red';
    }
  }

}
