import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../shared";
import {
	adminState,
	LogsComponent,
	JhiMetricsMonitoringModalComponent,
	JhiMetricsMonitoringComponent,
	JhiHealthModalComponent,
	JhiHealthCheckComponent,
	JhiConfigurationComponent,
	JhiDocsComponent,
	JhiConfigurationService,
	JhiHealthService,
	JhiMetricsService,
	LogsService
} from "./";


@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(adminState, {useHash: true})
	],
	declarations: [
		LogsComponent,
		JhiConfigurationComponent,
		JhiHealthCheckComponent,
		JhiHealthModalComponent,
		JhiDocsComponent,
		JhiMetricsMonitoringComponent,
		JhiMetricsMonitoringModalComponent
	],
	entryComponents: [
		JhiHealthModalComponent,
		JhiMetricsMonitoringModalComponent,
	],
	providers: [
		JhiConfigurationService,
		JhiHealthService,
		JhiMetricsService,
		LogsService
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceAdminModule {
}
