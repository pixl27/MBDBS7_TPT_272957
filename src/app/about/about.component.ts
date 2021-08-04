import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {
  lat = -18.910177;
  lng = 47.507752;
  constructor() { }

  ngOnInit(): void {
  }

}
