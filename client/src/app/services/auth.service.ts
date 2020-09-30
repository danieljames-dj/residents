import { Injectable, NgZone } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { GlobalService } from './global.service';
import * as firebase from 'firebase/app';
import { HttpClientService } from './http-client.service';

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private authChangeObserver
  private email
  private uid
  private userDetails
  private isUserSignedIn: boolean
  private authObservable
  private authStateChangeBlock

  constructor(private router: Router, private afAuth: AngularFireAuth, private global: GlobalService, private ngZone: NgZone, private httpClient: HttpClientService) {
    afAuth.authState.subscribe((user) => {
      if (this.userDetails == null && user != null || this.userDetails != null && user == null) {
        this.userDetails = user
        if (user != null) {
          this.getToken(true).then((token) => {
            this.httpClient.postAuth(this.global.baseApiURL + "/v1/login", {
              uid: user.uid,
              email: user.email,
              name: user.displayName
            }, token).subscribe((res) => {
              // console.log(res)
              // this.userType = res.userType
              // if (this.userType == "3") {
              //     // this.requestListColumns[5] = 'addEmployee'
              //     this.updateEmployees(token)
              //     this.updateHouses(token)
              // }
              // if (this.userType == "2") {
              // }
          })
          });
        }
        if (this.authStateChangeBlock != null) {
          this.authStateChangeBlock()
          this.authStateChangeBlock = null
        }
      }
      this.userDetails = user
    })
  }

  getCurrentUser() {
    return this.userDetails;
  }

  isLoggedIn() {
    return this.userDetails != null
  }

  executeWhenStateChanges(authStateChangeBlock) {
    this.authStateChangeBlock = authStateChangeBlock
  }

  authGuardCheck(shouldBeSignedIn: boolean) {
      let thisRef = this
      console.log("HI")
      return new Observable<boolean>(observer => {
          // thisRef.afAuth.authState.pipe(take(1)).subscribe((data) => {
          //     let isSignedIn = data != null
          //     if (isSignedIn == shouldBeSignedIn) {
          //         observer.next(true)
          //         this.checkForAuthChange()
          //     } else {
          //         observer.next(false)
          //         thisRef.ngZone.run(() => thisRef.router.navigate([isSignedIn ? thisRef.global.authHomePage : thisRef.global.unauthHomePage]))
          //     }
          // })
      })
  }

  checkForAuthChange() {
      let thisRef = this
      this.authObservable = this.afAuth.authState.subscribe((data) => {
          if (data != null) {
              this.email = data.email
              this.uid = data.uid
          }
          thisRef.ngZone.run(() => thisRef.router.navigate([(data != null) ? thisRef.global.authHomePage : thisRef.global.unauthHomePage]))
      })
  }

  isUserLoggedIn() {
    return this.afAuth.currentUser != null
  }

  getEmail() {
    return this.email
  }

  getUid() {
    return this.uid
  }

  createUser(email, password) {
    let thisCached = this
    this.afAuth.createUserWithEmailAndPassword(email, password).catch(function(error) {
      thisCached.manageAuthenticationAPIerrors(error.code)
    })
  }

  signInWithEmail(email, password) {
    let thisCached = this
    console.log(this.afAuth)
    this.afAuth.signInWithEmailAndPassword(email, password).catch(function(error) {
      alert(error)
      thisCached.manageAuthenticationAPIerrors(error.code)
    })
  }

  signInWithGoogle() {
    let thisCached = this
    this.afAuth.signInWithPopup(new firebase.auth.GoogleAuthProvider()).catch(function(error) {
      thisCached.manageAuthenticationAPIerrors(error.code)
      console.log(thisCached.isLoggedIn())
    })
  }

  getToken(forced) {
    return this.userDetails.getIdTokenResult(forced)
  }

  signOut() {
    this.afAuth.signOut()
  }

  manageAuthenticationAPIerrors(code) {
    switch (code) {
      case 'auth/user-not-found':
        alert("Looks like you haven't signed up. Please sign up.")
        break
      case 'auth/weak-password':
        alert("The password is weak. Please provide a strong password.")
        break
      case 'auth/email-already-in-use':
        alert("Email already in use.")
        break
      default:
      this.unhandledByAdmin(code)
    }
    /*
      Get all the error cases here: https://firebase.google.com/docs/auth/admin/errors
    */
  }

  unhandledByAdmin(code) {
    alert('Some internal server issues, please try again...')
    console.log(code)
  }
}
