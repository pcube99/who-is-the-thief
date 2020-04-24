import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
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
  constructor(private fb: FormBuilder,
    private router: Router) { }

  ngOnInit(): void {
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
      userName: new FormControl('', [Validators.required, Validators.minLength(3)]),
      roomId: new FormControl('', [Validators.required, Validators.minLength(5)]),
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

}
