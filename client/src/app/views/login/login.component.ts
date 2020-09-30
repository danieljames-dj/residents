import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { GlobalService } from 'src/app/services/global.service';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private authService: AuthService, private globalService: GlobalService, private router: Router, private title: Title) { }

  ngOnInit() {
    document.getElementById("logout").style.visibility = "hidden"
    let title = "Directory"
    this.title.setTitle(title)
    this.globalService.title = title
  }

  signIn(email, password) {
    this.authService.signInWithEmail(email, password)
  }

  signUp(email, password) {
    this.authService.createUser(email, password)
  }

  signInWithGoogle() {
    this.authService.signInWithGoogle()
  }

  getTitle() {
    return this.globalService.title
  }

}
