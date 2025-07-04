import { Component, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { EventService } from '../event.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.css']
})
export class CreateEventComponent implements OnInit, AfterViewInit {

  event = {
    eventName: '',
    eventCategory: 'SPORT',
    eventCity: '',
    eventPlace: '',
    eventDate: '',
    eventPrice: 0,
    eventDetails: '',
    imgUrl: ''
  };

  categories = ['SPORT', 'MUSIC_DANCE', 'THEATER', 'CINEMA'];
  currentStep: number = 0;
  steps: string[] = ['Informations', 'Localisation', 'Médias'];
  totalSteps: number = 3;
  
  constructor(
    private eventService: EventService, 
    private router: Router,
    private elementRef: ElementRef
  ) { }

  ngOnInit() {
    // Initialisation avec des valeurs par défaut
    this.event.eventCategory = this.categories[0];
  }

  ngAfterViewInit() {
    // Configuration des gestionnaires d'événements après le chargement de la vue
    this.setupStepNavigation();
  }

  setupStepNavigation() {
    const prevBtn = this.elementRef.nativeElement.querySelector('#prevBtn');
    const nextBtn = this.elementRef.nativeElement.querySelector('#nextBtn');

    if (prevBtn) {
      prevBtn.addEventListener('click', () => {
        this.navigateStep(-1);
      });
    }

    if (nextBtn) {
      nextBtn.addEventListener('click', () => {
        this.navigateStep(1);
      });
    }

    // Désactiver le bouton Précédent au démarrage
    this.updateNavigationButtons();
  }

  navigateStep(direction: number) {
    // Calculer la nouvelle étape
    const newStep = this.currentStep + direction;
    
    // Vérifier si la nouvelle étape est valide
    if (newStep >= 0 && newStep < this.totalSteps) {
      // Mettre à jour les classes des étapes
      const steps = this.elementRef.nativeElement.querySelectorAll('.step');
      steps[this.currentStep].classList.remove('active');
      steps[newStep].classList.add('active');
      
      // Mettre à jour les sections du formulaire
      const sections = this.elementRef.nativeElement.querySelectorAll('.form-section');
      sections[this.currentStep].classList.remove('active');
      sections[newStep].classList.add('active');
      
      // Mettre à jour l'étape courante
      this.currentStep = newStep;
      
      // Mettre à jour les boutons de navigation
      this.updateNavigationButtons();
    }
  }

  updateNavigationButtons() {
    const prevBtn = this.elementRef.nativeElement.querySelector('#prevBtn');
    const nextBtn = this.elementRef.nativeElement.querySelector('#nextBtn');
    const submitBtn = this.elementRef.nativeElement.querySelector('.submit-btn');
    
    // Gérer le bouton Précédent
    if (prevBtn) {
      if (this.currentStep === 0) {
        prevBtn.setAttribute('disabled', 'disabled');
        prevBtn.classList.add('disabled');
      } else {
        prevBtn.removeAttribute('disabled');
        prevBtn.classList.remove('disabled');
      }
    }
    
    // Gérer le bouton Suivant
    if (nextBtn) {
      if (this.currentStep === this.totalSteps - 1) {
        nextBtn.setAttribute('disabled', 'disabled');
        nextBtn.classList.add('disabled');
      } else {
        nextBtn.removeAttribute('disabled');
        nextBtn.classList.remove('disabled');
      }
    }
    
    // Gérer la visibilité du bouton de soumission
    if (submitBtn) {
      if (this.currentStep === this.totalSteps - 1) {
        submitBtn.style.display = 'block';
      } else {
        submitBtn.style.display = 'none';
      }
    }
  }

  onSubmit() {
    console.log('Submitting event:', this.event);
    // Afficher un indicateur de chargement ou désactiver le bouton si nécessaire
    const submitBtn = this.elementRef.nativeElement.querySelector('.submit-btn');
    if (submitBtn) {
      submitBtn.setAttribute('disabled', 'disabled');
      submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Création en cours...';
    }
    
    this.eventService.createEvent(this.event).subscribe({
      next: (response) => {
        console.log('Event created successfully!', response);
        alert('Event created successfully!');
        this.router.navigate(['/events']);
      },
      error: (error) => {
        console.error('Error creating event:', error);
        alert('Failed to create event. Please try again.');
        // Réactiver le bouton en cas d'erreur
        if (submitBtn) {
          submitBtn.removeAttribute('disabled');
          submitBtn.innerHTML = '<i class="fas fa-check-circle me-2"></i>Créer l\'événement';
        }
      }
    });
  }
}
