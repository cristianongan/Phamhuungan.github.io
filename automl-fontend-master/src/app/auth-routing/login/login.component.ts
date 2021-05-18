import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LoginService} from '../../@core/login/login.service';
import {Router} from '@angular/router';
import {NbIconConfig, NbThemeService, NbToastrService} from '@nebular/theme';
import {onlyCharacterValidator} from '../../share/directive/only-characters.directive';
import {TranslateService} from '@ngx-translate/core';
import {SessionStorageService} from 'ngx-webstorage';

@Component({
  selector: 'ngx-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, AfterViewInit {
  loginForm: FormGroup;
  authenticationError = false;
  isLoading: boolean;

  constructor(private fb: FormBuilder,
              private loginService: LoginService,
              public themeService: NbThemeService,
              public sessionStorage: SessionStorageService,
              private router: Router,
              private translate: TranslateService,
              private toastrService: NbToastrService
  ) {
  }

  ngOnInit() {
    this.loginForm = this.fb.group({
      account: [null, [Validators.required, Validators.maxLength(20) , onlyCharacterValidator(/^[a-zA-Z0-9_]{1,}$/)]],
      password: [null, [Validators.required, Validators.maxLength(20) , Validators.minLength(4)]],
      rememberMe: [true],
    });
    this.loginForm.valueChanges.subscribe(value => {
      this.authenticationError = false;
    })
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      document.getElementById('account').focus();
    }, 100);
  }

  signIn() {
    this.isLoading = true
    this.loginService
      .login({
        username: this.loginForm.get('account')!.value,
        password: this.loginForm.get('password')!.value,
        tokenDevice: 'test_1',
        deviceName: 'device_test1',
        rememberMe: this.loginForm.get('rememberMe')!.value
      })
      .subscribe(
        () => {
          this.isLoading = false
          this.authenticationError = false;
          this.router.navigate(['']);
        },
        (error) => {
          if (error) {
            const iconConfig: NbIconConfig = {icon: 'alert-circle-outline', pack: 'eva'};
            this.toastrService.danger(error.error ? error.error.message : 'Có lỗi hệ thống xảy ra trong quá trình xử lý.', 'Đăng nhập không thành công', iconConfig)
            this.isLoading = false
            this.authenticationError = true
          }
        }
      );
  }

  onClose() {
    this.authenticationError = false;
  }
  forgotPassword() {
    this.router.navigate(['auth/request-password']);
  }
}
