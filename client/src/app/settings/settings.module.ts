import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SettingsComponent } from './settings.component';
import { SettingsRoutingModule } from './settings-routing.module';
import { PreferencesComponent } from './preferences.component';
import { EditAccountComponent } from './edit-account.component';
import { ChangePasswordComponent } from './change-password.component';

@NgModule({
  imports: [
    SettingsRoutingModule,
    FormsModule,
    CommonModule
  ],
  declarations: [
    SettingsComponent,
    PreferencesComponent,
    EditAccountComponent,
    ChangePasswordComponent
  ]
})
export class SettingsModule {}
