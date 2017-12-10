import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TruncateModule } from 'ng2-truncate';
import { ContextMenuModule } from 'ngx-contextmenu';
import { ArtistDetailComponent } from './artist-detail.component';
import { ArtistRoutingModule } from './artist-routing.module';
import { OverviewComponent } from './overview.component';
import { RelatedArtistsComponent } from './related-artists.component';
import { AboutComponent } from './about.component';
import { ArtistGridComponent } from "./artist-grid.component";
import { SharedModule } from "../shared/shared.module";
import { ProductModalComponent } from "./product-modal.component";
import { AlbumModule } from "../album/album.module";
import { ArtistDetailResolver } from "./artist-resolver.service";
import { ArtistService } from "./artist.service";
import { ConcertsModule } from "../concerts/concerts.module";
import {ArtistConcertsComponent} from "./artist-concerts.component";

@NgModule({
  imports: [
    ArtistRoutingModule,
    CommonModule,
    FormsModule,
    NgbModule,
    ContextMenuModule,
    SharedModule,
    TruncateModule,
    AlbumModule,
    ConcertsModule
  ],
  declarations: [
    ArtistDetailComponent,
    OverviewComponent,
    RelatedArtistsComponent,
    AboutComponent,
    ArtistGridComponent,
    ProductModalComponent,
    ArtistConcertsComponent
  ],
  providers: [
    ArtistDetailResolver,
    ArtistService
  ],
  entryComponents: [
    ProductModalComponent
  ],
  exports: [
    ArtistGridComponent
  ]
})
export class ArtistModule {}
