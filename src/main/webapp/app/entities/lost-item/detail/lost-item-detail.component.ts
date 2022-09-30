import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILostItem } from '../lost-item.model';

@Component({
  selector: 'jhi-lost-item-detail',
  templateUrl: './lost-item-detail.component.html',
})
export class LostItemDetailComponent implements OnInit {
  lostItem: ILostItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lostItem }) => {
      this.lostItem = lostItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
