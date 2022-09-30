import dayjs from 'dayjs/esm';

export interface IEmployee {
  id: number;
  name?: string | null;
  dob?: dayjs.Dayjs | null;
  nationality?: string | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
