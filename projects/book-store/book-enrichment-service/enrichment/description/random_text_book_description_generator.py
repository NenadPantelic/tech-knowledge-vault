import random
import string

from data.book import Book
from data.enrichment import DescriptionGenerationRequest
from enrichment.description.book_description_generator import BookDescriptionGenerator
from logger import logger

CHAR_SET = string.ascii_letters + string.digits + string.punctuation
CHAR_SET_LENGTH = len(CHAR_SET)

logger = logger.get_logger(__name__)


class RandomTextBookDescriptionGenerator(BookDescriptionGenerator):
    def __init__(self):
        super().__init__()

    # TODO
    def generate(self, book: Book, book_description_request: DescriptionGenerationRequest) -> str:
        """
        Generates a random text book description.
        :param book: a book whose description is to be generated
        :param book_description_request: a description request
        :return: a string of random text book description
        """
        logger.debug("Generating a random text book description")
        length = book_description_request.length
        chars = []
        for _ in range(length):
            char_idx = random.randint(0, CHAR_SET_LENGTH - 1)
            chars.append(CHAR_SET[char_idx])

        return "".join(chars)
