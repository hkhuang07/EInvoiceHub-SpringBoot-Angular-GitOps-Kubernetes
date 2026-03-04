import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';
import { EinvStoreSerialService, EinvStoreSerialDto, EinvStoreSerialRequest } from './einvoice-template-serial.service';
import { EinvoiceConfigService, EinvStoreProviderDto } from '../provider/einvoice-config.service';
import { NotifyService } from '@shared';

interface InvoiceTypeOption {
    label: string;
    value: number;
}

// Internal form data type with Date for datepicker
interface FormDataModel {
    id?: string;
    providerId?: string;
    invoiceTypeId?: number;
    invoiceForm?: string;
    invoiceSerial?: string;
    startDate?: Date;
    status?: number;
}

// Fixed status options - không load từ API
const STATUS_OPTIONS = [
    { label: 'Chờ duyệt', value: 0 },
    { label: 'Đã duyệt', value: 1 },
    { label: 'Ngưng sử dụng', value: 8 }
];

// Provider names mapping
const PROVIDER_NAMES: Record<string, string> = {
    'BKAV': 'BKAV',
    'MOBI': 'MobiFone'
};

@Component({
    selector: 'app-einvoice-template',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        TranslateModule,
        TableModule,
        ButtonModule,
        TagModule,
        TooltipModule,
        DialogModule,
        InputTextModule,
        SelectModule,
        DatePickerModule
    ],
    template: `
    <div class="template-container">
        <div class="flex justify-between items-center mb-4">
            <div class="flex items-center gap-2">
                <p-select 
                    [options]="filterStatusOptions" 
                    [(ngModel)]="selectedStatusFilter"
                    optionLabel="label"
                    optionValue="value"
                    styleClass="w-40">
                </p-select>
            </div>
            <button pButton type="button" 
                [label]="'einv_serial.btn_add' | translate" 
                icon="pi pi-plus" 
                class="p-button-primary"
                [class.btn-locked]="!isIntegrated"
                (click)="onAdd()">
            </button>
        </div>

        <p-table [value]="filteredTemplates" 
            [paginator]="true" 
            [rows]="10"
            [loading]="isLoading"
            styleClass="p-datatable-sm p-datatable-striped"
            [rowHover]="true">
            
            <ng-template pTemplate="header">
                <tr>
                    <th style="width: 60px">{{ 'einv_serial.col.stt' | translate }}</th>
                    <th style="min-width: 180px">{{ 'einv_serial.col.invoice_type' | translate }}</th>
                    <th>{{ 'einv_serial.col.form' | translate }}</th>
                    <th>{{ 'einv_serial.col.serial' | translate }}</th>
                    <th style="width: 140px">{{ 'einv_serial.col.start_date' | translate }}</th>
                    <th style="width: 180px">{{ 'einv_serial.col.updated_date' | translate }}</th>
                    <th style="width: 100px">{{ 'einv_serial.col.status' | translate }}</th>
                    <th style="width: 100px">{{ 'einv_serial.col.action' | translate }}</th>
                </tr>
            </ng-template>

            <ng-template pTemplate="body" let-item let-i="rowIndex">
                <tr>
                    <td class="text-center">{{ i + 1 }}</td>
                    <td class="text-center">{{ getInvoiceTypeName(item.invoiceTypeId) }}</td>
                    <td class="text-center">{{ item.invoiceForm }}</td>
                    <td class="text-center font-semibold text-primary">{{ item.invoiceSerial }}</td>
                    <td class="text-center">{{ item.startDate | date:'dd/MM/yyyy' }}</td>
                    <td class="text-center">{{ item.updatedDate | date:'dd/MM/yyyy HH:mm' }}</td>
                    <td class="text-center">
                        <p-tag [value]="getStatusLabel(item.status)" 
                            [severity]="getStatusSeverity(item.status)">
                        </p-tag>
                    </td>
                    <td class="text-center">
                        <div class="flex justify-center gap-1">
                            @if (item.status === 0) {
                                <button pButton type="button" 
                                    icon="pi pi-check" 
                                    class="p-button-rounded p-button-text p-button-success p-button-sm"
                                    [pTooltip]="'einv_serial.tooltip.approve' | translate"
                                    [class.btn-locked]="!isIntegrated"
                                    (click)="onApprove(item)">
                                </button>
                            }
                            @if (item.status === 1) {
                                <button pButton type="button" 
                                    icon="pi pi-ban" 
                                    class="p-button-rounded p-button-text p-button-warning p-button-sm"
                                    [pTooltip]="'einv_serial.tooltip.deactivate' | translate"
                                    [class.btn-locked]="!isIntegrated"
                                    (click)="onDeactivateSerialFromTable(item)">
                                </button>
                            }
                            @if (item.status === 0) {
                                <button pButton type="button" 
                                    icon="pi pi-pencil" 
                                    class="p-button-rounded p-button-text p-button-sm"
                                    [pTooltip]="'einv_serial.tooltip.edit' | translate"
                                    [class.btn-locked]="!isIntegrated"
                                    (click)="onEdit(item)">
                                </button>
                            }
                            @if (item.status !== 1) {
                                <button pButton type="button" 
                                icon="pi pi-trash" 
                                class="p-button-rounded p-button-text p-button-danger p-button-sm"
                                [pTooltip]="'einv_serial.tooltip.delete' | translate"
                                [class.btn-locked]="!isIntegrated"
                                (click)="onDelete(item)">
                            </button>
                            }
                        </div>
                    </td>
                </tr>
            </ng-template>

            <ng-template pTemplate="emptymessage">
                <tr>
                    <td colspan="8" class="text-center py-8 text-gray-500">
                        {{ 'einv_serial.empty' | translate }}
                    </td>
                </tr>
            </ng-template>
        </p-table>
    </div>

    <!-- Add/Edit Dialog -->
    <p-dialog 
        [(visible)]="showDialog" 
        [header]="isEditMode ? ('einv_serial.dialog.title_edit' | translate) : ('einv_serial.dialog.title_add' | translate)"
        [modal]="true"
        [style]="{width: '500px', borderRadius: '8px'}"
        [closable]="true">
        
        <div class="flex flex-col gap-4 pt-2">
            <!-- Provider Info (readonly) -->
            <div class="flex flex-col gap-2">
                <label class="font-semibold">{{ 'einv_serial.dialog.provider' | translate }} <span class="text-red-500">*</span></label>
                @if (integratedProvider) {
                    <div class="p-3 bg-gray-100 rounded-lg border border-gray-200">
                        <span class="font-medium">{{ getProviderName(integratedProvider.providerId) }}</span>
                    </div>
                } @else {
                    <div class="p-3 bg-red-50 rounded-lg border border-red-300">
                        <span class="text-red-600 font-medium">
                            {{ 'einv_serial.dialog.no_provider' | translate }}
                        </span>
                    </div>
                }
            </div>

            <div class="flex gap-4">
                <!-- Invoice Type -->
                <div class="flex flex-col gap-2 flex-1">
                    <label class="font-semibold">{{ 'einv_serial.dialog.invoice_type' | translate }} <span class="text-red-500">*</span></label>
                    <p-select 
                        [options]="invoiceTypeOptions" 
                        [(ngModel)]="formData.invoiceTypeId"
                        optionLabel="label"
                        optionValue="value"
                        [placeholder]="'einv_serial.dialog.invoice_type_placeholder' | translate"
                        appendTo="body"
                        styleClass="w-full"
                        [disabled]="isFormDisabled"
                        (onChange)="onInvoiceTypeChange($event)">
                    </p-select>
                </div>

                <div class="flex flex-col gap-2 w-3/12">
                    <label class="font-semibold">{{ 'einv_serial.col.form' | translate }}</label>
                    <input pInputText 
                        [(ngModel)]="formData.invoiceForm" 
                        [readonly]="true"
                        placeholder="-"
                        class="w-full bg-gray-100" />
                </div>
            </div>

            <!-- Invoice Serial with hint -->
            <div class="flex flex-col gap-2">
                <label class="font-semibold">{{ 'einv_serial.col.serial' | translate }} <span class="text-red-500">*</span></label>
                <input pInputText 
                    [(ngModel)]="formData.invoiceSerial" 
                    [placeholder]="invoiceSerialHint"
                    [readonly]="isFormDisabled"
                    maxlength="10"
                    class="w-full" />
            </div>

            <!-- Start Date -->
            <div class="flex flex-col gap-2">
                <label class="font-semibold">{{ 'einv_serial.col.start_date' | translate }} <span class="text-red-500">*</span></label>
                <p-datepicker
                    [(ngModel)]="formData.startDate"
                    dateFormat="dd/mm/yy"
                    [showIcon]="true"
                    appendTo="body"
                    [disabled]="isFormDisabled"
                    styleClass="w-full">
                </p-datepicker>
            </div>

            @if (isEditMode) {
                <div class="flex items-center gap-2 pt-2 border-t border-gray-200">
                    <span class="font-semibold">{{ 'einv_serial.dialog.status' | translate }}:</span>
                    <span [class]="statusDisplayClass">{{ getStatusLabel(formData.status!) }}</span>
                </div>
            }
        </div>

        <ng-template pTemplate="footer">
            <div class="flex justify-end gap-2">
                <button pButton type="button" 
                    [label]="'einv_serial.dialog.btn_close' | translate" 
                    icon="pi pi-times"
                    class="p-button-secondary p-button-outlined"
                    (click)="showDialog = false">
                </button>
                @if (!isFormDisabled) {
                    <button pButton type="button" 
                        [label]="'einv_serial.dialog.btn_save' | translate" 
                        icon="pi pi-check"
                        class="p-button-primary"
                        [loading]="isSaving"
                        (click)="onSave()">
                    </button>
                }
            </div>
        </ng-template>
    </p-dialog>
    `,
    styles: [`
        .template-container {
            padding: 1rem;
        }
        :host ::ng-deep .p-datatable .p-datatable-thead > tr > th {
            background: var(--surface-100);
            color: var(--text-color);
            font-weight: 600;
            text-align: center;
        }
        :host ::ng-deep .p-datatable .p-datatable-tbody > tr > td {
            text-align: center;
        }
        :host ::ng-deep .p-datatable .p-datatable-tbody > tr:hover {
            background: var(--surface-50);
        }
        .text-primary {
            color: #2563eb;
        }
        :host ::ng-deep .bg-gray-100 {
            background-color: #f3f4f6;
        }
        .btn-locked {
            pointer-events: none;
            opacity: 0.6;
            cursor: not-allowed;
        }
    `]
})
export class EinvoiceTemplateComponent implements OnInit {
    readonly #serialService = inject(EinvStoreSerialService);
    readonly #configService = inject(EinvoiceConfigService);
    readonly #notify = inject(NotifyService);
    readonly #translate = inject(TranslateService);

    // State
    templates: EinvStoreSerialDto[] = [];
    isLoading = false;
    isSaving = false;
    showDialog = false;
    isEditMode = false;
    integratedProvider: EinvStoreProviderDto | null = null;
    selectedStatusFilter: number | null = null;

    // Options
    invoiceTypeOptions: InvoiceTypeOption[] = [];
    filterStatusOptions = [{ label: 'Tất cả', value: null }, ...STATUS_OPTIONS];

    // Form data
    formData: FormDataModel = {
        providerId: '',
        invoiceForm: '',
        invoiceSerial: '',
        status: 0,
        startDate: new Date()
    };

    // Computed properties
    get isIntegrated(): boolean {
        return this.integratedProvider?.status === 1;
    }

    get isFormDisabled(): boolean {
        return this.formData.status === 1 || this.formData.status === 8;
    }

    get filteredTemplates(): EinvStoreSerialDto[] {
        return this.selectedStatusFilter === null
            ? this.templates
            : this.templates.filter(t => t.status === this.selectedStatusFilter);
    }

    get invoiceSerialHint(): string {
        return `C${new Date().getFullYear().toString().slice(-2)}MAA`;
    }

    get statusDisplayClass(): string {
        switch (this.formData.status) {
            case 0: return 'text-yellow-600 font-medium';
            case 1: return 'text-green-600 font-medium';
            case 8: return 'text-gray-500 font-medium';
            default: return 'font-medium';
        }
    }

    ngOnInit(): void {
        this.loadIntegratedProvider();
        this.loadInvoiceTypes();
        this.loadData();
    }

    loadIntegratedProvider(): void {
        this.#configService.getConfig().subscribe(config => {
            this.integratedProvider = config ?? null;
        });
    }

    loadInvoiceTypes(): void {
        this.#serialService.getInvoiceTypes().subscribe(data => {
            this.invoiceTypeOptions = data.map(t => ({ label: t.name, value: t.id }));
        });
    }

    loadData(): void {
        this.isLoading = true;
        this.#serialService.getList().subscribe({
            next: (data) => {
                this.templates = data;
                this.isLoading = false;
            },
            error: () => {
                this.isLoading = false;
            }
        });
    }

    getProviderName(providerId: string | undefined): string {
        if (!providerId) return 'Chưa xác định';
        return PROVIDER_NAMES[providerId] || providerId;
    }

    getInvoiceTypeName(typeId: number): string {
        const found = this.invoiceTypeOptions.find(o => o.value === typeId);
        return found?.label || 'Không xác định';
    }

    getStatusLabel(status: number): string {
        switch (status) {
            case 0: return this.#translate.instant('einv_serial.status.pending');
            case 1: return this.#translate.instant('einv_serial.status.approved');
            case 8: return this.#translate.instant('einv_serial.status.inactive');
            default: return '-';
        }
    }

    getStatusSeverity(status: number): 'success' | 'info' | 'warn' | 'danger' | 'secondary' {
        switch (status) {
            case 1: return 'success';
            case 0: return 'warn';
            case 8: return 'danger';
            default: return 'secondary';
        }
    }

    // Auto-fill invoiceForm when invoice type changes
    onInvoiceTypeChange(event: any): void {
        if (event.value) {
            this.formData.invoiceForm = String(event.value);
        }
    }

    onAdd(): void {
        if (!this.isIntegrated) {
            this.#notify.alertWarning(this.#translate.instant('einv.error.provider_not_integrated'));
            return;
        }
        this.isEditMode = false;
        this.formData = {
            providerId: this.integratedProvider?.providerId || '',
            invoiceForm: '',
            invoiceSerial: '',
            status: 0,
            startDate: new Date()
        };
        this.showDialog = true;
    }

    onEdit(item: EinvStoreSerialDto): void {
        this.isEditMode = true;
        this.formData = {
            id: item.id,
            providerId: item.providerId,
            invoiceTypeId: item.invoiceTypeId,
            invoiceForm: item.invoiceForm,
            invoiceSerial: item.invoiceSerial,
            status: item.status,
            startDate: item.startDate ? new Date(item.startDate) : new Date()
        };
        this.showDialog = true;
    }

    onSave(): void {
        // Validation
        if (!this.formData.providerId) {
            this.#notify.alertWarning(this.#translate.instant('einv_serial.msg.provider_required'));
            return;
        }
        if (!this.formData.invoiceTypeId) {
            this.#notify.alertWarning(this.#translate.instant('einv_serial.msg.type_required'));
            return;
        }
        if (!this.formData.invoiceSerial) {
            this.#notify.alertWarning(this.#translate.instant('einv_serial.msg.serial_required'));
            return;
        }
        if (!this.formData.startDate) {
            this.#notify.alertWarning(this.#translate.instant('einv_serial.msg.date_required'));
            return;
        }

        let startDateStr: string | undefined;
        if (this.formData.startDate) {
            const dateOnly = new Date(this.formData.startDate);
            dateOnly.setUTCHours(0, 0, 0, 0);
            startDateStr = dateOnly.toISOString();
        }

        const requestData: EinvStoreSerialRequest = {
            id: this.formData.id,
            providerId: this.formData.providerId,
            invoiceTypeId: this.formData.invoiceTypeId,
            invoiceForm: this.formData.invoiceForm,
            invoiceSerial: this.formData.invoiceSerial,
            status: this.formData.status,
            startDate: startDateStr
        };

        this.isSaving = true;
        this.#serialService.save(requestData).subscribe({
            next: (res) => {
                this.isSaving = false;
                if (res.responseCode === '200') {
                    this.#notify.alertSuccess(this.isEditMode
                        ? this.#translate.instant('einv_serial.msg.update_success')
                        : this.#translate.instant('einv_serial.msg.add_success'));
                    this.showDialog = false;
                    this.loadData();
                } else {
                    this.#notify.alertError(this.#translate.instant(res.message || 'einv.error.provider_not_integrated'));
                }
            },
            error: () => {
                this.isSaving = false;
                this.#notify.alertError(this.#translate.instant('einv_serial.msg.error'));
            }
        });
    }

    onDelete(item: EinvStoreSerialDto): void {
        this.#notify.showConfirm(
            `${this.#translate.instant('einv_serial.confirm.delete_message')} "${item.invoiceSerial}"?`,
            {
                header: this.#translate.instant('einv_serial.confirm.delete_title'),
                acceptCallback: () => {
                    this.#serialService.delete(item.id!).subscribe({
                        next: () => {
                            this.#notify.alertSuccess(this.#translate.instant('einv_serial.msg.delete_success'));
                            this.loadData();
                        }
                    });
                }
            }
        ).subscribe();
    }

    onApprove(item: EinvStoreSerialDto): void {
        this.#notify.showConfirm(
            `${this.#translate.instant('einv_serial.confirm.approve_message')} "${item.invoiceSerial}"?`,
            {
                header: this.#translate.instant('einv_serial.confirm.approve_title'),
                acceptCallback: () => {
                    this.isLoading = true;
                    this.#serialService.approve(item.id!).subscribe({
                        next: (res) => {
                            this.isLoading = false;
                            if (res.responseCode === '200') {
                                this.#notify.alertSuccess(`${this.#translate.instant('einv_serial.msg.approve_success')} "${item.invoiceSerial}"`);
                                this.loadData();
                            } else {
                                const errorMsg = (res as any).description || res.message || this.#translate.instant('einv_serial.msg.approve_failed');
                                this.#notify.alertError(errorMsg);
                            }
                        },
                        error: (err) => {
                            this.isLoading = false;
                            const errorKey = err?.error?.description || err?.error?.message || err?.message || 'einv_serial.msg.approve_error';
                            this.#notify.alertError(this.#translate.instant(errorKey) || errorKey);
                        }
                    });
                }
            }
        ).subscribe();
    }

    onDeactivateSerialFromTable(item: EinvStoreSerialDto): void {
        this.#notify.showConfirm(
            `${this.#translate.instant('einv_serial.confirm.deactivate_message')} "${item.invoiceSerial}"`,
            {
                header: this.#translate.instant('einv_serial.confirm.deactivate_title'),
                acceptCallback: () => {
                    this.isLoading = true;
                    this.#serialService.deactivate(item.id!).subscribe({
                        next: (res) => {
                            this.isLoading = false;
                            if (res.responseCode === '200') {
                                this.#notify.alertSuccess(`${this.#translate.instant('einv_serial.msg.deactivate_success')} "${item.invoiceSerial}"`);
                                this.loadData();
                            } else {
                                this.#notify.alertError(res.message || this.#translate.instant('einv_serial.msg.error'));
                            }
                        },
                        error: (err) => {
                            this.isLoading = false;
                            this.#notify.alertError(err?.error?.message || this.#translate.instant('einv_serial.msg.error'));
                        }
                    });
                }
            }
        ).subscribe();
    }
}
