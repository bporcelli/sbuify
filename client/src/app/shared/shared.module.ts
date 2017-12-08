import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FormatDurationPipe } from "./format-duration.pipe";
import { ToImageSrcPipe } from "./to-image-src.pipe";
import { FilterBoxComponent } from "./filter-box.component";

@NgModule({
  declarations: [
    FormatDurationPipe,
    ToImageSrcPipe,
    FilterBoxComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    FormsModule
  ],
  exports: [
    FormatDurationPipe,
    ToImageSrcPipe,
    FilterBoxComponent
  ]
})
export class SharedModule {}
