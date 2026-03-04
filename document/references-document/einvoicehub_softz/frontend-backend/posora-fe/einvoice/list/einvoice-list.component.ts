import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Select } from 'primeng/select';
import { _, TranslateModule } from '@ngx-translate/core';
import { 
    BaseIndexComponent, 
    TableWrapperComponent, 
    EndpointType 
} from '@shared';
import { EinvoiceInvoiceDto, InvoiceStatus } from './einvoice-list.model';
import { EinvoiceInvoiceService } from './einvoice-list.service';

@Component({
    selector: 'app-einvoice-invoice-list',
    standalone: true,
    imports: [TableWrapperComponent, Select, FormsModule, TranslateModule],
    template: `
        <app-table-wrapper
            [headerTitle]="indexMeta.indexTitle"
            [titleIcon]="indexMeta.indexIcon"
            [headerBtnLabel]="indexMeta.createBtnLabel"
            [columns]="indexMeta.columns"
            [dataSource]="records()"
            [loading]="isLoading()"
            [displayCreateButton]="indexMeta.displayCreateButton"
            [displayUpdateButton]="indexMeta.displayUpdateButton"
            [displayDeleteButton]="indexMeta.displayDeleteButton"
            [displayViewButton]="indexMeta.displayViewButton"
            [showCurrentPageReport]="getDisplayCurrentPageReport()"
            [totalRecords]="totalRecords()"
            [recordsFiltered]="recordsFiltered()"
            paginatorPosition="bottom"
            [indexRole]="roles()['index']"
            [updateBtnRole]="roles()['update']"
            [createBtnRole]="roles()['create']"
            [showViewBtnRole]="roles()['show']"
            [deleteBtnRole]="roles()['delete']"
            [displayFilterBtn]="indexMeta.displayFilterButton"
            [(displayFilters)]="displayFilters"
            [stateKey]="undefined"
            [globalFilterValue]="globalFilterValue()"
            [globalFilterFields]="getGlobalFilterFields()"
            [reorderableColumns]="indexMeta.reorderableColumns ?? false"
            [reorderableRows]="indexMeta.reorderableRows ?? false"
            [toolbarStartTemplate]="toolbarStartTpl"
            (deleteBtnClicked)="confirmDelete($event)"
            (deleteListBtnClicked)="confirmDeleteList($event)"
            (onLoadData)="loadRecords($event)"
            [withMultiLayout]="withMultiLayout()"
            [withAdditionalContent]="false"
            [showGridlines]="false"
            [showHeaderGridlines]="true"
            [displayExportButton]="false"
            [displayDeleteListButton]="false"
        >
        </app-table-wrapper>

        <ng-template #toolbarStartTpl>
            <p-select 
                [options]="statusOptions()" 
                [(ngModel)]="selectedStatusId"
                optionLabel="name"
                optionValue="id"
                [filter]="true"
                filterBy="name"
                [placeholder]="'einv_list.select_status' | translate"
                [showClear]="true"
                (onChange)="onStatusChange()"
                [style]="{ width: '200px' }"
            ></p-select>
        </ng-template>
    `,
})
export class EinvoiceInvoiceListComponent extends BaseIndexComponent<EinvoiceInvoiceDto> implements OnInit {
    readonly #invoiceService = inject(EinvoiceInvoiceService);
    
    // Status filter
    statusOptions = signal<InvoiceStatus[]>([]);
    selectedStatusId: number | null = null;

    ngOnInit(): void {
        this.indexMeta = {
            ...this.indexMeta,
            endpoints: {
                [EndpointType.index]: 'einvoice/hub/list',
                [EndpointType.deleteList]: 'einvoice/hub/delete',
            },
            indexTableKey: 'einvoice_list',
            indexTitle: 'einv_list.title',
            functionCode: 'einvoice_list',
            displayFilterButton: false,
            displayCreateButton: false,
            displayUpdateButton: true,
            displayDeleteButton: false,
            displayViewButton: true,
            columns: [
                {
                    header: _('einv_list.form'),
                    field: 'invoiceForm',
                    searchable: true,
                    orderable: true,
                    minWidth: '8rem',
                    cellClass: 'text-right!'
                },
                {
                    header: _('einv_list.serial'),
                    field: 'invoiceSeries',
                    searchable: true,
                    orderable: true,
                    minWidth: '8rem',
                },
                {
                    header: _('einv_list.invoice_no'),
                    field: 'invoiceNo',
                    orderable: true,
                    minWidth: '8rem',
                    searchable: true,
                    cellClass: 'text-right!'
                },
                {
                    header: _('einv_list.invoice_date'),
                    field: 'invoiceDate',
                    dataType: 'date',
                    orderable: true,
                    minWidth: '8rem',
                },
                {
                    header: _('einv_list.buyer_name'),
                    field: 'buyerFullName',
                    orderable: true,
                    minWidth: '12rem',
                    searchable: true,
                },
                {
                    header: _('einv_list.buyer_tax_code'),
                    field: 'buyerTaxCode',
                    searchable: true,
                    orderable: false,
                    minWidth: '10rem',
                    cellClass: 'text-right!'
                },
                {
                    header: _('einv_list.payment_method'),
                    field: 'paymentMethodName',
                    orderable: false,
                    minWidth: '6rem',
                },
                {
                    header: _('einv_list.total_amount'),
                    field: 'totalAmount',
                    orderable: true,
                    minWidth: '10rem',
                    dataType: 'currency',
                    cellClass: 'text-right!'
                },
                {
                    header: _('einv_list.tax_amount'),
                    field: 'taxAmount',
                    orderable: false,
                    minWidth: '8rem',
                    dataType: 'currency',
                    cellClass: 'text-right!'
                },
                {
                    header: _('einv_list.cqt_code'),
                    field: 'taxAuthorityCode',
                    orderable: false,
                    minWidth: '10rem',
                },
                {
                    header: _('einv_list.status'),
                    field: 'statusName',
                    orderable: false,
                    minWidth: '8rem',
                },
                {
                    header: _('einv_list.created_by'),
                    field: 'createdByFullName',
                    orderable: false,
                    minWidth: '10rem',
                },
            ],
        };
        // this.initRolesUser();
        this.loadInvoiceStatus();
        this.filtersData.set({ start: 0 } as any);
    }

    private loadInvoiceStatus(): void {
        this.#invoiceService.loadInvoiceStatus().subscribe({
            next: (res) => {
                if (res.responseCode === '200' && res.data) {
                    this.statusOptions.set(res.data);
                }
            }
        });
    }

    onStatusChange(): void {
        const currentFilters = this.filtersData() || {};
        this.filtersData.set({
            ...currentFilters,
            customFilters: {
                ...(currentFilters as any)?.customFilters,
                statusId: this.selectedStatusId,
            },
            start: 0,
        } as any);
    }
}
