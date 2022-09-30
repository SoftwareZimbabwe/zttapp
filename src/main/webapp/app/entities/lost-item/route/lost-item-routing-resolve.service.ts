import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILostItem } from '../lost-item.model';
import { LostItemService } from '../service/lost-item.service';

@Injectable({ providedIn: 'root' })
export class LostItemRoutingResolveService implements Resolve<ILostItem | null> {
  constructor(protected service: LostItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILostItem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lostItem: HttpResponse<ILostItem>) => {
          if (lostItem.body) {
            return of(lostItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
