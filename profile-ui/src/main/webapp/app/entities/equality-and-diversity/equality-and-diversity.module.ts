import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileSharedModule} from "../../shared";
import {
	EqualityAndDiversityService,
	EqualityAndDiversityPopupService,
	EqualityAndDiversityComponent,
	EqualityAndDiversityDetailComponent,
	EqualityAndDiversityDialogComponent,
	EqualityAndDiversityPopupComponent,
	EqualityAndDiversityDeletePopupComponent,
	EqualityAndDiversityDeleteDialogComponent,
	equalityAndDiversityRoute,
	equalityAndDiversityPopupRoute,
	EqualityAndDiversityResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...equalityAndDiversityRoute,
	...equalityAndDiversityPopupRoute,
];

@NgModule({
	imports: [
		ProfileSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		EqualityAndDiversityComponent,
		EqualityAndDiversityDetailComponent,
		EqualityAndDiversityDialogComponent,
		EqualityAndDiversityDeleteDialogComponent,
		EqualityAndDiversityPopupComponent,
		EqualityAndDiversityDeletePopupComponent,
	],
	entryComponents: [
		EqualityAndDiversityComponent,
		EqualityAndDiversityDialogComponent,
		EqualityAndDiversityPopupComponent,
		EqualityAndDiversityDeleteDialogComponent,
		EqualityAndDiversityDeletePopupComponent,
	],
	providers: [
		EqualityAndDiversityService,
		EqualityAndDiversityPopupService,
		EqualityAndDiversityResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfileEqualityAndDiversityModule {
}
