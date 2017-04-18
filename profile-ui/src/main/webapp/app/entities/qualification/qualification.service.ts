import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {Qualification} from "./qualification.model";
import {DateUtils} from "ng-jhipster";
@Injectable()
export class QualificationService {

	private resourceUrl = 'api/qualifications';

	constructor(private http: Http, private dateUtils: DateUtils) {
	}

	create(qualification: Qualification): Observable<Qualification> {
		let copy: Qualification = Object.assign({}, qualification);
		copy.dateAttained = this.dateUtils
			.convertLocalDateToServer(qualification.dateAttained);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(qualification: Qualification): Observable<Qualification> {
		let copy: Qualification = Object.assign({}, qualification);
		copy.dateAttained = this.dateUtils
			.convertLocalDateToServer(qualification.dateAttained);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<Qualification> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			let jsonResponse = res.json();
			jsonResponse.dateAttained = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.dateAttained);
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
			jsonResponse[i].dateAttained = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].dateAttained);
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
