import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {ManageRecord} from "./manage-record.model";
import {DateUtils} from "ng-jhipster";
@Injectable()
export class ManageRecordService {

	private resourceUrl = 'api/manage-records';

	constructor(private http: Http, private dateUtils: DateUtils) {
	}

	create(manageRecord: ManageRecord): Observable<ManageRecord> {
		let copy: ManageRecord = Object.assign({}, manageRecord);
		copy.inactiveFrom = this.dateUtils
			.convertLocalDateToServer(manageRecord.inactiveFrom);
		copy.inactiveDate = this.dateUtils
			.convertLocalDateToServer(manageRecord.inactiveDate);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(manageRecord: ManageRecord): Observable<ManageRecord> {
		let copy: ManageRecord = Object.assign({}, manageRecord);
		copy.inactiveFrom = this.dateUtils
			.convertLocalDateToServer(manageRecord.inactiveFrom);
		copy.inactiveDate = this.dateUtils
			.convertLocalDateToServer(manageRecord.inactiveDate);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<ManageRecord> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			let jsonResponse = res.json();
			jsonResponse.inactiveFrom = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.inactiveFrom);
			jsonResponse.inactiveDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.inactiveDate);
			return jsonResponse;
		});
	}

	query(req?: any): Observable<Response> {
		let options = this.createRequestOption(req);
		return this.http.get(this.resourceUrl, options)
			.map((res: any) => this.convertResponse(res))
			;
	}

	delete(id: number): Observable<Response> {
		return this.http.delete(`${this.resourceUrl}/${id}`);
	}


	private convertResponse(res: any): any {
		let jsonResponse = res.json();
		for (let i = 0; i < jsonResponse.length; i++) {
			jsonResponse[i].inactiveFrom = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].inactiveFrom);
			jsonResponse[i].inactiveDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].inactiveDate);
		}
		res._body = jsonResponse;
		return res;
	}

	private createRequestOption(req?: any): BaseRequestOptions {
		let options: BaseRequestOptions = new BaseRequestOptions();
		if (req) {
			let params: URLSearchParams = new URLSearchParams();
			params.set('page', req.page);
			params.set('size', req.size);
			if (req.sort) {
				params.paramsMap.set('sort', req.sort);
			}
			params.set('query', req.query);

			options.search = params;
		}
		return options;
	}
}
