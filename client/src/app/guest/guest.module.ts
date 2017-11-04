import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

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
        FormsModule
    ],
})
export class GuestModule {}
