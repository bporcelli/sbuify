import { Component, ViewChild, ElementRef } from '@angular/core';
import { Song } from "../songs/song";
import { PlayerService } from "./player.service";
import { BehaviorSubject } from "rxjs/BehaviorSubject";

@Component({
    selector: 'playbar',
    templateUrl: './playbar.component.html'
})
export class PlaybarComponent {

  // progress bar
  @ViewChild('progressbar') progress: ElementRef;

  constructor(private ps: PlayerService) {}

  togglePlayback(): void {
    this.ps.toggle();
  }

  hasPrev(): boolean {
    return this.ps.hasPrev();
  }

  hasNext(): boolean {
    return this.ps.hasNext();
  }

  setVolume(volume: number): void {
    this.ps.volume = volume;
  }

  toggleMute(e: Event): void {
    e.preventDefault();
    this.ps.muted = !this.ps.muted;
  }

  getPlayButtonClass(): string {
    let playing = this.playing.getValue();
    return playing ? 'fa-pause-circle-o' : 'fa-play-circle-o';
  }

  get song(): BehaviorSubject<Song> {
    return this.ps.song;
  }

  get time(): BehaviorSubject<number> {
    return this.ps.time;
  }

  get duration(): BehaviorSubject<number> {
    return this.ps.duration;
  }

  get playing(): BehaviorSubject<boolean> {
    return this.ps.playing;
  }

  get muted(): boolean {
    return this.ps.muted;
  }

  get sliderWidth(): string {
    let curTime = this.time.getValue();
    let duration = this.duration.getValue();
    return (100 * curTime / duration) + '%';
  }

  /** Update the playback time as the user interacts with the progress bar */
  startSeek(): void {
    let moveHandler = this.handleMouseMove();

    document.addEventListener('mousemove', moveHandler);
    document.addEventListener('mouseup', this.endSeek(moveHandler));
  }

  handleMouseMove(): EventListener {
    let that = this;

    return function (e: MouseEvent) {
      let leftX = that.progress.nativeElement.offsetLeft;
      let barWidth = that.progress.nativeElement.clientWidth;
      let duration = that.duration.getValue()
      let newTime = Math.min(((e.clientX - leftX) / barWidth) * duration, duration);

      that.ps.setTime(Math.max(newTime, 0));
    };
  }

  endSeek(moveHandler: EventListener): EventListener {
    return function handleEndSeek(e: MouseEvent) {
      moveHandler(e);

      document.removeEventListener('mousemove', moveHandler);
      document.removeEventListener('mouseup', handleEndSeek);
    };
  }
}
