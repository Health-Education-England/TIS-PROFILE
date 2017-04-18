import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileSharedModule} from "../../shared";
import {
	PersonalDetailsService,
	PersonalDetailsPopupService,
	PersonalDetailsComponent,
	PersonalDetailsDetailComponent,
	PersonalDetailsDialogComponent,
	PersonalDetailsPopupComponent,
	PersonalDetailsDeletePopupComponent,
	PersonalDetailsDeleteDialogComponent,
	personalDetailsRoute,
	personalDetailsPopupRoute,
	PersonalDetailsResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...personalDetailsRoute,
	...personalDetailsPopupRoute,
];

@NgModule({
	imports: [
		ProfileSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		PersonalDetailsComponent,
		PersonalDetailsDetailComponent,
		PersonalDetailsDialogComponent,
		PersonalDetailsDeleteDialogComponent,
		PersonalDetailsPopupComponent,
		PersonalDetailsDeletePopupComponent,
	],
	entryComponents: [
		PersonalDetailsComponent,
		PersonalDetailsDialogComponent,
		PersonalDetailsPopupComponent,
		PersonalDetailsDeleteDialogComponent,
		PersonalDetailsDeletePopupComponent,
	],
	providers: [
		PersonalDetailsService,
		PersonalDetailsPopupService,
		PersonalDetailsResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfilePersonalDetailsModule {
}
