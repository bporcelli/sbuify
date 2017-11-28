import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Genre } from "./genre";
import { GenreService } from "app/browse/genres/genre.service";
import { Album } from "../../album/album";
import { BehaviorSubject } from "rxjs/BehaviorSubject";

@Component({
  templateUrl: './genre.component.html'
})
export class GenreComponent implements OnInit {

  genre: Genre = null;

  // top albums in the genre
  popular: BehaviorSubject<Array<Album>> = new BehaviorSubject(null);

  // most recent albums in the genre
  recent: BehaviorSubject<Array<Album>> = new BehaviorSubject(null);

  constructor(private route: ActivatedRoute,
              private gs: GenreService) {}

  ngOnInit(): void {
    this.route.data.subscribe((data: { genre: Genre }) => {
      this.genre = data.genre;

      this.gs.getPopular(this.genre.id).subscribe((albums: Array<Album>) => {
        this.popular.next(albums);
      });

      this.gs.getRecent(this.genre.id).subscribe((albums: Array<Album>) => {
        this.recent.next(albums);
      });
    });
  }
}
