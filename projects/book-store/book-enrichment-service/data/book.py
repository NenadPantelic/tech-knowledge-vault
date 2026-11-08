import dataclasses
from dataclasses import dataclass


@dataclass
class Book:
    book_id: str
    title: str = ""
    authors: list[str] = dataclasses.field(default_factory=list)
    publishing_year: int = 1900
