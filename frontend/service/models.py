__author__ = 'nherbaut'

from sqlalchemy import Column, String

from database import Base


class User(Base):
    __tablename__ = 'users'
    id = Column(String(50), primary_key=True)
    name = Column(String(50), unique=False)
    email = Column(String(120), unique=False)

    def __init__(self, id=None, name=None, email=None):
        self.name = name
        self.email = email
        self.id = id

    def __repr__(self):
        return '<User %r>' % (self.name)