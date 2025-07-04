import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EventsComponent } from './events/events.component';
import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { ContactComponent } from './contact/contact.component';
import { PaymentComponent } from './payment/payment.component';
import { ServicesComponent } from './services/services.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { CreateEventComponent } from './create-event/create-event.component';
import { AdminEventsComponent } from './admin-events/admin-events.component';
import { EditEventComponent } from './edit-event/edit-event.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'events',
    component: EventsComponent
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'about',
    component: AboutComponent
  },
  {
    path: 'contact',
    component: ContactComponent
  },
  {
    path: 'payment',
    component: PaymentComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'services',
    component: ServicesComponent
  },
  {
    path: "login",component: LoginComponent
  },
  {
    path: 'register', component: RegisterComponent
  },
  {
    path: 'create-event',
    component: CreateEventComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/events',
    component: AdminEventsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/events/edit/:id',
    component: EditEventComponent,
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
