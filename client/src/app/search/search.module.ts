import { NgModule } from '@angular/core';

import { SearchResultsComponent } from './search-results.component';
import { SearchRoutingModule } from './search-routing.module';

import { SongResultsComponent } from './song-results.component';
import { AlbumResultsComponent } from './album-results.component';
import { ArtistResultsComponent } from './artist-results.component';
import { PlaylistResultsComponent } from './playlist-results.component';
import { ProfileResultsComponent } from './profile-results.component';

@NgModule({
    imports: [
        SearchRoutingModule
    ],
    declarations: [
        SearchResultsComponent,
        SongResultsComponent,
        AlbumResultsComponent,
        ArtistResultsComponent,
        PlaylistResultsComponent,
        ProfileResultsComponent
    ]
})
export class SearchModule {}
