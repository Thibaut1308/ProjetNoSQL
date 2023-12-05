import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ProteinModel} from "../model/protein.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProteinService {
  private domain: string = "http://localhost:8080/protein";
  private http: HttpClient;

  constructor(http: HttpClient) {
    this.http = http;
  }

  public getProteinFromEntry(entry: string): Observable<ProteinModel>  {
    return this.http.get<ProteinModel>(this.domain + "/entry/" + entry, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getProteinFromEntryName(entryName: string): Observable<ProteinModel>  {
    return this.http.get<ProteinModel>(this.domain + "/entryName/" + entryName, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getProteinFromDescription(description: string): Observable<ProteinModel>  {
    return this.http.get<ProteinModel>(this.domain + "/description/" + description, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getNeighboursFromEntry(entry: string): Observable<ProteinModel[]>  {
    return this.http.get<ProteinModel[]>(this.domain + "/neighbours/" + entry, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getNeighborsAndNeigborsofNeighborsFromEntry(entry: string): Observable<Map<ProteinModel, ProteinModel[]>>  {
    return this.http.get<Map<ProteinModel, ProteinModel[]>>(this.domain + "/neighboursAndNeighboursOfNeighbours/" + entry, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }
}
