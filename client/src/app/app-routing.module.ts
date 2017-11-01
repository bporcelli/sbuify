import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const appRoutes: Routes = [
    { path: '', redirectTo: 'browse', pathMatch: 'full' },
];

@NgModule({
    imports: [
        RouterModule.forRoot(appRoutes, { enableTracing: true })  // TODO: disable tracing
    ],
    exports: [
        RouterModule
    ]
})
export class AppRoutingModule {}