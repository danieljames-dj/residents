import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  inputDetails
  groupDetails
  personDetails
  subGroups
  gid
  newHouse = {subGroup: '', persons: []}
  image
  houseId

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private httpClient: HttpClient,
    private global: GlobalService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.gid = params['gid']
      this.groupDetails = params
      this.inputDetails = params['inputDetails']
      this.personDetails = params['personDetails']
      this.subGroups = params['subGroups']
      let house = JSON.parse(params['selectedHouse'])
      if (house) {
        this.newHouse = house.details
        this.houseId = house.houseId
      }
    });
  }

  async addHouse() {
    let user = this.authService.getCurrentUser();
    console.log(this.image)
    const formData = new FormData()
    formData.append("file", this.image)
    var reader = new FileReader();
    let result = await new Promise((resolve) => {
      let fileReader = new FileReader();
      fileReader.onload = (e) => resolve(fileReader.result);
      if (this.image) {
        fileReader.readAsDataURL(this.image);
      } else {
        resolve(null)
      }
    });
    // reader.onload = function(e) {
    //     // browser completed reading file - display it
    //     console.log(e.target.result);
    // };
    user.getIdTokenResult(true).then((token) => {
      const httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': token.token
        })
      }
      this.httpClient.post(this.global.baseApiURL + "/v1/addHouse",
      {
        houseId: this.houseId,
        houseDetails: JSON.stringify(this.newHouse),
        image: result,
        imageType: this.image ? this.image.type : null,
        gid: this.gid
      }, httpOptions).subscribe((res) => {
        alert("House added successfully")
        this.router.navigate(["home"])
      }, error => {
        alert("Something went wrong... (Error: " + error + ")")
      })
    })
  }

  onChange(event) {
    this.image = event.srcElement.files[0];
  }

  addPerson() {
    this.newHouse.persons.push({})
  }

}
