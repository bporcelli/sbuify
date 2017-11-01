import { NgModule } from '@angular/core';

import { SettingsComponent } from './settings.component';
import { SettingsRoutingModule } from './settings-routing.module';

import { PreferencesComponent } from './preferences.component';
import { EditAccountComponent } from './edit-account.component';
import { ChangePasswordComponent } from './change-password.component';

@NgModule({
    imports: [
        SettingsRoutingModule
    ],
    declarations: [
        SettingsComponent,
        PreferencesComponent,
        EditAccountComponent,
        ChangePasswordComponent
    ]
})
export class SettingsModule {}