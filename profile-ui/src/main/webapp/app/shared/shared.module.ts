import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {DatePipe} from "@angular/common";
import {CookieService} from "angular2-cookie/services/cookies.service";
import {
	ReferenceSharedLibsModule,
	ReferenceSharedCommonModule,
	CSRFService,
	AuthService,
	AuthServerProvider,
	AccountService,
	UserService,
	StateStorageService,
	LoginService,
	LoginModalService,
	Principal,
	HasAnyAuthorityDirective,
	JhiLoginModalComponent
} from "./";

@NgModule({
	imports: [
		ReferenceSharedLibsModule,
		ReferenceSharedCommonModule
	],
	declarations: [
		JhiLoginModalComponent,
		HasAnyAuthorityDirective
	],
	providers: [
		CookieService,
		LoginService,
		LoginModalService,
		AccountService,
		StateStorageService,
		Principal,
		CSRFService,
		AuthServerProvider,
		AuthService,
		UserService,
		DatePipe
	],
	entryComponents: [JhiLoginModalComponent],
	exports: [
		ReferenceSharedCommonModule,
		JhiLoginModalComponent,
		HasAnyAuthorityDirective,
		DatePipe
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class ReferenceSharedModule {
}
