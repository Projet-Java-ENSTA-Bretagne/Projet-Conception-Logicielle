#!/usr/bin/env python
# -*- coding: utf-8 -*-

import socket

host = "127.0.0.1"
port = 6666

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((host, port))
client.send(b"{'command': 'createUser', 'args': {'username': 'thomate', 'password': 'mypassword', 'role': 'ROLE_USER'}}\n")

response = client.recv(4096)
print(response)
