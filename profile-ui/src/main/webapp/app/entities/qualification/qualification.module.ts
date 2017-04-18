import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileSharedModule} from "../../shared";
import {
	QualificationService,
	QualificationPopupService,
	QualificationComponent,
	QualificationDetailComponent,
	QualificationDialogComponent,
	QualificationPopupComponent,
	QualificationDeletePopupComponent,
	QualificationDeleteDialogComponent,
	qualificationRoute,
	qualificationPopupRoute,
	QualificationResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...qualificationRoute,
	...qualificationPopupRoute,
];

@NgModule({
	imports: [
		ProfileSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		QualificationComponent,
		QualificationDetailComponent,
		QualificationDialogComponent,
		QualificationDeleteDialogComponent,
		QualificationPopupComponent,
		QualificationDeletePopupComponent,
	],
	entryComponents: [
		QualificationComponent,
		QualificationDialogComponent,
		QualificationPopupComponent,
		QualificationDeleteDialogComponent,
		QualificationDeletePopupComponent,
	],
	providers: [
		QualificationService,
		QualificationPopupService,
		QualificationResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfileQualificationModule {
}
