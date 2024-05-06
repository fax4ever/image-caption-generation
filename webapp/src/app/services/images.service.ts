import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Image } from '../interfaces/image';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ImagesService {

  private galleryService = 'http://localhost/gallery';
  private captionService = 'http://localhost/caption';

  constructor(private http: HttpClient) { }

  findByUser(user: string, offset: number, limit: number): Observable<Image[]> {
    return this.http.get<Image[]>(`${this.galleryService}/image/user/${user}?offset=${offset}&limit=${limit}`)
      .pipe(
        map((images) => {
          for (var image of images) {
            image.filename = `${this.captionService}/image/${image.username}/${image.filename}`;
          }
          return images;
        })
      );
  }

  findByCaption(caption: string, offset: number, limit: number): Observable<Image[]> {
    return this.http.get<Image[]>(`${this.galleryService}/image/caption/${caption}?offset=${offset}&limit=${limit}`)
      .pipe(
        map((images) => {
          for (var image of images) {
            image.filename = `${this.captionService}/image/${image.username}/${image.filename}`;
          }
          return images;
        })
      );
  }

  findByDate(start: Date, end: Date, offset: number, limit: number): Observable<Image[]> {
    return this.http.get<Image[]>(`${this.galleryService}/image/from/${start}/to/${end}?offset=${offset}&limit=${limit}`)
      .pipe(
        map((images) => {
          for (var image of images) {
            image.filename = `${this.captionService}/image/${image.username}/${image.filename}`;
          }
          return images;
        })
      );
  }
}
