import dayjs from 'dayjs/esm';

import { ILostItem, NewLostItem } from './lost-item.model';

export const sampleWithRequiredData: ILostItem = {
  id: 48243,
};

export const sampleWithPartialData: ILostItem = {
  id: 73728,
  dateLost: dayjs('2022-09-29'),
  description: 'Soft Frozen',
};

export const sampleWithFullData: ILostItem = {
  id: 54770,
  dateLost: dayjs('2022-09-29'),
  description: 'generating Reunion',
  province: 'Manager SMTP',
  itemNumber: 'Hollow Garden',
};

export const sampleWithNewData: NewLostItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
