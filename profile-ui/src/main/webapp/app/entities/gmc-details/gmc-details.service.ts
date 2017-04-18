import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {GmcDetails} from "./gmc-details.model";
import {DateUtils} from "ng-jhipster";
@Injectable()
export class GmcDetailsService {

	private resourceUrl = 'api/gmc-details';

	constructor(private http: Http, private dateUtils: DateUtils) {
	}

	create(gmcDetails: GmcDetails): Observable<GmcDetails> {
		let copy: GmcDetails = Object.assign({}, gmcDetails);
		copy.gmcStartDate = this.dateUtils
			.convertLocalDateToServer(gmcDetails.gmcStartDate);
		copy.gmcExpiryDate = this.dateUtils
			.convertLocalDateToServer(gmcDetails.gmcExpiryDate);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(gmcDetails: GmcDetails): Observable<GmcDetails> {
		let copy: GmcDetails = Object.assign({}, gmcDetails);
		copy.gmcStartDate = this.dateUtils
			.convertLocalDateToServer(gmcDetails.gmcStartDate);
		copy.gmcExpiryDate = this.dateUtils
			.convertLocalDateToServer(gmcDetails.gmcExpiryDate);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<GmcDetails> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			let jsonResponse = res.json();
			jsonResponse.gmcStartDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.gmcStartDate);
			jsonResponse.gmcExpiryDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.gmcExpiryDate);
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
			jsonResponse[i].gmcStartDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].gmcStartDate);
			jsonResponse[i].gmcExpiryDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].gmcExpiryDate);
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
