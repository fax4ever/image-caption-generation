import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-photos',
  templateUrl: './photos.component.html',
  styleUrls: ['./photos.component.css']
})
export class PhotosComponent implements OnInit {
  images: string[] = [
    'https://via.placeholder.com/250?text=Image+1',
    'https://via.placeholder.com/250?text=Image+2',
    'https://via.placeholder.com/250?text=Image+3',
    'https://via.placeholder.com/250?text=Image+4',
    'https://via.placeholder.com/250?text=Image+5',
    'https://via.placeholder.com/250?text=Image+6',
    'https://via.placeholder.com/250?text=Image+7',
    'https://via.placeholder.com/250?text=Image+8',
    'https://via.placeholder.com/250?text=Image+9',
    'https://via.placeholder.com/250?text=Image+10',
    'https://via.placeholder.com/250?text=Image+11',
    'https://via.placeholder.com/250?text=Image+12'
  ]; // Sample images for the grid
   backgroundImages: string[] = [
    'https://source.unsplash.com/random/1024x768?nature',
    'https://source.unsplash.com/random/1024x768?water',
    'https://source.unsplash.com/random/1024x768?forest',
    'https://source.unsplash.com/random/1024x768?city'
  ];
  currentBackgroundIndex: number = 0;
  currentBackground: string = this.backgroundImages[0];

  constructor(private router: Router) { }

  ngOnInit(): void {
    const username = 'User'; // This could be dynamic depending on your application's user management
    alert(`Hi ${username}! Welcome to your photo gallery.`);
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (file) {
      const reader = new FileReader();

      reader.onload = (e: any) => {
        this.images.push(e.target.result);
      };

      reader.readAsDataURL(file);
    }
  }

  logOut() {
    sessionStorage.clear();
    this.router.navigate(['login']);
  }
}
