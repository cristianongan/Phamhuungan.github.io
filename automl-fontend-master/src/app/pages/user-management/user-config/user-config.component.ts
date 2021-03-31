import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Page} from '../../../@core/model/page.model';
import {UserService} from '../../../@core/user/user.service';
import {Router} from '@angular/router';
import {HttpHeaders} from '@angular/common/http';
import {IUser, User} from '../../../@core/user/user.model';
import {NbDialogService, NbToastrService} from '@nebular/theme';
import {FormBuilder, FormGroup} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {UserConfigUpdateComponent} from '../user-config-update/user-config-update.component';
import {ConfirmDialogComponent} from '../../../share/component/confirm-dialog/confirm-dialog.component';


@Component({
  selector: 'ngx-user-config',
  templateUrl: './user-config.component.html',
  styleUrls: ['./user-config.component.scss']
})
export class UserConfigComponent implements OnInit, AfterViewInit {
  page = new Page();
  users?: User[] = new Array<User>();
  columns = [
    {name: 'user.column.index', prop: 'index', flexGrow: 0.3},
    {name: 'user.column.account', prop: 'login', flexGrow: 1},
    {name: 'user.column.email', prop: 'email', flexGrow: 1},
    {name: 'user.column.status', prop: 'activated', flexGrow: 1},
    {name: 'user.column.phoneNumber', prop: 'phoneNumber', flexGrow: 1},
    {name: 'user.column.authoritiesName', prop: 'authoritiesName', flexGrow: 1},
    {name: 'user.column.active', prop: 'action_btn', flexGrow: 1}
  ];
  currentTheme: any = 'dark';

  data: IUser[];
  userForm: FormGroup = this.fb.group({
    authority: [null],
    keyword: [null]
  });
  authorities: any[];
  searchActive: boolean

  constructor(public userService: UserService,
              private dialogService: NbDialogService,
              private fb: FormBuilder,
              private translate: TranslateService,
              private toastrService: NbToastrService,
              private router: Router) {
    this.page.pageNumber = 0;
    this.page.size = 10;
  }

  ngOnInit() {
    this.userService.authorities().subscribe(authorities => {
      this.authorities = authorities
    });
    // this.catItemServiceService.fetch(CategoryId.DOMAIN).subscribe(domains => {
    //   this.domainData = domains
    // });
    this.setPage(this.page);
  }
  ngAfterViewInit(): void {
    setTimeout(() => {
      document.getElementById('keywordId').focus();
    }, 100);
  }
  search() {
    this.searchActive = true
    this.setPage({offset: 0});
  }

  setPage(pageInfo) {
    const pageToLoad: number = pageInfo.offset;
    this.userService.query({
      keyword: this.searchActive ? this.userForm.value.keyword : '',
      authority: this.searchActive ?  this.userForm.value.authority : '',
      page: pageToLoad,
      size: this.page.size
    }).subscribe(res => this.onSuccess(res.body, res.headers, pageToLoad));
  }

  protected onSuccess(data: any | null, headers: HttpHeaders, page: number): void {
    this.page.totalElements = Number(headers.get('X-Total-Count'));
    this.page.pageNumber = page || 0;
    this.users = data || [];
    this.users.forEach((u: any) => {
      u.authoritiesName = (u.authorities || []).map(a => a.displayName).sort().join();
    })
  }


  new() {
    const dialogNew = this.dialogService.open(UserConfigUpdateComponent, {
      closeOnBackdropClick: false,
      closeOnEsc: false,
      context: {
      },
    });
    dialogNew.onClose.subscribe(data => {
      if (data.result !== undefined &&  'complete' === data.result) {
        this.search();
      }
    });
  }

  edit(row) {
    const dialogUpdate = this.dialogService.open(UserConfigUpdateComponent, {
      context: {
        user: row
      },
      closeOnBackdropClick: false,
      closeOnEsc: false,
    });
    dialogUpdate.onClose.subscribe(data => {
      if (data.result === 'complete') {
        this.search();
      }
    });
  }

  resetPassword(user) {
    const dialog = this.dialogService.open(ConfirmDialogComponent, {
      autoFocus: true,
      context: {
        message: 'Bạn có chắc chắn reset mật khẩu người dùng này không?'
      },
    });
    dialog.onClose.subscribe(res => {
      if (res) {
        this.userService.resetPass(user?.id).subscribe( () => {
          this.toastrService.success('Mật khẩu được reset thành công về 123456a@', 'Thông báo');
          this.setPage(this.page);
        })
      }
    });
  }

  setActive(user, isActivated) {
    this.dialogService.open(ConfirmDialogComponent, {
      autoFocus: true,
      context: {
        message: 'Bạn có chắc chắn muốn chuyển trạng thái của người dùng không?'
      },
    }).onClose.subscribe(res => this.onComplete(res, user, isActivated));
  }
  onComplete(res, user, isActivated) {
    if (res) {
      user.activated = isActivated;
      this.userService.updateStatus(user).subscribe(
        () => {
          this.toastrService.success(this.translate.instant('user.changeStatusSuccess'), 'Thông báo');
          this.setPage(this.page);
        }
      );
    }
  }

}
