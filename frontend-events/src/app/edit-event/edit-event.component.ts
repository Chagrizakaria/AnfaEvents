import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventService } from '../event.service';

@Component({
  selector: 'app-edit-event',
  templateUrl: './edit-event.component.html',
  styleUrls: ['./edit-event.component.css']
})
export class EditEventComponent implements OnInit {

  eventForm: FormGroup;
  eventId!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private eventService: EventService
  ) {
    this.eventForm = this.fb.group({
      eventName: ['', Validators.required],
      eventCategory: ['', Validators.required],
      eventCity: ['', Validators.required],
      eventPlace: ['', Validators.required],
      eventDate: ['', Validators.required],
      eventPrice: ['', Validators.required],
      eventDetails: ['', Validators.required],
      imgUrl: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.eventId = this.route.snapshot.params['id'];
    this.eventService.getEventById(this.eventId).subscribe(
      data => {
        this.eventForm.patchValue(data);
      },
      error => {
        console.error('Error fetching event:', error);
      }
    );
  }

  onSubmit(): void {
    if (this.eventForm.valid) {
      this.eventService.updateEvent(this.eventId, this.eventForm.value).subscribe(
        () => {
          this.router.navigate(['/admin/events']);
        },
        error => {
          console.error('Error updating event:', error);
        }
      );
    }
  }
}
