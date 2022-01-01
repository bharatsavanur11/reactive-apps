export class Quote {

  id: string;
  book: string;
  content: string;

  constructor(id: string, book: string, content: string) {
    this.id = id;
    this.book = book;
    this.content = content;
  }
}
