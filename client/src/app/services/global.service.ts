import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GlobalService {

  public isTesting = true
  public authHomePage = 'home'
  public unauthHomePage = 'signin'
  public baseApiURL = this.isTesting ? "http://localhost:3000/api" :  "/api"
  public title = "iCamp"
  public selectedParty = ""
  partyList: [{value, viewValue}] = [{
    value: 'createParty',
    viewValue: 'Create Party'
  }]

  constructor() { }
}
