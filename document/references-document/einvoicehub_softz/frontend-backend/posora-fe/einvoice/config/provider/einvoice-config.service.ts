import { inject, Injectable } from '@angular/core';
import { Observable, map, catchError, of } from 'rxjs';
import { ApiService, ApiResponseDto } from '@shared';

export interface EinvProviderDto {
    id: string;
    providerName: string;
    integrationUrl?: string;
    inactive?: boolean;
}

export interface EinvStoreProviderDto {
    id?: string;
    providerId?: string;
    partnerId?: string;
    partnerToken?: string;
    partnerUsr?: string;
    partnerPwd?: string;
    status?: number;
    integratedDate?: string;
    integrationUrl?: string;
    taxCode?: string;
}

export interface EinvStoreProviderRequest {
    providerId: string;
    bkavPartnerGuid?: string;
    bkavPartnerToken?: string;
    mobifoneUsername?: string;
    mobifonePassword?: string;
    mobifoneTaxCode?: string;
}

@Injectable({ providedIn: 'root' })
export class EinvoiceConfigService {
    #apiService = inject(ApiService);
    private baseUrl = 'einvoice/config';

    getProviders(): Observable<EinvProviderDto[]> {
        return this.#apiService.postApi<EinvProviderDto[]>('einvoice/provider/list', {}).pipe(
            map(res => res.data ?? []),
            catchError(() => of([]))
        );
    }

    getConfig(): Observable<EinvStoreProviderDto | null> {
        return this.#apiService.postApi<EinvStoreProviderDto>(`${this.baseUrl}/detail`, {}).pipe(
            map(res => res.data ?? null),
            catchError(() => of(null))
        );
    }

    saveConfig(request: EinvStoreProviderRequest): Observable<ApiResponseDto<EinvStoreProviderDto>> {
        return this.#apiService.postApi<EinvStoreProviderDto>(`${this.baseUrl}/save`, request);
    }

    deactivate(): Observable<ApiResponseDto<any>> {
        return this.#apiService.postApi<any>(`${this.baseUrl}/deactivate`, {});
    }

    validateConfig(request: EinvStoreProviderRequest): Observable<ApiResponseDto<{ success: boolean; message: string }>> {
        return this.#apiService.postApi<{ success: boolean; message: string }>(`${this.baseUrl}/validate`, request);
    }
}
