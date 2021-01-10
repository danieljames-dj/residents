import { Component } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'client';
  signedIn = false;
  routerUrl;

  constructor(private afAuth: AngularFireAuth, private router: Router) {
    this.routerUrl = router.url;
    router.events.subscribe((val) => {
      this.routerUrl = router.url;
    })
    afAuth.authState.subscribe((user) => {
      this.signedIn = user != null
    })
  }

  signOut() {
    this.afAuth.signOut()
  }
}
