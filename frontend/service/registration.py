__author__ = 'nherbaut'



from flask import render_template


def do_sign_in(username,password):
    return render_template('main.html', username=username)


def do_sign_up(username='',password=''):
    return render_template('signup.html', username=username, password=password)
