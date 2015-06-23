from flask import Flask, request, redirect, url_for, \
    render_template
from flask import session
from flask import abort
import json

from settings import SECRET_KEY
from service.database import *
from service.repo import get_repos_for_user, do_update_repo
from service.registration import do_sign_up_or_in, do_sign_up_github, do_sign_up_final
from service.models import User


app = Flask(__name__)
app.config.from_object(__name__)
app.debug = True


@app.route('/')
def index():
    if session.has_key('user_id'):
        return render_template('main.html', user_id=session["user_id"], repos=get_repos_for_user(session["user_id"]))
    else:
        return redirect(url_for('static', filename='index.html'), code=302)


@app.route('/signup-final', methods=['POST'])
def signup_final():
    return do_sign_up_final(user_name=request.form["user_name"], user_email=request.form["email"],
                            user_id=request.form["user_id"])


@app.route('/signup-github', methods=['GET'])
def signup_github():
    user = User.query.filter_by(id=request.args.get("user_id")).first()
    if user is not None:
        session["user_id"] = user.id
        return render_template('main.html', user_id=user.id, repos=get_repos_for_user(user.id))
    else:
        return do_sign_up_github(user_name=request.args.get("user_name"), user_id=request.args.get("user_id"))


@app.route('/logout', methods=['GET'])
def logout():
    session.clear()
    return index()


@app.route('/sign', methods=['POST'])
def sign():
    error = None
    return do_sign_up_or_in()


@app.route('/repo/<repo_id>', methods=['PUT'])
def update_repo(repo_id):
    body = json.loads(request.data)
    if session.has_key("user_id"):
        return do_update_repo(session["user_id"], repo_id, body["state"])
    else:
        return abort(404)


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


if __name__ == '__main__':
    app.secret_key = SECRET_KEY
    app.run()



