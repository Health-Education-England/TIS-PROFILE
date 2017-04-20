import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {HeeUser} from "./hee-user.model";
@Injectable()
export class HeeUserService {

	private resourceUrl = 'api/hee-users';

	constructor(private http: Http) {
	}

	create(heeUser: HeeUser): Observable<HeeUser> {
		let copy: HeeUser = Object.assign({}, heeUser);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(heeUser: HeeUser): Observable<HeeUser> {
		let copy: HeeUser = Object.assign({}, heeUser);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(name: string): Observable<HeeUser> {
		return this.http.get(`${this.resourceUrl}/${name}`).map((res: Response) => {
			return res.json();
		});
	}

	query(req?: any): Observable<Response> {
		let options = this.createRequestOption(req);
		return this.http.get(this.resourceUrl, options)
			;
	}

	delete(name: string): Observable<Response> {
		return this.http.delete(`${this.resourceUrl}/${name}`);
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
