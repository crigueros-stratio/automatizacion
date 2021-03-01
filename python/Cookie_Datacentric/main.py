import requests
from bs4 import BeautifulSoup
from requests.packages.urllib3.exceptions import InsecureRequestWarning
from flask import Flask
import logging

app = Flask(__name__)

logging.basicConfig(level=logging.DEBUG)
needed_cookies = ["user"]

@app.route('/')
def index():
	cookies = "; ".join(get_cookies("https://rocket-comfandi.saaslatampd.stratio.com/comfandi-rocket/", "crigueros", "3#dKWmq.fArU"))
	return cookies

def login_sso(url, username, password):

    """
    Function that simulates the login in to sparta endpoint flow with SSO to obtain a valid
    cookie that will be used to make requests to Marathon
    """
    # First request to mesos master to be redirected to gosec sso login
    # page and be given a session cookie
    
    r = requests.Session()
    first_response = r.get(url, verify=False)
    callback_url = first_response.url

    # Parse response body for hidden tags needed in the data of our login post request
    body = first_response.text
    all_tags = BeautifulSoup(body, "lxml").find_all("input", type="hidden")
    tags_to_find = ['lt', 'execution']
    hidden_tags = [tag.attrs for tag in all_tags if tag['name'] in tags_to_find]
    data = {tag['name']: tag['value'] for tag in hidden_tags}

    # Add the rest of needed fields and login credentials in the data of
    # our login post request and send it
    data.update({
        '_eventId': 'submit',
        'submit': 'LOGIN',
        'username': username,
        'password': password,
        'tenant': 'comfandi'
    })
    login_response = r.post(callback_url, data=data, verify=False)
    return login_response


def get_cookies(url, usr, passwd):
    for (k,v) in login_sso(url, usr, passwd).request._cookies.items():
        if k in needed_cookies:
            yield "{}={}".format(k,v)


if __name__ == '__main__':
    app.run(debug=True)


