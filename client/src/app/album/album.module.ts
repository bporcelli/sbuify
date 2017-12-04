import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { TruncateModule } from 'ng2-truncate';
import { ContextMenuModule } from 'ngx-contextmenu';
import { AlbumListComponent } from "./album-list.component";
import { AlbumService } from "./album.service";

@NgModule({
  imports: [
    CommonModule,
    TruncateModule,
    NgbModule,
    ContextMenuModule
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
