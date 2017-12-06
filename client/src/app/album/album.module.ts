import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { TruncateModule } from 'ng2-truncate';
import { ContextMenuModule } from 'ngx-contextmenu';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CommonModule as ACommonModule } from "../common/common.module";
import { AlbumListComponent } from "./album-list.component";
import { AlbumService } from "./album.service";

@NgModule({
  imports: [
    CommonModule,
    TruncateModule,
    NgbModule,
    ContextMenuModule,
    ACommonModule,
    InfiniteScrollModule
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
