import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { GlobalService } from 'src/app/services/global.service';
import { HttpClientService } from 'src/app/services/http-client.service';

@Component({
  selector: 'app-add-house',
  templateUrl: './add-house.component.html',
  styleUrls: ['./add-house.component.css']
})
export class AddHouseComponent implements OnInit {

  gid

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private httpClient: HttpClientService,
    private global: GlobalService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.gid = params['id']
    });
  }

  addHouse(headName, contact, detailA, detailB, subGroup) {
    let user = this.authService.getCurrentUser();
    user.getIdTokenResult(true).then((token) => {
      this.httpClient.postAuth(this.global.baseApiURL + "/v1/addHouse", {
        houseDetails: JSON.stringify({
          headName: headName,
          contact: contact,
          detailA: detailA,
          detailB: detailB,
          subGroup: subGroup
        }),
        gid: this.gid
      }, token).subscribe((res) => {
        alert("House added successfully")
        this.router.navigate(["home"])
      }, error => {
        alert("Something went wrong... (Error: " + error + ")")
      })
    })
  }

}
