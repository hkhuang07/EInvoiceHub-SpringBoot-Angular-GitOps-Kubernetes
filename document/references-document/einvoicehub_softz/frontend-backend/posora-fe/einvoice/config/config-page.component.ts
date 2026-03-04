import { Component, ViewChild } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { TabsModule } from 'primeng/tabs';
import EinvoiceConfigComponent from './provider/einvoice-config.component';
import { EinvoiceTemplateComponent } from './serial/einvoice-template-serial.component';

@Component({
    selector: 'app-einvoice-page',
    standalone: true,
    imports: [
        TranslateModule,
        TabsModule,
        EinvoiceConfigComponent,
        EinvoiceTemplateComponent
    ],
    template: `
    <div class="einvoice-page">
        <p-tabs [value]="0" (valueChange)="onTabChange($event)">
            <p-tablist>
                <p-tab [value]="0">
                    <i class="pi pi-cog mr-2"></i>
                    {{ 'einv_config.tab.config' | translate }}
                </p-tab>
                <p-tab [value]="1">
                    <i class="pi pi-file mr-2"></i>
                    {{ 'einv_config.tab.serial' | translate }}
                </p-tab>
            </p-tablist>
            <p-tabpanels>
                <p-tabpanel [value]="0">
                    <app-einvoice-config></app-einvoice-config>
                </p-tabpanel>
                <p-tabpanel [value]="1">
                    <app-einvoice-template #templateComponent></app-einvoice-template>
                </p-tabpanel>
            </p-tabpanels>
        </p-tabs>
    </div>
    `,
    styles: [`
        .einvoice-page {
            padding: 1rem;
        }
        :host ::ng-deep .p-tabs {
            background: transparent;
        }
        :host ::ng-deep .p-tablist {
            background: #f8fafc;
            border-radius: 8px 8px 0 0;
            border-bottom: 1px solid #e2e8f0;
        }
        :host ::ng-deep .p-tab {
            padding: 1rem 1.5rem;
            font-weight: 500;
        }
        :host ::ng-deep .p-tab.p-tab-active {
            color: #2563eb;
            border-bottom: 2px solid #2563eb;
        }
        :host ::ng-deep .p-tabpanels {
            background: white;
            border-radius: 0 0 8px 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
    `]
})
export default class EinvoicePageComponent {
    @ViewChild('templateComponent') templateComponent!: EinvoiceTemplateComponent;

    onTabChange(tabIndex: any): void {
        if (tabIndex === 1 && this.templateComponent) {
            this.templateComponent.loadIntegratedProvider();
        }
    }
}
