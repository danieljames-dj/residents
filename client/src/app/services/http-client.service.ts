import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  constructor(private httpClient: HttpClient) { }

  post(url, params) {
    return this.httpClient.post(url, params, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    })
  }

  postAuth(url, params, token) {
    var args = new HttpParams();
    for (var key in params) {
        args = args.append(key, params[key])
    }
    return this.httpClient.post(url, args, {
      headers: new HttpHeaders({
          'Content-Type': 'application/x-www-form-urlencoded',
          'Authorization': token.token
      })
    })
  }

  getAuth(url, params, token) {
    return this.httpClient.get(url, {
      headers: new HttpHeaders({
          'Content-Type': 'application/x-www-form-urlencoded',
          'Authorization': token.token
      })
    })
  }
}
