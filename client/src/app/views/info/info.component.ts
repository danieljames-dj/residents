import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InfoComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  goToDashboard() {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(["home"])
    } else {
      this.router.navigate(["signin"])
    }
  }

}
