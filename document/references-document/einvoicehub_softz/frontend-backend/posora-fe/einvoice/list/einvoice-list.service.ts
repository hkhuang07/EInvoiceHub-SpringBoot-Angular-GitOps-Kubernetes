import { inject, Injectable } from '@angular/core';
import { Observable, map, catchError, of } from 'rxjs';
import { ApiService, ApiResponseDto } from '@shared';
import { EinvoiceInvoiceDto, InvoiceSearchRequest, InvoiceSearchResponse, InvoiceStatus } from './einvoice-list.model';

@Injectable({ providedIn: 'root' })
export class EinvoiceInvoiceService {
    #apiService = inject(ApiService);
    private baseUrl = 'einvoice/hub';
    private invoiceStatusUrl = 'einvoice/invoice-status';

    list(request: InvoiceSearchRequest): Observable<ApiResponseDto<InvoiceSearchResponse>> {
        return this.#apiService.postApi<InvoiceSearchResponse>(`${this.baseUrl}/list`, request);
    }

    getDetail(id: string): Observable<EinvoiceInvoiceDto | null> {
        return this.#apiService.postApi<EinvoiceInvoiceDto>(`${this.baseUrl}/detail`, { id }).pipe(
            map(res => res.data ?? null),
            catchError(() => of(null))
        );
    }

    signInvoice(id: string): Observable<ApiResponseDto<any>> {
        return this.#apiService.postApi<any>(`${this.baseUrl}/sign`, [{ invoiceId: id }]);
    }

    submitInvoice(data: any): Observable<ApiResponseDto<any>> {
        return this.#apiService.postApi<any>(`${this.baseUrl}/submit`, data);
    }

    loadInvoiceStatus(): Observable<ApiResponseDto<InvoiceStatus[]>> {
        return this.#apiService.postApi<InvoiceStatus[]>(`${this.invoiceStatusUrl}/list`, {});
    } 
}
