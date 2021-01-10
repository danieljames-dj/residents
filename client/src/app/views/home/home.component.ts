import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { GlobalService } from 'src/app/services/global.service';
import { HttpClientService } from 'src/app/services/http-client.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  groupColumns: string[] = ['name', 'edit', 'manage'];
  groups = [];

  constructor(private authService: AuthService, private router: Router,
    private httpClient: HttpClientService,
    private global: GlobalService) {
      let user = this.authService.getCurrentUser();
      user.getIdTokenResult(true).then((token) => {
        this.httpClient.getAuth(this.global.baseApiURL + "/v1/getGroups", {
        }, token).subscribe((res: {data: {groups}}) => {
          this.groups = res.data.groups
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

  ngOnInit(): void {
  }

  signOut() {
    this.authService.signOut();
  }

  createGroup() {
    this.router.navigate(["group-edit"])
  }

  editGroup(group) {
    this.router.navigate(["group-edit"], {queryParams: group})
  }

  manageGroup(group) {
    this.router.navigate(["group/" + group.gid], {queryParams: group})
  }

}
