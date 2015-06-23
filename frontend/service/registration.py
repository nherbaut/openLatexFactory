__author__ = 'nherbaut'
from flask import g
from flask import render_template
from flask import session
from flask import abort

from service.repo import get_repos_for_user

from models import User
from database import db_session
from settings import git_microservice_url


def get_db():
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = connect_to_database()
    return db


def do_sign_up_or_in(username='', password=''):
    return render_template('signup.html',
                           github_signup=git_microservice_url + "github/auth")


def do_sign_up_github(user_name='', user_id=''):
    return render_template('signup.html', user_name=user_name, user_id=user_id, github=True)


def do_sign_up_final(user_name, user_email, user_id):
    session['user_id'] = user_id
    u = User.query.filter_by(id=user_id).first()
    if u is None:
        u = User(user_id, user_name, user_email)
        db_session.add(u)
        db_session.commit()
        return render_template('main.html', user_id=u.id, repos=get_repos_for_user(user_id))
    return abort(404)