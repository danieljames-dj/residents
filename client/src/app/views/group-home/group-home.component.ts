import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { element } from 'protractor';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { GlobalService } from 'src/app/services/global.service';
import { HttpClientService } from 'src/app/services/http-client.service';

@Component({
  selector: 'app-group-home',
  templateUrl: './group-home.component.html',
  styleUrls: ['./group-home.component.css']
})
export class GroupHomeComponent implements OnInit {

  groupDetails
  gid
  houses = []
  houseColumns: string[] = ['details', 'delete'];

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private httpClient: HttpClientService,
    private global: GlobalService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.groupDetails = params
      this.gid = params['gid']
      this.getHouses()
    });
  }

  getHouses() {
    let user = this.authService.getCurrentUser();
    user.getIdTokenResult(true).then((token) => {
      this.httpClient.postAuth(this.global.baseApiURL + "/v1/getHouses", {
        gid: this.gid
      }, token).subscribe((res: []) => {
        this.houses = []
        res.forEach(element => {
          this.houses.push({
            details: element
          })
        })
      }, error => {
        alert("Something went wrong... (Error: " + error + ")")
      })
    })
  }

  addHouse() {
    this.router.navigate(["addHouse/" + this.gid], {queryParams: this.groupDetails})
  }

  addMember(email) {
    if (email.length == 0) {
      alert("Please enter email ID")
    } else {
      let user = this.authService.getCurrentUser();
      user.getIdTokenResult(true).then((token) => {
        this.httpClient.postAuth(this.global.baseApiURL + "/v1/addMember", {
          email: email,
          gid: this.gid
        }, token).subscribe((res) => {
          alert("Member added successfully")
        }, error => {
          alert("Something went wrong... (Error: " + error + ")")
        })
      })
    }
  }

  deleteHouse(house) {
    console.log(house)
    let user = this.authService.getCurrentUser();
    user.getIdTokenResult(true).then((token) => {
      this.httpClient.postAuth(this.global.baseApiURL + "/v1/deleteHouse", {
        gid: this.gid,
        hid: house.details.id
      }, token).subscribe((res) => {
        alert("Deleted successfully")
        this.getHouses()
      }, error => {
        alert("Something went wrong... (Error: " + error + ")")
      })
    })
  }

}
