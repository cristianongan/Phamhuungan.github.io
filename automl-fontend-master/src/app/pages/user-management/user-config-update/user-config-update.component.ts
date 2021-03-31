import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../../../@core/user/user.model';
import {UserService} from '../../../@core/user/user.service';
import {NbDialogRef, NbDialogService, NbToastrService} from '@nebular/theme';
import {onlyCharacterValidator} from '../../../share/directive/only-characters.directive';
import {Language} from '../../../@core/model/language.model';
import {ConfirmDialogComponent} from '../../../share/component/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'ngx-user-config-update',
  templateUrl: './user-config-update.component.html',
  styleUrls: ['./user-config-update.component.scss']
})
export class UserConfigUpdateComponent implements OnInit {
  @Input() user: User;
  languages: Language[];
  authorities: any[];
  domainData: any[];
  userInfo: FormGroup = this.fb.group({
    id: [null],
    login: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), onlyCharacterValidator(/^[a-zA-Z0-9_]{1,}$/)]],
    firstName: ['', [Validators.required, Validators.maxLength(50)]],
    lastName: ['', [Validators.required, Validators.maxLength(50)]],
    phoneNumber: ['', [Validators.required, Validators.maxLength(11), Validators.pattern('^[0-9]*')]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    activated: [true],
    // alarmLeader: [false],
    langKey: ['', Validators.required],
    authorities: [null, Validators.required],
    // domains: [null]
  });

  constructor(private fb: FormBuilder,
              // public activatedRoute: ActivatedRoute,
              protected router: Router,
              private userService: UserService,
              private dialogService: NbDialogService,
              private toastrService: NbToastrService,
              protected ref: NbDialogRef<UserConfigUpdateComponent>
  ) {
  }

  ngOnInit() {
    this.userInfo = this.fb.group({
      id: [null],
      login: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), onlyCharacterValidator(/^[a-zA-Z0-9_]{1,}$/)]],
      firstName: ['', [Validators.required, Validators.maxLength(100)]],
      phoneNumber: [null, [Validators.required, Validators.maxLength(11), Validators.pattern('^[0-9]*')]],
      email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
      activated: [true],
      alarmLeader: [false],
      langKey: ['', Validators.required],
      authorities: [null, Validators.required],
    });
    this.userService.authorities().subscribe(authorities => {
      this.authorities = authorities;
      const roleUser = authorities.find((a: any) => a.name === 'ROLE_USER');
      if (roleUser && !this.user)
        this.userInfo.get('authorities').patchValue([roleUser]);
    });
    this.languages = this.getAll();
    this.userInfo.get('langKey').patchValue(this.languages[0].langKey);
    if (this.user !== undefined && this.user !== null) {
      this.userService.find(this.user.login).subscribe(res => {
        this.userInfo.patchValue(res.body);
      });
    }
  }

  getAll(): Language[] {
    const langs: Language[] = [
      {langKey: 'vi', langTitle: 'Tiếng Việt'},
      {langKey: 'en', langTitle: 'Tiếng Anh'}
    ];
    return langs;
  }

  dismiss() {
    this.ref.close({result: 'close'});
  }

  onSave() {
    this.dialogService.open(ConfirmDialogComponent, {
      context: {
        title: 'Xác nhận thông tin',
        message: 'Bạn có chắc chắn muốn lưu thông tin không?'
      },
    }).onClose.subscribe(res => this.save(res));
  }

  save(confirm) {
    if (confirm) {
      if (this.userInfo.get('id').value !== null) {
        this.userService.update(this.userInfo.value).subscribe(() => this.onSaveSuccess(true), error => this.onSaveError(true, error));
      } else {
        this.userService.create(this.userInfo.value).subscribe(() => this.onSaveSuccess(false), error => this.onSaveError(false, error));
      }
    }
  }

  private onSaveSuccess(isUpdate) {
    this.ref.close({result: 'complete'});
    if (isUpdate) {
      this.toastrService.success('Cập nhật thành công', 'Thông báo');
    } else {
      this.toastrService.success('Thêm mới thành công , Mật khẩu mặc đinh là: 123456a@', 'Thông báo');
    }
  }

  private onSaveError(isUpdate, error) {
    if (error.status === 403) {
      this.toastrService.warning('Bạn không có quyền thực hiện thao tác', 'Thông báo');
    } else if (isUpdate) {
      this.toastrService.danger(error.error.message, 'Thông báo');
    } else {
      this.toastrService.danger(error.error.message, 'Thông báo');
    }
  }

}
