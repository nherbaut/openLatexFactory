__author__ = 'nherbaut'
import requests
import json

from settings import git_microservice_url


def get_repos_for_user(userId):
    headers = {'Accept': 'application/json'}

    r = requests.get(git_microservice_url + "repos/" + userId, headers=headers)
    if r.status_code == 200:
        return r.json()
    else:
        return "[]"



def do_update_repo(user_id, repo_id, state):
    data = json.dumps({"state": state})
    r = requests.put(git_microservice_url + "users/" + user_id + "/" + repo_id, data)
    return '', r.status_code

