import { Routes } from '@angular/router';

export default [
    { 
        path: '', 
        redirectTo: 'config', 
        pathMatch: 'full' 
    },
    { 
        path: 'config', 
        loadComponent: () => import('./config/config-page.component'),
        data: {
            breadcrumbs: [
                { label: 'einv_config.title', url: '' }
            ]
        }
    },
    { 
        path: 'list', 
        loadComponent: () => import('./list/einvoice-list.component')
            .then(m => m.EinvoiceInvoiceListComponent),
        data: {
            breadcrumbs: [
                { label: 'einv_list.title', url: '' }
            ]
        }
    }
] as Routes;

