import { Pipe, PipeTransform } from '@angular/core';

/**
 * Pipe used to transform plaintext lyrics into HTML.
 */
@Pipe({
  name: "lyricsToHTML"
})
export class LyricsToHtmlPipe implements PipeTransform {
  PARAGRAPH_DELIMITER = '\n\n';

  transform(value: any, ...args): any {
    let paragraphs: string[] = value.split(this.PARAGRAPH_DELIMITER);
    let html: string = "";
    for (let paragraph of paragraphs) {
      let trimmed = paragraph.trim();
      html += "<p>" + trimmed.replace(/\n/g, '<br>') + "</p>";
    }
    return html;
  }
}
