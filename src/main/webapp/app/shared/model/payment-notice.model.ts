import dayjs from 'dayjs';
import { IPayNotErrorMessages } from 'app/shared/model/pay-not-error-messages.model';
import { IPaymentReconciliation } from 'app/shared/model/payment-reconciliation.model';
import { PaymentStatusEnum } from 'app/shared/model/enumerations/payment-status-enum.model';

export interface IPaymentNotice {
  id?: number;
  guid?: string | null;
  parsed?: string | null;
  identifier?: string | null;
  paymentDate?: string | null;
  amount?: number | null;
  paymentStatus?: PaymentStatusEnum | null;
  errors?: IPayNotErrorMessages[] | null;
  payment?: IPaymentReconciliation | null;
}

export const defaultValue: Readonly<IPaymentNotice> = {};
