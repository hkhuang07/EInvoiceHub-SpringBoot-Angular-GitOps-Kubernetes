import { inject, Injectable } from '@angular/core';
import { Observable, map, catchError, of } from 'rxjs';
import { ApiService, ApiResponseDto } from '@shared';
import { EinvProviderDto } from '../provider/einvoice-config.service';

export interface EinvStoreSerialDto {
    id?: string;
    tenantId?: string;
    storeId?: string;
    providerId?: string;
    invoiceTypeId?: number;
    invoiceTypeName?: string;
    invoiceForm?: string;
    invoiceSerial?: string;
    startDate?: string;
    status?: number;
    providerSerialId?: string;
    createdDate?: string;
    updatedDate?: string;
}

export interface EinvStoreSerialRequest {
    id?: string;
    providerId?: string;
    invoiceTypeId?: number;
    invoiceForm?: string;
    invoiceSerial?: string;
    startDate?: string;
    status?: number;
}


export interface InvoiceStatusDto {
    id: number;
    name: string;
    note?: string;
}

export interface InvoiceTypeDto {
    id: number;
    name: string;
    note?: string;
}

@Injectable({ providedIn: 'root' })
export class EinvStoreSerialService {
    #apiService = inject(ApiService);
    private baseUrl = 'einvoice/serial';

    getList(): Observable<EinvStoreSerialDto[]> {
        return this.#apiService.postApi<EinvStoreSerialDto[]>(`${this.baseUrl}/list`, {}).pipe(
            map(res => res.data ?? []),
            catchError(() => of([]))
        );
    }

    getListByProvider(providerId: string): Observable<EinvStoreSerialDto[]> {
        return this.#apiService.postApi<EinvStoreSerialDto[]>(`${this.baseUrl}/list-by-provider`, { providerId }).pipe(
            map(res => res.data ?? []),
            catchError(() => of([]))
        );
    }

    getDetail(id: string): Observable<EinvStoreSerialDto | null> {
        return this.#apiService.postApi<EinvStoreSerialDto>(`${this.baseUrl}/detail`, { id }).pipe(
            map(res => res.data ?? null),
            catchError(() => of(null))
        );
    }

    save(request: EinvStoreSerialRequest): Observable<ApiResponseDto<EinvStoreSerialDto>> {
        return this.#apiService.postApi<EinvStoreSerialDto>(`${this.baseUrl}/save`, request);
    }

    delete(id: string): Observable<ApiResponseDto<any>> {
        return this.#apiService.postApi<any>(`${this.baseUrl}/delete`, { id });
    }

    approve(id: string): Observable<ApiResponseDto<EinvStoreSerialDto>> {
        return this.#apiService.postApi<EinvStoreSerialDto>(`${this.baseUrl}/approve`, { id });
    }

    deactivate(id: string): Observable<ApiResponseDto<EinvStoreSerialDto>> {
        return this.#apiService.postApi<EinvStoreSerialDto>(`${this.baseUrl}/deactivate`, { id });
    }

    getProviders(): Observable<EinvProviderDto[]> {
        return this.#apiService.postApi<EinvProviderDto[]>('einvoice/provider/list', {}).pipe(
            map(res => res.data ?? []),
            catchError(() => of([]))
        );
    }

    getInvoiceTypes(): Observable<InvoiceTypeDto[]> {
        return this.#apiService.postApi<InvoiceTypeDto[]>('einvoice/invoice-type/list', {}).pipe(
            map(res => res.data ?? []),
            catchError(() => of([]))
        );
    }

    getInvoiceStatuses(): Observable<InvoiceStatusDto[]> {
        return this.#apiService.postApi<InvoiceStatusDto[]>('einvoice/invoice-status/list', {}).pipe(
            map(res => res.data ?? []),
            catchError(() => of([]))
        );
    }
}
