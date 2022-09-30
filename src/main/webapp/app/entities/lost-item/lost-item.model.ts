import dayjs from 'dayjs/esm';

export interface ILostItem {
  id: number;
  dateLost?: dayjs.Dayjs | null;
  description?: string | null;
  province?: string | null;
  itemNumber?: string | null;
}

export type NewLostItem = Omit<ILostItem, 'id'> & { id: null };
