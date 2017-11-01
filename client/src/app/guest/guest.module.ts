import { NgModule } from '@angular/core';

import { RecaptchaModule } from 'ng-recaptcha';

import { GuestRoutingModule } from './guest-routing.module';

import { LoginComponent } from './login.component';
import { RegisterComponent } from './register.component';
import { ResetPasswordComponent } from './reset-password.component';

@NgModule({
    declarations: [
        LoginComponent,
        RegisterComponent,
        ResetPasswordComponent
    ],
    imports: [
        GuestRoutingModule,
        RecaptchaModule
    ],
})
export class GuestModule {}