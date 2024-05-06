import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuardPro: CanActivateFn = (route, state) => {
  if (sessionStorage.getItem('username') && sessionStorage.getItem('pro')) {
    return true;
  } else {
    const router = inject(Router);
    return router.navigate(['login']);
  }
};
