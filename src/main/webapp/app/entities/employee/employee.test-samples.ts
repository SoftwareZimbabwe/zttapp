import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 7813,
};

export const sampleWithPartialData: IEmployee = {
  id: 14928,
  dob: dayjs('2022-09-29'),
  nationality: 'reboot Missouri',
};

export const sampleWithFullData: IEmployee = {
  id: 41420,
  name: 'Rustic',
  dob: dayjs('2022-09-29'),
  nationality: 'systemic zero',
};

export const sampleWithNewData: NewEmployee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
