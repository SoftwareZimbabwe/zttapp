import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LostItemDetailComponent } from './lost-item-detail.component';

describe('LostItem Management Detail Component', () => {
  let comp: LostItemDetailComponent;
  let fixture: ComponentFixture<LostItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LostItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lostItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LostItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LostItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lostItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lostItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
