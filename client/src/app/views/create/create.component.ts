import { Component, OnInit } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { GlobalService } from 'src/app/services/global.service';
import { HttpClientService } from 'src/app/services/http-client.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router, private afAuth: AngularFireAuth, private httpClient: HttpClientService,
    private global: GlobalService) { }

  ngOnInit(): void {
  }

  createNewGroup(groupName) {
    let user = this.authService.getCurrentUser();
    user.getIdTokenResult(true).then((token) => {
      this.httpClient.postAuth(this.global.baseApiURL + "/v1/createGroup", {
        gname: groupName
      }, token).subscribe((res) => {
        this.router.navigate(["home"])
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

  signOut() {
    this.authService.signOut();
  }

}
