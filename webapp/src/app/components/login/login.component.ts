import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Login } from 'src/app/interfaces/login';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  })

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private msgService: MessageService
  ) { }

  get username() {
    return this.loginForm.controls['username'];
  }

  get password() {
    return this.loginForm.controls['password'];
  }

  loginUser() {
    var login = this.loginForm.value as Login;

    this.authService.validate(login).subscribe(
      response => {
        if (response.valid) {
          sessionStorage.setItem('username', response.username);
          if (response.proUser) {
            sessionStorage.setItem('pro', "true");
          } else {
            sessionStorage.removeItem('pro')
          }
          this.router.navigate(['/mainpage']);
        } else {
          this.msgService.add({ severity: 'error', summary: 'Error', detail: 'username or password is wrong' });
        }
      },
      error => {
        this.msgService.add({ severity: 'error', summary: 'Error', detail: 'Something went wrong' });
      }
    )
  }
}
