import "./vendor.ts";
import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {Ng2Webstorage} from "ng2-webstorage";
import {ProfileSharedModule, UserRouteAccessService} from "./shared";
import {ProfileHomeModule} from "./home/home.module";
import {ProfileAdminModule} from "./admin/admin.module";
import {ProfileEntityModule} from "./entities/entity.module";
import {
	LayoutRoutingModule,
	JhiMainComponent,
	NavbarComponent,
	FooterComponent,
	ProfileService,
	PageRibbonComponent,
	ActiveMenuDirective,
	ErrorComponent
} from "./layouts";
import {customHttpProvider} from "./blocks/interceptor/http.provider";
import {PaginationConfig} from "./blocks/config/uib-pagination.config";


@NgModule({
	imports: [
		BrowserModule,
		LayoutRoutingModule,
		Ng2Webstorage.forRoot({prefix: 'jhi', separator: '-'}),
		ProfileSharedModule,
		ProfileHomeModule,
		ProfileAdminModule,
		ProfileEntityModule
	],
	declarations: [
		JhiMainComponent,
		NavbarComponent,
		ErrorComponent,
		PageRibbonComponent,
		ActiveMenuDirective,
		FooterComponent
	],
	providers: [
		ProfileService,
		{provide: Window, useValue: window},
		{provide: Document, useValue: document},
		customHttpProvider(),
		PaginationConfig,
		UserRouteAccessService
	],
	bootstrap: [JhiMainComponent]
})
export class ProfileAppModule {
}