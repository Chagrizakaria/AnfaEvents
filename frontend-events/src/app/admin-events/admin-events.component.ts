import { Component, OnInit } from '@angular/core';
import { EventService } from '../event.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-events',
  templateUrl: './admin-events.component.html',
  styleUrls: ['./admin-events.component.css']
})
export class AdminEventsComponent implements OnInit {

  events: any[] = [];
  filteredEvents: any[] = [];
  searchQuery: string = '';
  selectedEventIds = new Set<number>();

  constructor(private eventService: EventService, private router: Router) { }

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.eventService.getAllEvents().subscribe(
      data => {
        this.events = data;
        this.filteredEvents = data;
        this.selectedEventIds.clear(); // Clear selection on reload
      },
      error => {
        console.error('Error fetching events:', error);
      }
    );
  }

  deleteEvent(id: number): void {
    if (confirm('Are you sure you want to delete this event?')) {
      this.eventService.deleteEvent(id).subscribe(
        () => {
          this.loadEvents(); // Refresh the list after deletion
        },
        error => {
          console.error('Error deleting event:', error);
        }
      );
    }
  }

  editEvent(id: number): void {
    this.router.navigate(['/admin/events/edit', id]);
  }

  onSearchChange(): void {
    this.filteredEvents = this.events.filter(event =>
      event.eventName.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      event.eventCity.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      event.eventPlace.toLowerCase().includes(this.searchQuery.toLowerCase())
    );
    this.selectedEventIds.clear(); // Clear selection when search query changes
  }

  // Methods for checkbox selection
  onCheckboxChange(event: any, eventId: number): void {
    if (event.target.checked) {
      this.selectedEventIds.add(eventId);
    } else {
      this.selectedEventIds.delete(eventId);
    }
  }

  isSelected(eventId: number): boolean {
    return this.selectedEventIds.has(eventId);
  }

  onSelectAllChange(event: any): void {
    if (event.target.checked) {
      this.filteredEvents.forEach(e => this.selectedEventIds.add(e.enventId));
    } else {
      this.selectedEventIds.clear();
    }
  }

  deleteSelectedEvents(): void {
    const idsToDelete = Array.from(this.selectedEventIds);
    if (idsToDelete.length > 0 && confirm(`Are you sure you want to delete ${idsToDelete.length} events?`)) {
      this.eventService.deleteMultipleEvents(idsToDelete).subscribe(
        () => {
          this.loadEvents(); // Refresh the list
        },
        error => {
          console.error('Error deleting events:', error);
        }
      );
    }
  }
}
