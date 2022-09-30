import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LostItemComponent } from '../list/lost-item.component';
import { LostItemDetailComponent } from '../detail/lost-item-detail.component';
import { LostItemUpdateComponent } from '../update/lost-item-update.component';
import { LostItemRoutingResolveService } from './lost-item-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const lostItemRoute: Routes = [
  {
    path: '',
    component: LostItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LostItemDetailComponent,
    resolve: {
      lostItem: LostItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LostItemUpdateComponent,
    resolve: {
      lostItem: LostItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LostItemUpdateComponent,
    resolve: {
      lostItem: LostItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lostItemRoute)],
  exports: [RouterModule],
})
export class LostItemRoutingModule {}
