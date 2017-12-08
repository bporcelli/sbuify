import { Component, ViewChild, ElementRef, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Song } from "./songs/song";
import { PlayerService } from "./playback/player.service";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { RepeatMode } from "./playback/repeat-mode";
import { LyricsModalComponent } from "./lyrics-modal.component";

@Component({
    selector: 'playbar',
    templateUrl: './playbar.component.html'
})
export class PlaybarComponent implements OnInit {

  /** Progress bar */
  @ViewChild('progressbar') progress: ElementRef;

  /** Current song */
  song: Song = null;

  constructor(
    private playerService: PlayerService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.playerService.song
      .subscribe((song) => this.song = song);
  }

  togglePlayback(): void {
    this.playerService.toggle();
  }

  next(): void {
    return this.playerService.next();
  }

  previous(): void {
    return this.playerService.prev();
  }

  hasNext(): boolean {
    return this.playerService.hasNext();
  }

  hasPrev(): boolean {
    return this.playerService.hasPrev();
  }

  hasSong(): boolean {
    return this.song != null;
  }

  setVolume(volume: number): void {
    this.playerService.volume = volume;
  }

  toggleMute(e: Event): void {
    e.preventDefault();
    this.playerService.muted = !this.playerService.muted;
  }

  toggleShuffle(): void {
    this.playerService.toggleShuffle();
  }

  isShuffled(): boolean {
    return this.playerService.shuffled;
  }

  toggleRepeat(): void {
    this.playerService.toggleRepeat();
  }

  getPlayButtonClass(): string {
    let playing = this.playing.getValue();
    return playing ? 'fa-pause-circle-o' : 'fa-play-circle-o';
  }

  getRepeatButtonClass(): string {
    if (this.playerService.repeat == RepeatMode.REPEAT) {
      return 'active';
    } else if (this.playerService.repeat == RepeatMode.REPEAT_ONE) {
      return 'active repeat-one';
    }
    return '';
  }

  get time(): BehaviorSubject<number> {
    return this.playerService.time;
  }

  get duration(): BehaviorSubject<number> {
    return this.playerService.duration;
  }

  get playing(): BehaviorSubject<boolean> {
    return this.playerService.playing;
  }

  get muted(): boolean {
    return this.playerService.muted;
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

      that.playerService.setTime(Math.max(newTime, 0));
    };
  }

  endSeek(moveHandler: EventListener): EventListener {
    return function handleEndSeek(e: MouseEvent) {
      moveHandler(e);

      document.removeEventListener('mousemove', moveHandler);
      document.removeEventListener('mouseup', handleEndSeek);
    };
  }

  /** Open the Lyrics modal for the current song */
  openLyricsModal(): void {
    let modal = this.modalService.open(LyricsModalComponent);
    modal.componentInstance.song = this.song;
  }
}
