import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILostItem, NewLostItem } from '../lost-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILostItem for edit and NewLostItemFormGroupInput for create.
 */
type LostItemFormGroupInput = ILostItem | PartialWithRequiredKeyOf<NewLostItem>;

type LostItemFormDefaults = Pick<NewLostItem, 'id'>;

type LostItemFormGroupContent = {
  id: FormControl<ILostItem['id'] | NewLostItem['id']>;
  dateLost: FormControl<ILostItem['dateLost']>;
  description: FormControl<ILostItem['description']>;
  province: FormControl<ILostItem['province']>;
  itemNumber: FormControl<ILostItem['itemNumber']>;
};

export type LostItemFormGroup = FormGroup<LostItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LostItemFormService {
  createLostItemFormGroup(lostItem: LostItemFormGroupInput = { id: null }): LostItemFormGroup {
    const lostItemRawValue = {
      ...this.getFormDefaults(),
      ...lostItem,
    };
    return new FormGroup<LostItemFormGroupContent>({
      id: new FormControl(
        { value: lostItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dateLost: new FormControl(lostItemRawValue.dateLost),
      description: new FormControl(lostItemRawValue.description),
      province: new FormControl(lostItemRawValue.province),
      itemNumber: new FormControl(lostItemRawValue.itemNumber),
    });
  }

  getLostItem(form: LostItemFormGroup): ILostItem | NewLostItem {
    return form.getRawValue() as ILostItem | NewLostItem;
  }

  resetForm(form: LostItemFormGroup, lostItem: LostItemFormGroupInput): void {
    const lostItemRawValue = { ...this.getFormDefaults(), ...lostItem };
    form.reset(
      {
        ...lostItemRawValue,
        id: { value: lostItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LostItemFormDefaults {
    return {
      id: null,
    };
  }
}
