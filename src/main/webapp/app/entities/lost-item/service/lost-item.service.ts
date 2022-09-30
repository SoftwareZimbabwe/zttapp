import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILostItem, NewLostItem } from '../lost-item.model';

export type PartialUpdateLostItem = Partial<ILostItem> & Pick<ILostItem, 'id'>;

type RestOf<T extends ILostItem | NewLostItem> = Omit<T, 'dateLost'> & {
  dateLost?: string | null;
};

export type RestLostItem = RestOf<ILostItem>;

export type NewRestLostItem = RestOf<NewLostItem>;

export type PartialUpdateRestLostItem = RestOf<PartialUpdateLostItem>;

export type EntityResponseType = HttpResponse<ILostItem>;
export type EntityArrayResponseType = HttpResponse<ILostItem[]>;

@Injectable({ providedIn: 'root' })
export class LostItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lost-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lostItem: NewLostItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lostItem);
    return this.http
      .post<RestLostItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(lostItem: ILostItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lostItem);
    return this.http
      .put<RestLostItem>(`${this.resourceUrl}/${this.getLostItemIdentifier(lostItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(lostItem: PartialUpdateLostItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lostItem);
    return this.http
      .patch<RestLostItem>(`${this.resourceUrl}/${this.getLostItemIdentifier(lostItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLostItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLostItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLostItemIdentifier(lostItem: Pick<ILostItem, 'id'>): number {
    return lostItem.id;
  }

  compareLostItem(o1: Pick<ILostItem, 'id'> | null, o2: Pick<ILostItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getLostItemIdentifier(o1) === this.getLostItemIdentifier(o2) : o1 === o2;
  }

  addLostItemToCollectionIfMissing<Type extends Pick<ILostItem, 'id'>>(
    lostItemCollection: Type[],
    ...lostItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const lostItems: Type[] = lostItemsToCheck.filter(isPresent);
    if (lostItems.length > 0) {
      const lostItemCollectionIdentifiers = lostItemCollection.map(lostItemItem => this.getLostItemIdentifier(lostItemItem)!);
      const lostItemsToAdd = lostItems.filter(lostItemItem => {
        const lostItemIdentifier = this.getLostItemIdentifier(lostItemItem);
        if (lostItemCollectionIdentifiers.includes(lostItemIdentifier)) {
          return false;
        }
        lostItemCollectionIdentifiers.push(lostItemIdentifier);
        return true;
      });
      return [...lostItemsToAdd, ...lostItemCollection];
    }
    return lostItemCollection;
  }

  protected convertDateFromClient<T extends ILostItem | NewLostItem | PartialUpdateLostItem>(lostItem: T): RestOf<T> {
    return {
      ...lostItem,
      dateLost: lostItem.dateLost?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restLostItem: RestLostItem): ILostItem {
    return {
      ...restLostItem,
      dateLost: restLostItem.dateLost ? dayjs(restLostItem.dateLost) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLostItem>): HttpResponse<ILostItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLostItem[]>): HttpResponse<ILostItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
