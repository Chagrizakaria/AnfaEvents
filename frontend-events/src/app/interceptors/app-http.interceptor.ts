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
    // Check if the request is for a public route (login or register)
    if (request.url.includes("/auth/login") || request.url.includes("/auth/register")) {
      // If it is, pass the request through without modification
      return next.handle(request);
    }

    // For all other (protected) routes, add the Authorization header
    if (this.authService.accessToken) {
      const newRequest = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + this.authService.accessToken)
      });
      return next.handle(newRequest);
    }
    
    // If there's no token, pass the original request through
    return next.handle(request);

  }
}
