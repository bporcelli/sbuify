import { Pipe, PipeTransform } from '@angular/core';

/**
 * Transforms a duration in milliseconds into an expression of the form HH:MM:SS.
 */
@Pipe({
  name: 'formatDuration'
})
export class FormatDurationPipe implements PipeTransform {
  SECS_PER_MIN: number = 60;
  MINS_PER_HOUR: number = 60;
  SECS_PER_HOUR: number = this.SECS_PER_MIN * 60;

  transform(millis: number): any {
    let seconds = millis / 1000;

    let secs: number = Math.floor(seconds % this.SECS_PER_MIN);
    let mins: number = Math.floor((seconds / this.SECS_PER_MIN) % this.MINS_PER_HOUR);
    let hours: number = Math.floor((seconds / (this.SECS_PER_MIN * this.MINS_PER_HOUR)) % this.SECS_PER_HOUR);

    return this.stringify(hours, mins, secs);
  }

  private stringify(hours: number, mins: number, secs: number): string {
    let hourString = (hours > 0) ? (hours.toString() + ":") : "";
    let minuteString = mins.toString();
    let secondString = secs.toString();
    if (secondString.length == 1) {  // pad to two digits
      secondString = "0" + secondString;
    }
    return hourString + minuteString + ":" + secondString;
  }
}
