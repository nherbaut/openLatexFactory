from flask import Flask
from flask import request
from service.registration import do_sign_in, do_sign_up
from flask import render_template, redirect, url_for

app = Flask(__name__)
app.debug = True

@app.route('/')
def hello_world():
    return redirect(url_for('static', filename='index.html'), code=302)


@app.route('/sign', methods=['POST'])
def sign():
   error = None
   if request.form.has_key("signin"):
       return do_sign_in(request.form["email"],request.form["password"])

   elif request.form.has_key("signup"):
       return do_sign_up(request.form["email"],request.form["password"])


   return render_template('static/index.html', error=error)






if __name__ == '__main__':
    app.run()
