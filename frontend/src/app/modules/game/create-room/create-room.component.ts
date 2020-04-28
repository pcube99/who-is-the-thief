import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormGroup, Validators, FormControl, FormBuilder } from '@angular/forms';
declare let $: any;
@Component({
  selector: 'app-create-room',
  templateUrl: './create-room.component.html',
  styleUrls: ['./create-room.component.css']
})
export class CreateRoomComponent implements OnInit {
  baseUrl: any;
  createRoomForm: FormGroup;
  result: any;
  selectedProfile: any;
  shareLink: any;
  roomName: any;
  numberOfRounds: any;
  shareLink1: any;
  submitted: any;
  constructor(private http: HttpClient,
    private fb: FormBuilder) { }

  ngOnInit(): void {
    this.submitted = false;
    this.shareLink = "You have been invited to play *Who is the theif?* \n \n" +
      "Click below link to join the room and start playing. \n \n" +
      "https://whoisthetheif.web.app/app/play?roomId=f153";
      this.shareLink = encodeURI(this.shareLink);
      console.log(this.shareLink);
    this.selectedProfile = true;
    this.createForm();
    $(document).ready(function(){
      $('select').formSelect();
    })
  }

  createForm() {
    this.createRoomForm = this.fb.group({
      roomName: new FormControl('', [Validators.required, Validators.minLength(3)]),
      numberOfRounds: new FormControl('', [Validators.required]),
    });
  }

  get createRoomFormControls() {
    return this.createRoomForm.controls;
  }
  validateForm() {
    if(this.createRoomFormControls.roomName.valid && this.createRoomFormControls.numberOfRounds.valid) {
      return true;
    } else {
      return false;
    }
  }

  createRoom() {
    console.log('this.roomName' + this.roomName + " this.numberOfRounds " + this.numberOfRounds);
    this.baseUrl = "http://54.87.54.255/rooms?room_name="+ this.roomName + "&rounds=" + this.numberOfRounds;
    this.http.post( this.baseUrl, { title: 'Angular POST Request Example' }).subscribe({
    next: data => console.log(data),
    error: error => console.error('There was an error!', error)
})
    
  }

  shareRoom() {
    
  }
}
