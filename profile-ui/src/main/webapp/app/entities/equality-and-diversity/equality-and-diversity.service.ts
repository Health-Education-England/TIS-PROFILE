import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {EqualityAndDiversity} from "./equality-and-diversity.model";
import {DateUtils} from "ng-jhipster";
@Injectable()
export class EqualityAndDiversityService {

	private resourceUrl = 'api/equality-and-diversities';

	constructor(private http: Http, private dateUtils: DateUtils) {
	}

	create(equalityAndDiversity: EqualityAndDiversity): Observable<EqualityAndDiversity> {
		let copy: EqualityAndDiversity = Object.assign({}, equalityAndDiversity);
		copy.dateOfBirth = this.dateUtils
			.convertLocalDateToServer(equalityAndDiversity.dateOfBirth);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(equalityAndDiversity: EqualityAndDiversity): Observable<EqualityAndDiversity> {
		let copy: EqualityAndDiversity = Object.assign({}, equalityAndDiversity);
		copy.dateOfBirth = this.dateUtils
			.convertLocalDateToServer(equalityAndDiversity.dateOfBirth);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<EqualityAndDiversity> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			let jsonResponse = res.json();
			jsonResponse.dateOfBirth = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.dateOfBirth);
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
			jsonResponse[i].dateOfBirth = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].dateOfBirth);
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
