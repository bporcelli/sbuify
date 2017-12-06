import { Component, AfterViewInit, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { Observable } from "rxjs/Rx";

@Component({
  selector: 'filter-box',
  templateUrl: 'filter-box.component.html'
})
export class FilterBoxComponent implements AfterViewInit {
  @ViewChild('filterInput') filterInput: ElementRef;
  @Output() onFilterChanged: EventEmitter<string> = new EventEmitter();

  ngAfterViewInit(): void {
    let filterElement = this.filterInput.nativeElement;

    Observable.fromEvent(filterElement, 'input')
      .map((event: Event) => event.target['value'].trim())
      .subscribe((value: string) => this.onFilterChanged.emit(value));
  }

  resetFilter(): void {
    // reset filter input
    this.filterInput.nativeElement.value = '';
    // trigger update
    this.onFilterChanged.emit('');
  }
}
