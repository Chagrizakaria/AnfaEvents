import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend-events';
  showDropdown = false;
  searchQuery: string = '';
  private navbarCollapsed = true;

  constructor(private router: Router, private authService: AuthService) {}

  navigateToHome() {
    this.router.navigate(['/home']);
    this.closeNavbar();
  }

  isAuthenticated() {
    return this.authService.isAuthenticated;
  }

  getUsername() {
    return this.authService.getUsername();
  }

  hasRole(role: string) {
    return this.authService.hasRole(role);
  }

  logout() {
    this.authService.logout();
    this.showDropdown = false; // Close the dropdown after logout
    this.closeNavbar();
    console.log('Logout clicked');
  }

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  handleSearch() {
    if (this.searchQuery.trim()) {
      this.router.navigate(['/events'], { queryParams: { query: this.searchQuery } });
    } else {
      this.router.navigate(['/events']);
    }
    this.closeNavbar();
  }

  // Méthode pour fermer le menu navbar mobile
  closeNavbar() {
    // Cette méthode utilise le DOM pour trouver et fermer la navbar
    const navbarToggler = document.querySelector('.navbar-toggler') as HTMLElement;
    const navbarCollapse = document.querySelector('.navbar-collapse') as HTMLElement;
    
    if (navbarCollapse && navbarCollapse.classList.contains('show')) {
      if (navbarToggler) {
        navbarToggler.click(); // Simule un clic sur le bouton hamburger pour fermer le menu
      } else {
        // Fermeture manuelle si le toggler n'est pas trouvé
        navbarCollapse.classList.remove('show');
      }
    }
  }
}
