import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ConcertsComponent } from './concerts.component';
import { ConcertsTableComponent } from "./concerts-table.component";

@NgModule({
  imports: [
    CommonModule,
    RouterModule
  ],
  declarations: [
    ConcertsComponent,
    ConcertsTableComponent
  ],
  exports: [
    ConcertsTableComponent
  ]
})
export class ConcertsModule {}
