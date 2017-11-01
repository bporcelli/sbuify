import { NgModule } from '@angular/core';

import { RouterModule, Routes } from '@angular/router';

import { SearchResultsComponent } from './search-results.component';
import { SongResultsComponent } from './song-results.component';
import { AlbumResultsComponent } from './album-results.component';
import { ArtistResultsComponent } from './artist-results.component';
import { PlaylistResultsComponent } from './playlist-results.component';
import { ProfileResultsComponent } from './profile-results.component';

import { AuthGuard } from '../auth-guard.service';

const searchRoutes: Routes = [
    {
        path: 'search',
        component: SearchResultsComponent,
        canActivate: [ AuthGuard ],
        canActivateChild: [ AuthGuard ],
        children: [
            { path: 'songs', component: SongResultsComponent },
            { path: 'albums', component: AlbumResultsComponent },
            { path: 'artists', component: ArtistResultsComponent },
            { path: 'playlists', component: PlaylistResultsComponent },
            { path: 'profiles', component: ProfileResultsComponent },
            { path: '', redirectTo: 'songs', pathMatch: 'full' }
        ]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(searchRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class SearchRoutingModule {}
