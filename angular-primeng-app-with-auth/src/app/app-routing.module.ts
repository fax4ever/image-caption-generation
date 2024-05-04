import { NgModule, ɵsetInjectorProfilerContext } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { authGuard } from './guards/auth.guard';
import { MainpageComponent } from './components/mainpage/mainpage.component';
import { PhotosComponent } from './photos/photos.component';


const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'mainpage',
    component: MainpageComponent,
    canActivate: [authGuard]
  },
  {
    path: 'photos',
    component: PhotosComponent,
    canActivate: [authGuard]
  },
  {
    path: '', redirectTo: 'mainpage', pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
