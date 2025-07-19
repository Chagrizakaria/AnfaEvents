import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    console.log('Intercepting request to:', request.url);
    
    // More robust check for auth endpoints
    if (request.url.indexOf('/auth/login') !== -1 || 
        request.url.indexOf('/auth/register') !== -1 ||
        request.url.endsWith('/auth/login') || 
        request.url.endsWith('/auth/register')) {
      console.log('Public auth route detected, skipping token');
      return next.handle(request);
    }

    // For all other (protected) routes, add the Authorization header
    if (this.authService.accessToken) {
      console.log('Adding auth token to request');
      const newRequest = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + this.authService.accessToken)
      });
      return next.handle(newRequest);
    }
    
    // If there's no token, pass the original request through
    console.log('No token available, passing request through');
    return next.handle(request);

  }
}
