import { Pipe, PipeTransform } from '@angular/core';
import { Image } from "./image";
import { Config } from "../config";

/**
 * Pipe to transform an Image object into a URI.
 */
@Pipe({
  name: 'toImageSrc'
})
export class ToImageSrcPipe implements PipeTransform {
  transform(image: Image, size: string = 'full'): string {
    let id = image == null ? Config.DEFAULT_IMAGE_ID : image.id;
    return '/static/i/' + id + '/' + size;
  }
}
