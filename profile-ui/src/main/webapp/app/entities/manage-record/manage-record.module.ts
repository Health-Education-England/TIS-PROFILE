import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileSharedModule} from "../../shared";
import {
	ManageRecordService,
	ManageRecordPopupService,
	ManageRecordComponent,
	ManageRecordDetailComponent,
	ManageRecordDialogComponent,
	ManageRecordPopupComponent,
	ManageRecordDeletePopupComponent,
	ManageRecordDeleteDialogComponent,
	manageRecordRoute,
	manageRecordPopupRoute,
	ManageRecordResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...manageRecordRoute,
	...manageRecordPopupRoute,
];

@NgModule({
	imports: [
		ProfileSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		ManageRecordComponent,
		ManageRecordDetailComponent,
		ManageRecordDialogComponent,
		ManageRecordDeleteDialogComponent,
		ManageRecordPopupComponent,
		ManageRecordDeletePopupComponent,
	],
	entryComponents: [
		ManageRecordComponent,
		ManageRecordDialogComponent,
		ManageRecordPopupComponent,
		ManageRecordDeleteDialogComponent,
		ManageRecordDeletePopupComponent,
	],
	providers: [
		ManageRecordService,
		ManageRecordPopupService,
		ManageRecordResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfileManageRecordModule {
}
