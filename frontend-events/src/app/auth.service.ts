import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = `${environment.apiUrl}/auth`;
  isAuthenticated: boolean = false;
  username: string | null = null;
  role: string | null = null;
  accessToken: string | null = null;

  constructor(private http: HttpClient, private router: Router) {
    this.loadTokenFromStorage();
  }

  private loadTokenFromStorage(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedJwt: any = jwtDecode(token);
        if (decodedJwt.exp * 1000 > Date.now()) {
          this.accessToken = token;
          this.username = decodedJwt.sub;
          this.role = decodedJwt.scope;
          this.isAuthenticated = true;
        } else {
          this.logout();
        }
      } catch (error) {
        console.error("Failed to decode token from storage. Logging out.", error);
        this.logout();
      }
    }
  }

  public login(email: string, password: string): Observable<any> {
    let httpParams = new HttpParams()
      .set('username', email)
      .set('password', password);

    return this.http.post(`${this.baseUrl}/login`, httpParams, {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    });
  }
  
  

  public register(userInfo: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, userInfo);
  }

  public loadProfile(data: any): void {
    if (data && data['access-token']) {
      const token = data['access-token'];
      try {
        const decodedJwt: any = jwtDecode(token);
        this.accessToken = token;
        this.username = decodedJwt.sub;
        this.role = decodedJwt.scope;
        this.isAuthenticated = true;
        localStorage.setItem('token', token);
      } catch (error) {
        console.error("Failed to process token after login. Logging out.", error);
        this.logout();
      }
    } else {
        console.error("loadProfile called with invalid data.", data);
    }
  }

  public logout(): void {
    this.isAuthenticated = false;
    this.role = null;
    this.username = null;
    this.accessToken = null;
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  getUsername(): string | null {
    return this.username ? this.username.split('@')[0] : null;
  }

  public hasRole(role: string): boolean {
    return !!this.role && this.role.includes(role);
  }
}
