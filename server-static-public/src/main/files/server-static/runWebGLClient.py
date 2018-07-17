#!/usr/bin/env python

# Documentation: https://docs.python.org/2/library/webbrowser.html
import webbrowser

### Choose one of the following browsers types:

# BROWSER = 'windows-default'
# BROWSER = 'chrome'
# BROWSER = 'firefox'
# BROWSER = 'safari'

### Or choose chrome in Windows, Linux and macOS
# BROWSER = 'C:\Program Files (x86)\Google\Chrome\Application\chrome.exe %s'
# BROWSER = '/usr/bin/google-chrome %s'
BROWSER = 'open -a /Applications/Google\ Chrome.app %s'

### Or, set your own browser:
# BROWSER = 'PATH %s'
# BROWSER = 'PATH %s'
# BROWSER = 'open -a /PATH/ %s'

### Set the WebGL client url:
WEBGL_CLIENT_URL = "http://localhost:8080/static/runner.html"

browser = webbrowser.get(BROWSER)
browser.open_new(WEBGL_CLIENT_URL)
