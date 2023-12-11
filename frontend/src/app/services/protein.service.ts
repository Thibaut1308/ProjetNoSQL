import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ProteinModel} from "../model/protein.model";
import {Observable} from "rxjs";
import {ProteinlinksModel} from "../model/proteinlinks.model";

@Injectable({
  providedIn: 'root'
})
export class ProteinService {
  private domain: string = "http://localhost:8080/neo4j";

  constructor(private http: HttpClient) {
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

  public getNeighborsAndNeigborsofNeighborsFromEntry(entry: string): Observable<ProteinlinksModel[]>  {
    return this.http.get<ProteinlinksModel[]>(this.domain + "/neighboursAndNeighboursOfNeighbours/" + entry, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }
}
