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
  private mongoDomain: string = "http://localhost:8080/mongodb"

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

  public getProteinByEntryId(id: string):Observable<ProteinModel> {
    return this.http.get<ProteinModel>(this.mongoDomain + "/entry/" + id,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getProteinByGO(go: string): Observable<ProteinModel[]> {
    return this.http.get<ProteinModel[]>(this.mongoDomain + "/go/" + go,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getProteinByEntryName(entrName: string): Observable<ProteinModel> {
    return this.http.get<ProteinModel>(this.mongoDomain + "/entryName/" + entrName,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getProteinBySequence(sequence: string): Observable<ProteinModel[]> {
    return this.http.get<ProteinModel[]>(this.mongoDomain + "/sequence/" + sequence,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getProteinByInterPro(interpro: string): Observable<ProteinModel[]> {
    return this.http.get<ProteinModel[]>(this.mongoDomain + "/interpro/" + interpro,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getProteinByDescription(description: string): Observable<ProteinModel[]> {
    return this.http.get<ProteinModel[]>(this.mongoDomain + "/description/" + description,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getProteinByProteinNames(proteinNames: string): Observable<ProteinModel[]> {
    return this.http.get<ProteinModel[]>(this.mongoDomain + "/proteinNames/" + proteinNames,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getProteinCount(): Observable<number> {
    return this.http.get<number>(this.mongoDomain + "/proteinCount",{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getUndescribedProteinCount(): Observable<ProteinModel[]> {
    return this.http.get<ProteinModel[]>(this.mongoDomain + "/undescribedProteinCount",{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  public getNoInterProteinCount(): Observable<ProteinModel[]> {
    return this.http.get<ProteinModel[]>(this.mongoDomain + "/noInterProtein",{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

}
