import {Component, Input, OnInit} from '@angular/core';
import {NbDialogRef} from '@nebular/theme';

@Component({
  selector: 'ngx-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {
  @Input() title?: string = 'Thông báo';
  @Input() message: string;
  @Input() okTitle?: string = 'Đồng ý';
  @Input() cancelTitle?: string = 'Huỷ';
  @Input() hideCancel?;

  constructor(protected ref: NbDialogRef<ConfirmDialogComponent>) {
  }

  ngOnInit() {
  }

  close() {
    this.ref.close();
  }

  save() {
    this.ref.close('confirm');
  }
}
