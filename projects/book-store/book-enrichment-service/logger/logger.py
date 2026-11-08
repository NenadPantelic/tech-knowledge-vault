import logging
from logging.handlers import RotatingFileHandler
from pathlib import Path

from config.config import config


def configure_logging() -> None:
    """
    Configure application-wide logging.

    All application logs are written to a single rotating log file.
    """

    log_dir = Path("logs")
    log_dir.mkdir(parents=True, exist_ok=True)

    log_file = log_dir / "application.log"

    file_handler = RotatingFileHandler(
        filename=log_file,
        maxBytes=10 * 1024 * 1024,  # 10 MB
        backupCount=5,
        encoding="utf-8"
    )

    formatter = logging.Formatter(
        fmt=(
            "%(asctime)s | "
            "%(levelname)s | "
            "%(name)s | "
            "%(message)s"
        ),
        datefmt="%Y-%m-%d %H:%M:%S"
    )

    file_handler.setFormatter(formatter)

    logging.basicConfig(
        level=config.log_level.upper(),
        handlers=[file_handler],
        force=True
    )


def get_logger(name: str) -> logging.Logger:
    """Return a logger for the specified module."""
    return logging.getLogger(name)
