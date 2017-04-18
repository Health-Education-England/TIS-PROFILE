import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {ManageRecord} from "./manage-record.model";
import {ManageRecordService} from "./manage-record.service";

@Component({
	selector: 'jhi-manage-record-detail',
	templateUrl: './manage-record-detail.component.html'
})
export class ManageRecordDetailComponent implements OnInit, OnDestroy {

	manageRecord: ManageRecord;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private manageRecordService: ManageRecordService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['manageRecord']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.manageRecordService.find(id).subscribe(manageRecord => {
			this.manageRecord = manageRecord;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
