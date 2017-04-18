import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Person} from "./person.model";
import {PersonService} from "./person.service";

@Component({
	selector: 'jhi-person-detail',
	templateUrl: './person-detail.component.html'
})
export class PersonDetailComponent implements OnInit, OnDestroy {

	person: Person;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private personService: PersonService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['person']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.personService.find(id).subscribe(person => {
			this.person = person;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
