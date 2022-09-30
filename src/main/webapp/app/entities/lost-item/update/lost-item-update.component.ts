import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { LostItemFormService, LostItemFormGroup } from './lost-item-form.service';
import { ILostItem } from '../lost-item.model';
import { LostItemService } from '../service/lost-item.service';

@Component({
  selector: 'jhi-lost-item-update',
  templateUrl: './lost-item-update.component.html',
})
export class LostItemUpdateComponent implements OnInit {
  isSaving = false;
  lostItem: ILostItem | null = null;

  editForm: LostItemFormGroup = this.lostItemFormService.createLostItemFormGroup();

  constructor(
    protected lostItemService: LostItemService,
    protected lostItemFormService: LostItemFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lostItem }) => {
      this.lostItem = lostItem;
      if (lostItem) {
        this.updateForm(lostItem);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lostItem = this.lostItemFormService.getLostItem(this.editForm);
    if (lostItem.id !== null) {
      this.subscribeToSaveResponse(this.lostItemService.update(lostItem));
    } else {
      this.subscribeToSaveResponse(this.lostItemService.create(lostItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILostItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(lostItem: ILostItem): void {
    this.lostItem = lostItem;
    this.lostItemFormService.resetForm(this.editForm, lostItem);
  }
}
