import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LostItemFormService } from './lost-item-form.service';
import { LostItemService } from '../service/lost-item.service';
import { ILostItem } from '../lost-item.model';

import { LostItemUpdateComponent } from './lost-item-update.component';

describe('LostItem Management Update Component', () => {
  let comp: LostItemUpdateComponent;
  let fixture: ComponentFixture<LostItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lostItemFormService: LostItemFormService;
  let lostItemService: LostItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LostItemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LostItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LostItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lostItemFormService = TestBed.inject(LostItemFormService);
    lostItemService = TestBed.inject(LostItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const lostItem: ILostItem = { id: 456 };

      activatedRoute.data = of({ lostItem });
      comp.ngOnInit();

      expect(comp.lostItem).toEqual(lostItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILostItem>>();
      const lostItem = { id: 123 };
      jest.spyOn(lostItemFormService, 'getLostItem').mockReturnValue(lostItem);
      jest.spyOn(lostItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lostItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lostItem }));
      saveSubject.complete();

      // THEN
      expect(lostItemFormService.getLostItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(lostItemService.update).toHaveBeenCalledWith(expect.objectContaining(lostItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILostItem>>();
      const lostItem = { id: 123 };
      jest.spyOn(lostItemFormService, 'getLostItem').mockReturnValue({ id: null });
      jest.spyOn(lostItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lostItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lostItem }));
      saveSubject.complete();

      // THEN
      expect(lostItemFormService.getLostItem).toHaveBeenCalled();
      expect(lostItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILostItem>>();
      const lostItem = { id: 123 };
      jest.spyOn(lostItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lostItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lostItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
