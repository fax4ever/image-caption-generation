import { Component, inject } from '@angular/core';
import { ImageItemComponent } from '../image-item/image-item.component';
import { Image } from '../interfaces/image';
import { ImagesService } from '../services/images.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-advanced-search',
  templateUrl: './advanced-search.component.html',
  styleUrls: ['./advanced-search.component.css']
})
export class AdvancedSearchComponent {
  images: Image[] = [];
  imagesService: ImagesService = inject(ImagesService);
  router: Router = inject(Router)
  page = 0;
  text : string = "";

  search(text: string) {
    this.page = 0;
    this.images = [];
    this.text = text;
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

  logOut() {
    sessionStorage.clear();
    this.router.navigate(['login']);
  }

  noImage() {
    return this.images.length == 0;
  }

  private updateImages() {
    if (!this.text) {
      return;
    }

    this.imagesService.findByCaption(this.text, this.page * 12, 12)
      .subscribe(
        (response) => {
          console.log(response);
          this.images = response;
        },
        (error) => console.log(error)
      )
  }
}
