import { Component, Input } from '@angular/core';

@Component({
  selector: 'concerts-table',
  templateUrl: './concerts-table.component.html'
})
export class ConcertsTableComponent {

  /** Concerts to display */
  @Input() concerts: any[] = [];

  /** Is the concerts list loading? */
  @Input() loading: boolean = true;

  /** Open the Songkick event page for a concert */
  openConcertPage(concert: any): void {
    window.open(concert.uri, '_blank');
  }
}
