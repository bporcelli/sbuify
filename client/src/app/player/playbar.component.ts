import { Component, ViewChild, ElementRef } from '@angular/core';
import { Song } from "../songs/song";
import { PlayerService } from "./player.service";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import {RepeatMode} from "./repeat-mode";

@Component({
    selector: 'playbar',
    templateUrl: './playbar.component.html'
})
export class PlaybarComponent {

  // todo: can we avoid susbcribing to song multiple times in the template?

  // progress bar
  @ViewChild('progressbar') progress: ElementRef;

  constructor(private ps: PlayerService) {}

  togglePlayback(): void {
    this.ps.toggle();
  }

  next(): void {
    return this.ps.next();
  }

  previous(): void {
    return this.ps.prev();
  }

  hasNext(): boolean {
    return this.ps.hasNext();
  }

  hasPrev(): boolean {
    return this.ps.hasPrev();
  }

  hasSong(): boolean {
    return this.song.getValue() != null;
  }

  setVolume(volume: number): void {
    this.ps.volume = volume;
  }

  toggleMute(e: Event): void {
    e.preventDefault();
    this.ps.muted = !this.ps.muted;
  }

  toggleShuffle(): void {
    this.ps.toggleShuffle();
  }

  isShuffled(): boolean {
    return this.ps.shuffled;
  }

  toggleRepeat(): void {
    this.ps.toggleRepeat();
  }

  getPlayButtonClass(): string {
    let playing = this.playing.getValue();
    return playing ? 'fa-pause-circle-o' : 'fa-play-circle-o';
  }

  getRepeatButtonClass(): string {
    if (this.ps.repeat == RepeatMode.REPEAT) {
      return 'active';
    } else if (this.ps.repeat == RepeatMode.REPEAT_ONE) {
      return 'active repeat-one';
    }
    return '';
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
    if (!this.hasSong()) {
      return '0%';
    } else {
      return (100 * curTime / duration) + '%';
    }
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
