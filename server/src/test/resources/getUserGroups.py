#!/usr/bin/env python
# -*- coding: utf-8 -*-

import socket

host = "127.0.0.1"
port = 6666

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((host, port))

req = b"{'command': 'getUserGroups', 'args': {'user_id': 'd48cdb48-15e0-49e2-8304-e4e0589f6319'}}\n"
print("Sending : ", req)
client.send(req)

response = client.recv(4096)
print(response)
