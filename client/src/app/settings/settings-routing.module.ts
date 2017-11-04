import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SettingsComponent } from './settings.component';
import { PreferencesComponent } from './preferences.component';
import { EditAccountComponent } from './edit-account.component';
import { ChangePasswordComponent } from './change-password.component';

import { AuthGuard } from '../auth/auth-guard.service';

const settingsRoutes: Routes = [
    {
        path: 'settings',
        component: SettingsComponent,
        canActivate: [ AuthGuard ],
        canActivateChild: [ AuthGuard ],
        children: [
            { path: 'preferences', component: PreferencesComponent },
            { path: 'account', component: EditAccountComponent },
            { path: 'password', component: ChangePasswordComponent },
            { path: '', redirectTo: 'preferences', pathMatch: 'full' }
        ]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(settingsRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class SettingsRoutingModule {}
