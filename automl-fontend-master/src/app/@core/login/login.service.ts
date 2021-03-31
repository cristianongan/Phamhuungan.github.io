import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {flatMap} from 'rxjs/operators';

import {Login} from './login.model';
import {AccountService} from '../auth/account.service';
import {AuthServerProvider} from '../auth/auth-jwt.service';
import {Account} from '../user/account.model';
import {Router} from '@angular/router';

@Injectable({providedIn: 'root'})
export class LoginService {
  constructor(
    private accountService: AccountService,
    private authServerProvider: AuthServerProvider,
    private router: Router
  ) {
  }

  login(credentials: Login): Observable<Account | null> {
    return this.authServerProvider.login(credentials).pipe(flatMap(() => this.accountService.identity(true)));
  }
  logout(): void {
    this.authServerProvider.logout().subscribe(null, null, () => {this.accountService.authenticate(null);
    this.router.navigate(['auth/login']);
    });
  }
}
