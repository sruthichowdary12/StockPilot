"""MySQL connection; credentials are loaded from .env."""
import os
from dotenv import load_dotenv
from sqlalchemy import create_engine
from sqlalchemy.engine import URL

load_dotenv()

def mysql_engine():
    password = os.getenv('MYSQL_PASSWORD')
    if password is None or password == 'CHANGE_ME':
        raise ValueError('Set MYSQL_PASSWORD in .env before running.')
    url = URL.create(
        'mysql+pymysql',
        username=os.getenv('MYSQL_USER', 'stockpilot_reader'),
        password=password,
        host=os.getenv('MYSQL_HOST', 'localhost'),
        port=int(os.getenv('MYSQL_PORT', '3306')),
        database=os.getenv('MYSQL_DATABASE', 'inventory_stress_tester'),
    )
    return create_engine(url, pool_pre_ping=True)
