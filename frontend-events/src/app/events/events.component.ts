import { Component, OnInit } from '@angular/core';
import { EventService } from '../event.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit{

  events: any[] = [];

  constructor(private eventService: EventService, private router: Router, private route: ActivatedRoute) { }


  navigateToPayment() {
    this.router.navigate(['/payment']);
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const query = params['query'];
      if (query) {
        this.eventService.searchEvents(query).subscribe(
          data => {
            this.events = data;
          },
          error => {
            console.error('Error searching events:', error);
          }
        );
      } else {
        this.eventService.getAllEvents().subscribe(
          data => {
            this.events = data;
          },
          error => {
            console.error('Error fetching events:', error);
          }
        );
      }
    });
  }

}
