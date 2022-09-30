import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILostItem } from '../lost-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../lost-item.test-samples';

import { LostItemService, RestLostItem } from './lost-item.service';

const requireRestSample: RestLostItem = {
  ...sampleWithRequiredData,
  dateLost: sampleWithRequiredData.dateLost?.format(DATE_FORMAT),
};

describe('LostItem Service', () => {
  let service: LostItemService;
  let httpMock: HttpTestingController;
  let expectedResult: ILostItem | ILostItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LostItemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a LostItem', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const lostItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(lostItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LostItem', () => {
      const lostItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(lostItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LostItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LostItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LostItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLostItemToCollectionIfMissing', () => {
      it('should add a LostItem to an empty array', () => {
        const lostItem: ILostItem = sampleWithRequiredData;
        expectedResult = service.addLostItemToCollectionIfMissing([], lostItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lostItem);
      });

      it('should not add a LostItem to an array that contains it', () => {
        const lostItem: ILostItem = sampleWithRequiredData;
        const lostItemCollection: ILostItem[] = [
          {
            ...lostItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLostItemToCollectionIfMissing(lostItemCollection, lostItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LostItem to an array that doesn't contain it", () => {
        const lostItem: ILostItem = sampleWithRequiredData;
        const lostItemCollection: ILostItem[] = [sampleWithPartialData];
        expectedResult = service.addLostItemToCollectionIfMissing(lostItemCollection, lostItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lostItem);
      });

      it('should add only unique LostItem to an array', () => {
        const lostItemArray: ILostItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const lostItemCollection: ILostItem[] = [sampleWithRequiredData];
        expectedResult = service.addLostItemToCollectionIfMissing(lostItemCollection, ...lostItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lostItem: ILostItem = sampleWithRequiredData;
        const lostItem2: ILostItem = sampleWithPartialData;
        expectedResult = service.addLostItemToCollectionIfMissing([], lostItem, lostItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lostItem);
        expect(expectedResult).toContain(lostItem2);
      });

      it('should accept null and undefined values', () => {
        const lostItem: ILostItem = sampleWithRequiredData;
        expectedResult = service.addLostItemToCollectionIfMissing([], null, lostItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lostItem);
      });

      it('should return initial array if no LostItem is added', () => {
        const lostItemCollection: ILostItem[] = [sampleWithRequiredData];
        expectedResult = service.addLostItemToCollectionIfMissing(lostItemCollection, undefined, null);
        expect(expectedResult).toEqual(lostItemCollection);
      });
    });

    describe('compareLostItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLostItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLostItem(entity1, entity2);
        const compareResult2 = service.compareLostItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLostItem(entity1, entity2);
        const compareResult2 = service.compareLostItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLostItem(entity1, entity2);
        const compareResult2 = service.compareLostItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
