import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GlobalService {

  public authHomePage = 'home'
  public unauthHomePage = 'signin'
  public baseApiURL = "/api"//"http://localhost:3000/api"
  public title = "iCamp"
  public selectedParty = ""
  partyList: [{value, viewValue}] = [{
    value: 'createParty',
    viewValue: 'Create Party'
  }]

  constructor() { }
}
