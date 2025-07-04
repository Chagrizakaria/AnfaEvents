import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  formLogin! : FormGroup;

  constructor(private fb : FormBuilder, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.formLogin=this.fb.group({
      email : this.fb.control(""),
      password : this.fb.control("")
    })
  }

  handleLogin(){
    let email = this.formLogin.value.email;
    let pswd = this.formLogin.value.password
    this.authService.login(email, pswd).subscribe({
      next: data => {
        if (data && (data as any)['access-token']) {
          this.authService.loadProfile(data);
          this.router.navigateByUrl("/events");
        } else {
          alert("An unexpected error occurred during login.");
        }
      },
      error: err => {
        console.error("Login failed:", err);
        alert("Invalid username or password. Please try again.");
      }
    });
  }

}
