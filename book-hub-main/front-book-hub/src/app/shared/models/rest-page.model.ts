export interface RestPage<T> {
  content: T[]; // La liste des éléments de la page actuelle (ex: vos livres)
  totalElements: number; // Le nombre total d'éléments en base de données
  totalPages: number; // Le nombre total de pages calculé par le back
  size: number; // Le nombre d'éléments par page
  number: number; // Le numéro de la page actuelle (0, 1, 2...)
  first: boolean; // Est-ce la première page ?
  last: boolean; // Est-ce la dernière page ?
}
