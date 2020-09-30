import { Component } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'client';
  signedIn = false;

  constructor(private afAuth: AngularFireAuth) {
    afAuth.authState.subscribe((user) => {
      this.signedIn = user != null
    })
  }

  signOut() {
    this.afAuth.signOut()
  }
}
