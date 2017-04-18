import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {ManageRecordComponent} from "./manage-record.component";
import {ManageRecordDetailComponent} from "./manage-record-detail.component";
import {ManageRecordPopupComponent} from "./manage-record-dialog.component";
import {ManageRecordDeletePopupComponent} from "./manage-record-delete-dialog.component";

@Injectable()
export class ManageRecordResolvePagingParams implements Resolve<any> {

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

export const manageRecordRoute: Routes = [
	{
		path: 'manage-record',
		component: ManageRecordComponent,
		resolve: {
			'pagingParams': ManageRecordResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.manageRecord.home.title'
		}
	}, {
		path: 'manage-record/:id',
		component: ManageRecordDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.manageRecord.home.title'
		}
	}
];

export const manageRecordPopupRoute: Routes = [
	{
		path: 'manage-record-new',
		component: ManageRecordPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.manageRecord.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'manage-record/:id/edit',
		component: ManageRecordPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.manageRecord.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'manage-record/:id/delete',
		component: ManageRecordDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.manageRecord.home.title'
		},
		outlet: 'popup'
	}
];
