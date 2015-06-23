from flask import Flask, request, redirect, url_for, \
    render_template
from settings import SECRET_KEY
from service.database import *






from service.registration import do_sign_in, do_sign_up, do_sign_up_github, do_sign_up_final


app = Flask(__name__)
app.config.from_object(__name__)
app.debug = True


@app.route('/')
def hello_world():
    return redirect(url_for('static', filename='index.html'), code=302)


@app.route('/signup-final', methods=['POST'])
def signup_final():
    return do_sign_up_final(user_name=request.form["user_name"], user_email=request.form["email"],
                            user_id=request.form["user_id"])


@app.route('/signup-github', methods=['GET'])
def signup_github():
    return do_sign_up_github(user_name=request.args.get("user_name"), user_id=request.args.get("user_id"))


@app.route('/sign', methods=['POST'])
def sign():
    error = None
    if request.form.has_key("signin"):
        return do_sign_in(request.form["email"], request.form["password"])

    elif request.form.has_key("signup"):
        return do_sign_up(request.form["email"], request.form["password"])

    return render_template('static/index.html', error=error)


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


if __name__ == '__main__':
    app.secret_key=SECRET_KEY
    app.run()



