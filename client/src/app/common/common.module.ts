import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule as ACommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FormatDurationPipe } from "./format-duration.pipe";

@NgModule({
  declarations: [
    FormatDurationPipe
  ],
  imports: [
    BrowserModule,
    ACommonModule,
    FormsModule
  ],
  exports: [
    FormatDurationPipe
  ]
})
export class CommonModule {}
