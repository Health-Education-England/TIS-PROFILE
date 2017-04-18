import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {GdcDetails} from "./gdc-details.model";
import {DateUtils} from "ng-jhipster";
@Injectable()
export class GdcDetailsService {

	private resourceUrl = 'api/gdc-details';

	constructor(private http: Http, private dateUtils: DateUtils) {
	}

	create(gdcDetails: GdcDetails): Observable<GdcDetails> {
		let copy: GdcDetails = Object.assign({}, gdcDetails);
		copy.gdcStartDate = this.dateUtils
			.convertLocalDateToServer(gdcDetails.gdcStartDate);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(gdcDetails: GdcDetails): Observable<GdcDetails> {
		let copy: GdcDetails = Object.assign({}, gdcDetails);
		copy.gdcStartDate = this.dateUtils
			.convertLocalDateToServer(gdcDetails.gdcStartDate);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<GdcDetails> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			let jsonResponse = res.json();
			jsonResponse.gdcStartDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.gdcStartDate);
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
			jsonResponse[i].gdcStartDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].gdcStartDate);
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
