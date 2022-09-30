import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILostItem } from '../lost-item.model';
import { LostItemService } from '../service/lost-item.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './lost-item-delete-dialog.component.html',
})
export class LostItemDeleteDialogComponent {
  lostItem?: ILostItem;

  constructor(protected lostItemService: LostItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lostItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
