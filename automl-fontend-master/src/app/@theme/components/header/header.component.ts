import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {
  NbDialogService,
  NbIconLibraries,
  NbMediaBreakpointsService, NbMenuItem,
  NbMenuService,
  NbSidebarService,
  NbThemeService,
} from '@nebular/theme';

import {LayoutService} from '../../../@core/utils';
import {map, takeUntil} from 'rxjs/operators';
import {Subject} from 'rxjs';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {AuthoritiesConstant} from '../../../authorities.constant';
import {AccountService} from '../../../@core/auth/account.service';
import {ChangePasswordComponent} from '../../../auth-routing/change-password/change-password.component';
import {MenuItem} from 'primeng/api';

@Component({
  selector: 'ngx-header',
  styleUrls: ['./header.component.scss'],
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit, OnDestroy, AfterViewInit {
  authoritiesConstant = AuthoritiesConstant;
  private destroy$: Subject<void> = new Subject<void>();
  userPictureOnly: boolean = false;
  user: any;
  items: MenuItem[] = [
    {
      label: 'AutoML tool',
      routerLink: 'dashboard',
    },
    {
      label: 'Introduction',
      routerLink: 'introduction',
    },
    {
      label: 'Run models',
      items: [
        {
          label: 'Run existing models',
          routerLink: 'run-existing-models',
        },
        {
          label: 'Run new models',
          routerLink: 'run-new-models',
        },
      ],
    },
    {
      label: 'Monitor',
      items: [
        {
          label: 'Monitor projects',
          routerLink: 'project-list',
        },
        {
          label: 'Monitor models',
          routerLink: 'model-list',
        },
      ],
    },
  ];

  currentTheme = 'dark';

  userMenu = [/*{title: 'Đổi mật khẩu', target: 'changePassword'},*/ {title: 'Đăng xuất', target: 'logout'}];

  constructor(private sidebarService: NbSidebarService,
              private menuService: NbMenuService,
              private themeService: NbThemeService,
              private layoutService: LayoutService,
              private accountService: AccountService,
              private dialogService: NbDialogService,
              private breakpointService: NbMediaBreakpointsService,
              public router: Router,
              private iconsLibrary: NbIconLibraries) {
    iconsLibrary.registerFontPack('fa', {packClass: 'fa', iconClassPrefix: 'fa'});
  }

  ngAfterViewInit() {
    // this.toggleSidebar()
  }

  ngOnInit() {
    this.currentTheme = this.themeService.currentTheme;
    // this.userService.getUsers()
    //   .pipe(takeUntil(this.destroy$))
    //   .subscribe((users: any) => {
    //     this.user = users.nick;
    //     console.log(users);
    //   });
    this.checkActiveRoute('params');
    this.menuService.onItemClick()
      .pipe(
        takeUntil(this.destroy$),
      )
      .subscribe((event: any) => {
          if (event.item.target === 'logout') {
            this.router.navigate(['auth/logout']);
          }
          if (event.item.target === 'changePassword') {
            // this.router.navigate(['auth/change-password']);
            const ref = this.dialogService.open(ChangePasswordComponent, {
              closeOnBackdropClick: false,
              closeOnEsc: false,
              context: {},
            });
          }
          // if (event.item.target === 'favorite') {
          //   this.openDialogInsertFavorite();
          // }
        },
      );
    // this.user = {name: 'admin'};
    this.accountService.identity().subscribe(res => {
      this.user = res;
    });
    const {xl} = this.breakpointService.getBreakpointsMap();
    this.themeService.onMediaQueryChange()
      .pipe(
        map(([, currentBreakpoint]) => currentBreakpoint.width < xl),
        takeUntil(this.destroy$),
      )
      .subscribe((isLessThanXl: boolean) => this.userPictureOnly = isLessThanXl);

    this.themeService.onThemeChange()
      .pipe(
        map(({name}) => name),
        takeUntil(this.destroy$),
      )
      .subscribe(themeName => this.currentTheme = themeName);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  checkActiveRoute(params) {
    const pathname = window.location.pathname;
    return pathname.includes(params);
  }

  changeTheme(themeName: string) {
    this.themeService.changeTheme(themeName);
  }

  toggleSidebar(): boolean {
    this.sidebarService.toggle(false, 'menu-sidebar');
    // this.layoutService.changeLayoutSize();

    return false;
  }

  navigateHome() {
    this.menuService.navigateHome();
    return false;
  }
}
