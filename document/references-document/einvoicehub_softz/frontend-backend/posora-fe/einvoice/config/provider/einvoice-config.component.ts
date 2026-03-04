import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';

import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { FormlyFieldConfig, FormlyModule } from '@ngx-formly/core';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TooltipModule } from 'primeng/tooltip';
import { FormComponent, NotifyService } from '@shared';
import { FormlyFieldType } from '@/shared/types/formly-field.types';
import { EinvoiceConfigService, EinvStoreProviderRequest } from './einvoice-config.service';

// #region Types & Interfaces
interface Provider {
    label: string;
    value: string;
}

interface EinvoiceConfigModel {
    provider: string;
    bkavPartnerGuid: string;
    bkavPartnerToken: string;
    mobifoneUsername: string;
    mobifonePassword: string;
    mobifoneTaxCode: string;
}

interface RealCredentials {
    bkavPartnerToken: string;
    mobifonePassword: string;
}

const enum IntegrationStatus {
    NotIntegrated = 0,
    Integrated = 1,
    Cancelled = 8
}

const DEFAULT_MODEL: EinvoiceConfigModel = {
    provider: 'BKAV',
    bkavPartnerGuid: '',
    bkavPartnerToken: '',
    mobifoneUsername: '',
    mobifonePassword: '',
    mobifoneTaxCode: ''
};
// #endregion

@Component({
    selector: 'app-einvoice-config',
    standalone: true,
    imports: [
        ReactiveFormsModule,
        FormlyModule,
        TranslateModule,
        ButtonModule,
        CardModule,
        TooltipModule,
        FormComponent
    ],
    template: `
    <div class="flex items-center justify-center content-center mt-5">
        <p-card [style]="{width: '600px'}" styleClass="shadow-4">
            <ng-template pTemplate="header">
                <div class="flex items-center justify-center py-4">
                    <span class="text-2xl font-bold text-blue-500">{{ 'einv_config.title' | translate }}</span>
                </div>
            </ng-template>

            <div class="p-4">
                <app-form
                    [fields]="formFields"
                    [form]="form"
                    [model]="model"
                    [isDisabled]="isLoading"
                    [showSubmitButton]="false"
                    [showResetButton]="false">
                </app-form>
                
                <!-- Status & Action Buttons -->
                <div class="flex items-center gap-2 pt-4 border-t border-gray-200">
                    <div class="flex items-center gap-2">
                        <span class="text-sm font-medium">{{ 'einv_config.status' | translate }}:</span>
                        <span [class]="statusClass">{{ statusText }}</span>
                    </div>
                    <div class="flex gap-2">
                        @if (currentStatus === 1) {
                            <button pButton type="button" 
                                [label]="'einv_config.btn_cancel_integration' | translate" icon="pi pi-times" 
                                class="p-button-danger p-button-outlined p-button-sm"
                                [disabled]="isEditMode || isLoading"
                                [pTooltip]="isEditMode ? ('einv_config.tooltip_save_first' | translate) : ''"
                                tooltipPosition="top"
                                (click)="onCancelIntegration()">
                            </button>
                        } @else {
                            <button pButton type="button" 
                                [label]="'einv_config.btn_validate' | translate" icon="pi pi-check-circle" 
                                class="p-button-success p-button-outlined p-button-sm"
                                [disabled]="isEditMode || isLoading"
                                [pTooltip]="isEditMode ? ('einv_config.tooltip_save_before_validate' | translate) : ''"
                                tooltipPosition="top"
                                (click)="onValidate()">
                            </button>
                        }
                    </div>
                </div>
                
                <!-- Edit & Save Buttons -->
                <div class="flex justify-end gap-2 pt-3">
                    @if (hasExistingConfig && !isEditMode && currentStatus !== 1) {
                        <button pButton type="button" 
                            [label]="'einv_config.btn_edit' | translate" icon="pi pi-pencil" 
                            class="p-button-secondary p-button-outlined"
                            (click)="onEdit()">
                        </button>
                    }
                    @if (hasExistingConfig && isEditMode) {
                        <button pButton type="button" 
                            [label]="'einv_config.btn_cancel_edit' | translate" icon="pi pi-times" 
                            class="p-button-secondary p-button-outlined"
                            (click)="onCancelEdit()">
                        </button>
                    }
                    <button pButton type="button" 
                        [label]="'einv_config.btn_save' | translate" icon="pi pi-save" 
                        class="p-button-primary"
                        [disabled]="!isEditMode || isLoading"
                        (click)="onSave()">
                    </button>
                </div>
            </div>
        </p-card>
    </div>
    `,
    styles: [`
        :host ::ng-deep .p-card {
            border-radius: 12px;
            overflow: hidden;
            padding: 0 !important;
        }
        :host ::ng-deep .p-card .p-card-body,
        :host ::ng-deep .p-card .p-card-content {
            padding: 0 !important;
        }
        :host ::ng-deep .form-group {
            margin-bottom: 1.25rem;
        }
        :host ::ng-deep .p-inputtext {
            width: 100%;
            padding: 0.75rem;
        }
        :host ::ng-deep .p-select {
            width: 100%;
        }
    `]
})
export default class EinvoiceConfigComponent implements OnInit {
    // #region Dependencies
    readonly #notify = inject(NotifyService);
    readonly #translate = inject(TranslateService);
    readonly #service = inject(EinvoiceConfigService);
    readonly #cdr = inject(ChangeDetectorRef);
    readonly #fb = inject(FormBuilder);
    // #endregion

    // #region State
    isLoading = false;
    isEditMode = false;
    hasExistingConfig = false;
    currentStatus: number | null = IntegrationStatus.NotIntegrated;
    statusText = 'Chưa tích hợp';
    statusClass = 'text-gray-500';

    form: FormGroup = this.#fb.group({});
    formFields: FormlyFieldConfig[] = [];
    model: EinvoiceConfigModel = { ...DEFAULT_MODEL };
    originalModel: EinvoiceConfigModel = { ...DEFAULT_MODEL };
    realCredentials: RealCredentials = { bkavPartnerToken: '', mobifonePassword: '' };
    providers: Provider[] = [];
    // #endregion

    // #region Lifecycle
    ngOnInit(): void {
        this.loadProviders();
        this.loadConfig();
    }
    // #endregion

    // #region Public Methods
    onEdit(): void {
        this.originalModel = { ...this.model };
        this.setEditMode(true);
    }

    onCancelEdit(): void {
        this.model = { ...this.originalModel };
        this.setEditMode(false);
    }

    onSave(): void {
        this.form.markAllAsTouched();
        if (!this.form.valid) {
            this.#notify.alertWarning(this.#translate.instant('Message.InvalidData'));
            return;
        }

        const request = this.buildSaveRequest();
        this.isLoading = true;

        this.#service.saveConfig(request).subscribe({
            next: (res) => {
                this.isLoading = false;
                if (res.responseCode === '200') {
                    this.#notify.alertSuccess(this.#translate.instant('Message.SaveSuccess'));
                    this.originalModel = { ...this.model };
                    this.hasExistingConfig = true;
                    this.setEditMode(false);
                    if (res.data?.status !== undefined) {
                        this.updateStatus(res.data.status);
                    }
                } else {
                    this.#notify.alertError(res.message || this.#translate.instant('einv_config.msg.save_failed'));
                }
            },
            error: () => {
                this.isLoading = false;
                this.#notify.alertError(this.#translate.instant('einv_config.msg.save_error'));
            }
        });
    }

    onValidate(): void {
        if (!this.model.provider) {
            this.#notify.alertWarning(this.#translate.instant('einv_config.msg.select_provider'));
            return;
        }

        const request = this.buildValidateRequest();
        this.isLoading = true;

        this.#service.validateConfig(request).subscribe({
            next: (res) => {
                this.isLoading = false;
                if (res.data?.success == true) {
                    this.#notify.alertSuccess(this.#translate.instant('einv_config.msg.validate_success'));
                    this.updateStatus(IntegrationStatus.Integrated);
                } else {
                    const errorKey = res.data?.message || 'einv_config.msg.validate_error';
                    this.#notify.alertError(this.#translate.instant(errorKey));
                }
            },
            error: () => {
                this.isLoading = false;
                this.#notify.alertError(this.#translate.instant('einv_config.msg.validate_error'));
            }
        });
    }

    onCancelIntegration(): void {
        this.#notify.showConfirm(
            this.#translate.instant('einv_config.confirm.cancel_message'),
            {
                header: this.#translate.instant('einv_config.confirm.cancel_title'),
                acceptCallback: () => this.executeCancelIntegration()
            }
        ).subscribe();
    }

    private executeCancelIntegration(): void {
        this.isLoading = true;
        this.#service.deactivate().subscribe({
            next: (res) => {
                this.isLoading = false;
                if (res.data?.success) {
                    const successKey = res.data.message || 'einv_config.msg.cancelled';
                    this.#notify.alertSuccess(this.#translate.instant(successKey));
                    this.updateStatus(IntegrationStatus.Cancelled);
                } else {
                    const errorKey = res.data?.message || 'einv_config.msg.cancel_error';
                    this.#notify.alertError(this.#translate.instant(errorKey));
                }
            },
            error: (err) => {
                this.isLoading = false;
                const errorMsg = err?.error?.description || err?.error?.message || this.#translate.instant('einv_config.msg.cancel_error');
                this.#notify.alertError(errorMsg);
            }
        });
    }
    // #endregion

    // #region Private Methods
    private loadProviders(): void {
        this.#service.getProviders().subscribe(providers => {
            this.providers = providers.map(p => ({ label: p.providerName, value: p.id }));
            this.formFields = this.buildFormFields();
        });
    }

    private loadConfig(): void {
        this.isLoading = true;
        this.#service.getConfig().subscribe({
            next: (config) => {
                this.isLoading = false;
                if (config) {
                    this.model = this.mapDtoToModel(config);
                    this.originalModel = { ...this.model };
                    this.updateStatus(config.status);
                    this.hasExistingConfig = true;
                    this.setEditMode(false);
                } else {
                    this.updateStatus(null);
                    this.hasExistingConfig = false;
                    this.setEditMode(true);
                }
            },
            error: () => {
                this.isLoading = false;
                this.updateStatus(null);
                this.hasExistingConfig = false;
                this.setEditMode(true);
            }
        });
    }

    private setEditMode(enabled: boolean): void {
        this.isEditMode = enabled;
        this.refreshFormFields();
    }

    private refreshFormFields(): void {
        this.formFields = [...this.formFields];
        this.#cdr.detectChanges();
    }

    private updateStatus(status: number | null | undefined): void {
        this.currentStatus = status ?? null;
        switch (status) {
            case IntegrationStatus.Integrated:
                this.statusText = this.#translate.instant('einv_config.status.integrated');
                this.statusClass = 'text-green-600 font-semibold';
                break;
            case IntegrationStatus.Cancelled:
                this.statusText = this.#translate.instant('einv_config.status.cancelled');
                this.statusClass = 'text-red-500 font-semibold';
                break;
            default:
                this.statusText = this.#translate.instant('einv_config.status.not_integrated');
                this.statusClass = 'text-gray-500';
        }
    }

    private mapDtoToModel(dto: any): EinvoiceConfigModel {
        this.realCredentials = {
            bkavPartnerToken: dto.partnerToken || '',
            mobifonePassword: dto.partnerPwd || ''
        };

        return {
            provider: dto.providerId || 'BKAV',
            bkavPartnerGuid: dto.partnerId || '',
            bkavPartnerToken: this.maskToken(dto.partnerToken),
            mobifoneUsername: dto.partnerUsr || '',
            mobifonePassword: dto.partnerPwd || '',
            mobifoneTaxCode: dto.taxCode || ''
        };
    }

    private maskToken(token: string | null | undefined): string {
        if (!token || token.length <= 8) return token || '';
        return token.substring(0, 8) + '********';
    }

    private buildSaveRequest(): EinvStoreProviderRequest {
        const { provider, bkavPartnerGuid, bkavPartnerToken,
            mobifoneUsername, mobifonePassword, mobifoneTaxCode } = this.model;

        const request: EinvStoreProviderRequest = { providerId: provider };

        if (provider === 'BKAV') {
            const finalToken = bkavPartnerToken.endsWith('********')
                ? this.realCredentials.bkavPartnerToken
                : bkavPartnerToken;
            Object.assign(request, { bkavPartnerGuid, bkavPartnerToken: finalToken });
        } else if (provider === 'MOBI') {
            Object.assign(request, { mobifoneUsername, mobifonePassword, mobifoneTaxCode });
        }

        return request;
    }

    private buildValidateRequest(): EinvStoreProviderRequest {
        const { provider, mobifoneUsername, mobifonePassword, mobifoneTaxCode } = this.model;
        return { providerId: provider, mobifoneUsername, mobifonePassword, mobifoneTaxCode };
    }

    private buildFormFields(): FormlyFieldConfig[] {
        const disabledExpression = () => !this.isEditMode;

        return [{
            fieldGroupClassName: 'grid grid-cols-1 gap-3',
            fieldGroup: [
                // Provider Selection
                {
                    key: 'provider',
                    type: FormlyFieldType.Select,
                    props: {
                        label: this.#translate.instant('einv_config.field.provider'),
                        placeholder: this.#translate.instant('einv_config.field.provider_placeholder'),
                        required: true,
                        options: this.providers,
                        className: 'w-full'
                    },
                    expressions: { 'props.disabled': disabledExpression }
                },
                // BKAV Fields
                {
                    key: 'bkavPartnerGuid',
                    type: FormlyFieldType.Input,
                    props: {
                        label: this.#translate.instant('einv_config.field.partner_guid'),
                        placeholder: 'Partner GUID',
                        required: true,
                        className: 'w-full'
                    },
                    expressions: {
                        hide: 'model.provider !== "BKAV"',
                        'props.disabled': disabledExpression
                    }
                },
                {
                    key: 'bkavPartnerToken',
                    type: FormlyFieldType.MaskedToken,
                    props: {
                        label: this.#translate.instant('einv_config.field.partner_token'),
                        placeholder: 'Partner Token',
                        required: true,
                        className: 'w-full'
                    },
                    expressions: {
                        hide: 'model.provider !== "BKAV"',
                        'props.disabled': disabledExpression
                    }
                },
                // MobiFone Fields
                {
                    key: 'mobifoneUsername',
                    type: FormlyFieldType.Input,
                    props: {
                        label: this.#translate.instant('einv_config.field.username'),
                        placeholder: 'Username',
                        required: true,
                        className: 'w-full'
                    },
                    expressions: {
                        hide: 'model.provider !== "MOBI"',
                        'props.disabled': disabledExpression
                    }
                },
                {
                    key: 'mobifonePassword',
                    type: FormlyFieldType.Password,
                    props: {
                        label: this.#translate.instant('einv_config.field.password'),
                        placeholder: 'Password',
                        required: true,
                        toggleMask: true,
                        className: 'w-full'
                    },
                    expressions: {
                        hide: 'model.provider !== "MOBI"',
                        'props.disabled': disabledExpression
                    }
                },
                {
                    key: 'mobifoneTaxCode',
                    type: FormlyFieldType.TaxCode,
                    props: {
                        label: this.#translate.instant('einv_config.field.tax_code'),
                        placeholder: this.#translate.instant('einv_config.field.tax_code'),
                        required: true,
                        className: 'w-full'
                    },
                    validators: {
                        taxCodeLength: {
                            expression: (c: any) => {
                                const val = c.value?.replace(/\D/g, '');
                                return !val || val.length === 10 || val.length === 13;
                            },
                            message: this.#translate.instant('einv_config.field.tax_code_error')
                        }
                    },
                    expressions: {
                        hide: 'model.provider !== "MOBI"',
                        'props.disabled': disabledExpression
                    }
                }
            ]
        }];
    }
    // #endregion
}
