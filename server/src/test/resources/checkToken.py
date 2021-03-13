#!/usr/bin/env python
# -*- coding: utf-8 -*-

import socket

host = "127.0.0.1"
port = 6666

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((host, port))

req = b"{'command': 'checkToken', 'token': 'eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJMb2dpblRva2VuIiwidXNlcl9pZCI6IjBmMzNkMzc0LTUzOGQtNDk0OC05NTA2LWEyZjIwZTM2YjM2OCIsImlhdCI6MTYxNTQ1MjY2MywiaXNzIjoiZW5zdGEtYnJldGFnbmUiLCJleHAiOjE2MTU3OTgyNjN9.EoC27yKRIhHlW3k3TgHLneYitcID1-nRlyZ4S-PwYqxL912EEjiW8bZLtO4SPpbJw-dIyM5pAnSCXI95cyegDg'}\n"

print("Sending : ", req)
client.send(req)

response = client.recv(4096)
print(response)
