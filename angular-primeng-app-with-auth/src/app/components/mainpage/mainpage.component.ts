import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mainpage',
  templateUrl: './mainpage.component.html',
  styleUrls: ['./mainpage.component.css']
})
export class MainpageComponent {
  [x: string]: any;
  caption: any;

  constructor(private router: Router) { }

  logOut() {
    sessionStorage.clear();
    this.router.navigate(['login']);
  }

  photos() {
    this.router.navigate(['photos']);
  }
}
