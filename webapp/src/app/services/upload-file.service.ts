import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UploadFileService {

  constructor(private https: HttpClient) { }

  pushFileToStorage(file: File): Observable<HttpEvent<{}>> {
    var url = 'http://localhost/caption/new-image/';
    var user = sessionStorage.getItem('username');
    if (user != null) {
      url = url + user;
    }

    const data: FormData = new FormData();
    data.append('file', file);
    const newRequest = new HttpRequest('POST', url, data, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.https.request(newRequest);
  }
}