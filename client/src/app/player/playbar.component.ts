import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Song } from "../songs/song";
import { PlayerService } from "./player.service";
import { Observable } from "rxjs/Observable";
import { tokenGetter } from "../auth/helpers";
import { BehaviorSubject } from "rxjs/BehaviorSubject";

@Component({
    selector: 'playbar',
    templateUrl: './playbar.component.html'
})
export class PlaybarComponent implements OnInit {

  // audio player
  player: any = new Audio();

  // progress bar
  @ViewChild('progressbar') progress: ElementRef;

  // current playback time (secs)
  _time: BehaviorSubject<number> = new BehaviorSubject(0);

  // duration of current song (secs)
  _duration: BehaviorSubject<number> = new BehaviorSubject(0);

  constructor(private ps: PlayerService) {}

  ngOnInit(): void {
    // update audio src when song changes
    this.ps.song.subscribe((song: Song) => {
      if (song != null) {
        this.player.src = '/api/stream/' + song.id + '/?token=' + tokenGetter()();
      }
    });

    // handle requests to start/pause playback
    this.ps.playing.subscribe((playing: boolean) => {
      if (!this.player)
        return;
      else if (playing)
        this.player.play();
      else
        this.player.pause();
    });

    // update duration when it changes
    Observable.fromEvent(this.player, 'durationchange').subscribe(() => {
      this._duration.next(this.player.duration);
    });

    // update the time as playback progresses
    Observable.fromEvent(this.player, 'timeupdate').subscribe(() => {
      this._time.next(this.player.currentTime);
    });
  }

  togglePlayback(): void {
    if (this.player.paused) {
      this.ps.play();
    } else {
      this.ps.pause();
    }
  }

  hasPrev(): boolean {
    return this.ps.hasPrev();
  }

  hasNext(): boolean {
    return this.ps.hasNext();
  }

  setVolume(volume: number): void {
    this.player.volume = volume;
  }

  toggleMute(e: Event): void {
    e.preventDefault();

    if (this.player.muted) {
      this.player.muted = false;
    } else {
      this.player.muted = true;
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
      let duration = that._duration.getValue()
      let newTime = Math.min(((e.clientX - leftX) / barWidth) * duration, duration);

      that.player.currentTime = Math.max(newTime, 0);
    };
  }

  endSeek(moveHandler: EventListener): EventListener {
    return function handleEndSeek(e: MouseEvent) {
      moveHandler(e);

      document.removeEventListener('mousemove', moveHandler);
      document.removeEventListener('mouseup', handleEndSeek);
    };
  }

  get song(): BehaviorSubject<Song> {
    return this.ps.song;
  }

  get time(): BehaviorSubject<number> {
    return this._time;
  }

  get duration(): BehaviorSubject<number> {
    return this._duration;
  }

  get sliderWidth(): Observable<string> {
    return this.time.map((newTime: number) => {
      return (100 * newTime / this.player.duration)  + '%';
    });
  }
}
