import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../lost-item.test-samples';

import { LostItemFormService } from './lost-item-form.service';

describe('LostItem Form Service', () => {
  let service: LostItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LostItemFormService);
  });

  describe('Service methods', () => {
    describe('createLostItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLostItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateLost: expect.any(Object),
            description: expect.any(Object),
            province: expect.any(Object),
            itemNumber: expect.any(Object),
          })
        );
      });

      it('passing ILostItem should create a new form with FormGroup', () => {
        const formGroup = service.createLostItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateLost: expect.any(Object),
            description: expect.any(Object),
            province: expect.any(Object),
            itemNumber: expect.any(Object),
          })
        );
      });
    });

    describe('getLostItem', () => {
      it('should return NewLostItem for default LostItem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLostItemFormGroup(sampleWithNewData);

        const lostItem = service.getLostItem(formGroup) as any;

        expect(lostItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewLostItem for empty LostItem initial value', () => {
        const formGroup = service.createLostItemFormGroup();

        const lostItem = service.getLostItem(formGroup) as any;

        expect(lostItem).toMatchObject({});
      });

      it('should return ILostItem', () => {
        const formGroup = service.createLostItemFormGroup(sampleWithRequiredData);

        const lostItem = service.getLostItem(formGroup) as any;

        expect(lostItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILostItem should not enable id FormControl', () => {
        const formGroup = service.createLostItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLostItem should disable id FormControl', () => {
        const formGroup = service.createLostItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
