import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Image } from '../interfaces/image';
import { ImagesService } from '../services/images.service';

@Component({
  selector: 'app-photos',
  templateUrl: './photos.component.html',
  styleUrls: ['./photos.component.css']
})
export class PhotosComponent implements OnInit {
  page = 0;
  images: Image[] = [];
  user = sessionStorage.getItem('username');
  pro : boolean = sessionStorage.getItem('pro') != null;

  backgroundImages: string[] = [
    'https://picsum.photos/1920/1080',
    'https://picsum.photos/1920/1080',
    'https://picsum.photos/1920/1080',
    'https://picsum.photos/1920/1080'
  ];
  currentBackgroundIndex: number = 0;
  currentBackground: string = this.backgroundImages[0];

  constructor(private router: Router, private service: ImagesService) { }

  ngOnInit(): void {
    this.updateImages();
  }

  next(): void {
    this.page = this.page + 1;
    this.updateImages();
  }

  prev(): void {
    if (this.page > 0) {
      this.page = this.page - 1;
      this.updateImages();
    }
  }

  hiddenPro(): boolean {
    return !this.pro;
  }

  logOut() {
    sessionStorage.clear();
    this.router.navigate(['login']);
  }

  private updateImages() {
    this.service.findByUser(this.user as string, this.page * 12, 12)
      .subscribe(
        (response) => {
          console.log(response);
          this.images = response;
        },
        (error) => console.log(error)
      )
  }
}
