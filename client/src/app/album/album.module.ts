import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TruncateModule } from 'ng2-truncate';
import { AlbumListComponent } from "./album-list.component";
import { AlbumService } from "./album.service";

@NgModule({
  imports: [
    CommonModule,
    TruncateModule
  ],
  declarations: [
    AlbumListComponent
  ],
  exports: [
    AlbumListComponent
  ],
  providers: [
    AlbumService
  ]
})
export class AlbumModule {}
