import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs/Rx";
import {EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService} from "ng-jhipster";
import {HeeUser} from "./hee-user.model";
import {HeeUserService} from "./hee-user.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";
import {PaginationConfig} from "../../blocks/config/uib-pagination.config";

@Component({
	selector: 'jhi-hee-user',
	templateUrl: './hee-user.component.html'
})
export class HeeUserComponent implements OnInit, OnDestroy {

	currentAccount: any;
	heeUsers: HeeUser[];
	error: any;
	success: any;
	eventSubscriber: Subscription;
	routeData: any;
	links: any;
	totalItems: any;
	queryCount: any;
	itemsPerPage: any;
	page: any;
	predicate: any;
	previousPage: any;
	reverse: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private heeUserService: HeeUserService,
				private parseLinks: ParseLinks,
				private alertService: AlertService,
				private principal: Principal,
				private activatedRoute: ActivatedRoute,
				private router: Router,
				private eventManager: EventManager,
				private paginationUtil: PaginationUtil,
				private paginationConfig: PaginationConfig) {
		this.itemsPerPage = ITEMS_PER_PAGE;
		this.routeData = this.activatedRoute.data.subscribe(data => {
			this.page = data['pagingParams'].page;
			this.previousPage = data['pagingParams'].page;
			this.reverse = data['pagingParams'].ascending;
			this.predicate = data['pagingParams'].predicate;
		});
		this.jhiLanguageService.setLocations(['heeUser']);
	}

	loadAll() {
		this.heeUserService.query({
			page: this.page - 1,
			size: this.itemsPerPage,
			sort: this.sort()
		}).subscribe(
			(res: Response) => this.onSuccess(res.json(), res.headers),
			(res: Response) => this.onError(res.json())
		);
	}

	loadPage(page: number) {
		if (page !== this.previousPage) {
			this.previousPage = page;
			this.transition();
		}
	}

	transition() {
		this.router.navigate(['/hee-user'], {
			queryParams: {
				page: this.page,
				size: this.itemsPerPage,
				sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
			}
		});
		this.loadAll();
	}

	clear() {
		this.page = 0;
		this.router.navigate(['/hee-user', {
			page: this.page,
			sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
		}]);
		this.loadAll();
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInHeeUsers();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: HeeUser) {
		return item.name;
	}


	registerChangeInHeeUsers() {
		this.eventSubscriber = this.eventManager.subscribe('heeUserListModification', (response) => this.loadAll());
	}

	sort() {
		let result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
		if (this.predicate !== 'name') {
			result.push('name');
		}
		return result;
	}

	private onSuccess(data, headers) {
		this.links = this.parseLinks.parse(headers.get('link'));
		this.totalItems = headers.get('X-Total-Count');
		this.queryCount = this.totalItems;
		// this.page = pagingParams.page;
		this.heeUsers = data;
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
