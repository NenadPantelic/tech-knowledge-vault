import os
from dataclasses import dataclass
from dotenv import load_dotenv

# Load .env for local development.
# Existing environment variables take precedence.
load_dotenv()


def required_env(name: str) -> str:
    value = os.getenv(name)

    if not value:
        raise ValueError(f"Missing required environment variable: {name}")

    return value


def env_with_default(name: str, default_value: str) -> str:
    value = os.getenv(name)

    if not value:
        # print
        return default_value

    return value


@dataclass(frozen=True)
class Config:
    app_name: str
    log_level: str
    port: int

    kafka_bootstrap_servers: list[str]
    kafka_book_requests_topic: str
    kafka_book_results_topic: str
    kafka_consumer_group: str
    kafka_auto_offset_reset: str
    kafka_consumer_timeout_in_secs: int


config = Config(
    app_name=os.getenv("APP_NAME", "book-enrichment-service"),
    log_level=os.getenv("LOG_LEVEL", "INFO"),
    port=int(os.getenv("PORT", "5000")),

    kafka_bootstrap_servers=[
        server.strip()
        for server in required_env("KAFKA_BOOTSTRAP_SERVERS").split(",")
    ],

    kafka_book_requests_topic=required_env(
        "KAFKA_BOOK_REQUESTS_TOPIC"
    ),

    kafka_book_results_topic=required_env(
        "KAFKA_BOOK_RESULTS_TOPIC"
    ),

    kafka_consumer_group=required_env(
        "KAFKA_CONSUMER_GROUP"
    ),
    kafka_auto_offset_reset=required_env(
        "KAFKA_AUTO_OFFSET_RESET"
    ),
    kafka_consumer_timeout_in_secs=int(env_with_default(
        "KAFKA_CONSUMER_TIMEOUT_IN_SECS", "5.0")
    )
)
