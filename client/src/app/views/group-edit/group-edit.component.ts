import { Component, OnInit } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { GlobalService } from 'src/app/services/global.service';
import { HttpClientService } from 'src/app/services/http-client.service';
import {COMMA, ENTER} from '@angular/cdk/keycodes';

@Component({
  selector: 'app-group-edit',
  templateUrl: './group-edit.component.html',
  styleUrls: ['./group-edit.component.css']
})
export class GroupEditComponent implements OnInit {

  groupDetails: {gid: string, gname: string, summary: string, subGroups: Array<string>, inputDetails: Array<string>, personDetails: Array<string>}
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];

  constructor(private route: ActivatedRoute, private authService: AuthService, private router: Router, private afAuth: AngularFireAuth, private httpClient: HttpClientService,
    private global: GlobalService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.groupDetails = {
        gid: params.gid,
        gname: params.gname || "",
        summary: params.summary || "",
        subGroups: params.subGroups || [],
        inputDetails: params.inputDetails || [],
        personDetails: params.personDetails || []
      }
    });
  }

  updateGroup() {
    let user = this.authService.getCurrentUser();
    user.getIdTokenResult(true).then((token) => {
      this.httpClient.postAuth(this.global.baseApiURL + (this.groupDetails.gid == undefined ? "/v1/createGroup" : "/v1/updateGroup"), this.groupDetails, token).subscribe((res) => {
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

  add(event): void {
    const input = event.input;
    const value = event.value;

    // Add our fruit
    if ((value || '').trim()) {
      this.groupDetails.subGroups.push(value.trim());
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }

  add1(event): void {
    const input = event.input;
    const value = event.value;

    // Add our fruit
    if ((value || '').trim()) {
      this.groupDetails.inputDetails.push(value.trim());
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }

  add2(event): void {
    const input = event.input;
    const value = event.value;

    // Add our fruit
    if ((value || '').trim()) {
      this.groupDetails.personDetails.push(value.trim());
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }

  remove(subGroup): void {
    const index = this.groupDetails.subGroups.indexOf(subGroup);

    if (index >= 0) {
      this.groupDetails.subGroups.splice(index, 1);
    }
  }

  remove1(subGroup): void {
    const index = this.groupDetails.inputDetails.indexOf(subGroup);

    if (index >= 0) {
      this.groupDetails.inputDetails.splice(index, 1);
    }
  }

  remove2(subGroup): void {
    const index = this.groupDetails.personDetails.indexOf(subGroup);

    if (index >= 0) {
      this.groupDetails.personDetails.splice(index, 1);
    }
  }

}
