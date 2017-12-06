import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ContextMenuModule } from 'ngx-contextmenu';
import { ArtistDetailComponent } from './artist-detail.component';
import { ArtistRoutingModule } from './artist-routing.module';
import { OverviewComponent } from './overview.component';
import { RelatedArtistsComponent } from './related-artists.component';
import { AboutComponent } from './about.component';
import { ArtistGridComponent } from "./artist-grid.component";

@NgModule({
  imports: [
    ArtistRoutingModule,
    CommonModule,
    NgbModule,
    ContextMenuModule
  ],
  declarations: [
    ArtistDetailComponent,
    OverviewComponent,
    RelatedArtistsComponent,
    AboutComponent,
    ArtistGridComponent
  ],
  exports: [
    ArtistGridComponent
  ]
})
export class ArtistModule {}
