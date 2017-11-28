import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Genre } from "./genre";

@Component({
    templateUrl: './genres.component.html'
})
export class GenresComponent implements OnInit {

  // genres to display
  genres: Array<Genre> = [];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.data.subscribe(
      (data: { genres: Array<Genre> }) => {
        this.genres = data.genres;
      }
    )
  }
}
