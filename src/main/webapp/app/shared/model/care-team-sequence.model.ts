import { IItem } from 'app/shared/model/item.model';

export interface ICareTeamSequence {
  id?: number;
  careSeq?: number | null;
  item?: IItem | null;
}

export const defaultValue: Readonly<ICareTeamSequence> = {};
