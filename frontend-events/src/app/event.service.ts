import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private apiUrl = `${environment.apiUrl}/api/events`;
  private publicApiUrl = `${environment.apiUrl}/api/public/events`;

  constructor(private http:HttpClient) { }

  getAllEvents(): Observable<any>{
    return this.http.get(this.publicApiUrl)
  }

  getEventById(eventId:number): Observable<any>{
    return this.http.get(`${this.apiUrl}/${eventId}`)
  }

  // createEvent(event: any): Observable<any> {
  //   return this.http.post(this.apiUrl, event);
  // }
  createEvent(event: any): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(this.apiUrl, event, { headers });
  }

  updateEvent(id: number, event: any): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.put(`${this.apiUrl}/${id}`, event, { headers });
  }

  deleteEvent(id: number): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.delete(`${this.apiUrl}/${id}`, { headers });
  }

  searchEvents(query: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/search?query=${query}`);
  }

  deleteMultipleEvents(ids: number[]): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(`${this.apiUrl}/batch-delete`, ids, { headers });
  }
  
}
