import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LostItemComponent } from './list/lost-item.component';
import { LostItemDetailComponent } from './detail/lost-item-detail.component';
import { LostItemUpdateComponent } from './update/lost-item-update.component';
import { LostItemDeleteDialogComponent } from './delete/lost-item-delete-dialog.component';
import { LostItemRoutingModule } from './route/lost-item-routing.module';

@NgModule({
  imports: [SharedModule, LostItemRoutingModule],
  declarations: [LostItemComponent, LostItemDetailComponent, LostItemUpdateComponent, LostItemDeleteDialogComponent],
})
export class LostItemModule {}
