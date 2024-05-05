import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { UploadFileService } from 'src/app/services/upload-file.service';

@Component({
  selector: 'app-mainpage',
  templateUrl: './mainpage.component.html',
  styleUrls: ['./mainpage.component.css']
})
export class MainpageComponent {
  
  caption : string = "";
  selectedFile = null;

  imageForm = this.fb.group({
    image : [null]
  });  

  constructor(private fb: FormBuilder, private router: Router, private uploadService: UploadFileService
  ) { }

  get image() {
    return this.imageForm.controls['image'];
  }

  logOut() {
    sessionStorage.clear();
    this.router.navigate(['login']);
  }

  photos() {
    this.router.navigate(['photos']);
  }

  changedImage(event : any) {
    this.selectedFile = event.target.files[0];
  }

  submitDetails() {
    if (!this.selectedFile) {
      return;
    }
    
    this.uploadService.pushFileToStorage( this.selectedFile )
      .subscribe(          
        (response) => { 
          console.log(response);
          if (response.type == 1) {
            this.caption = response.loaded + "/" + response.total;
          }
          if (response.type == 3) {
            this.caption = response + "";
          }
          if (response.type == 4) {
            this.caption = response.body as string;
          }
        },   
        (error) =>console.log(error)  
      );
  }
}
