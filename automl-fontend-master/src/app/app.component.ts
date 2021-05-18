/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
import { Component, OnInit } from '@angular/core';
import {NgSelectConfig} from '@ng-select/ng-select';


@Component({
  selector: 'ngx-app',
  template: '<router-outlet></router-outlet>',
})
export class AppComponent implements OnInit {

  constructor(
    // analytics: AnalyticsService,
    // private seoService: SeoService,
    private ngSelectConfig: NgSelectConfig) {
  }

  ngOnInit(): void {
    // this.analytics.trackPageViews();
    // this.seoService.trackCanonicalChanges();

    this.ngSelectConfig.notFoundText = 'Không có kết quả';
    this.ngSelectConfig.loadingText = 'Đang tìm';
    this.ngSelectConfig.clearAllText = 'Xóa';
    this.ngSelectConfig.addTagText = 'Thêm tag';
    this.ngSelectConfig.appendTo = 'body';
  }
}
