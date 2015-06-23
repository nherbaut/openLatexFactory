__author__ = 'nherbaut'
import requests

from settings import git_microservice_url


def get_repos_for_user(userId):
    headers = {'Accept': 'application/json'}

    r = requests.get(git_microservice_url + "repos/" + userId, headers=headers )
    if r.status_code == 200:
        return r.json()



