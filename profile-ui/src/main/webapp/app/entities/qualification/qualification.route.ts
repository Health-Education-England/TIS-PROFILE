import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {QualificationComponent} from "./qualification.component";
import {QualificationDetailComponent} from "./qualification-detail.component";
import {QualificationPopupComponent} from "./qualification-dialog.component";
import {QualificationDeletePopupComponent} from "./qualification-delete-dialog.component";

@Injectable()
export class QualificationResolvePagingParams implements Resolve<any> {

	constructor(private paginationUtil: PaginationUtil) {
	}

	resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
		let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
		return {
			page: this.paginationUtil.parsePage(page),
			predicate: this.paginationUtil.parsePredicate(sort),
			ascending: this.paginationUtil.parseAscending(sort)
		};
	}
}

export const qualificationRoute: Routes = [
	{
		path: 'qualification',
		component: QualificationComponent,
		resolve: {
			'pagingParams': QualificationResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.qualification.home.title'
		}
	}, {
		path: 'qualification/:id',
		component: QualificationDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.qualification.home.title'
		}
	}
];

export const qualificationPopupRoute: Routes = [
	{
		path: 'qualification-new',
		component: QualificationPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.qualification.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'qualification/:id/edit',
		component: QualificationPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.qualification.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'qualification/:id/delete',
		component: QualificationDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.qualification.home.title'
		},
		outlet: 'popup'
	}
];
