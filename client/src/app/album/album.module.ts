import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlbumListComponent } from "./album-list.component";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    AlbumListComponent
  ],
  exports: [
    AlbumListComponent
  ]
})
export class AlbumModule {}
