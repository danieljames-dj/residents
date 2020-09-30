import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  
  constructor(private authService: AuthService, private router: Router) { }

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const thisRef = this
    if (this.authService.isLoggedIn() == route.data.shouldBeSignedIn) {
      this.authService.executeWhenStateChanges(() => {
        thisRef.router.navigate([route.data.redirect])
      })
      return true
    } else {
      this.router.navigate([route.data.redirect])
      return false
    }
  }
  
}
