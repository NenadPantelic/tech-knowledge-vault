from data.book import Book
from enrichment.description.book_description_generator import BookDescriptionGenerator


class ChatGPTBookDescriptionGenerator(BookDescriptionGenerator):
    def __init__(self):
        super().__init__()
        pass

    def generate(self, book: Book) -> str:
        """
        Generates a description of the book.

        :param book:
        :return:
        """
        return ""
